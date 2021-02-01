package com.github.leeyazhou.cio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageBuffer;
import com.github.leeyazhou.cio.message.MessageProcessor;
import com.github.leeyazhou.cio.message.MessageReader;
import com.github.leeyazhou.cio.message.MessageReaderFactory;
import com.github.leeyazhou.cio.message.MessageWriter;

public class NioChannelProcessor implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(NioChannelProcessor.class);
	private Queue<ChannelContext> inboundChannelQueue = null;

	private MessageBuffer readMessageBuffer = null;
	private MessageBuffer writeMessageBuffer = null;
	private MessageReaderFactory messageReaderFactory = null;

	private Queue<Message> outboundMessageQueue = new LinkedList<>();

	private Map<Long, ChannelContext> socketCache = new HashMap<>();

	private ByteBuffer readByteBuffer = ByteBuffer.allocate(1024 * 1024);
	private ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024 * 1024);
	private Selector readSelector = null;

	private MessageProcessor messageProcessor = null;
	private WriteProxy writeProxy = null;

	private boolean running = true;
	private long nextSocketId = 16 * 1024;

	public NioChannelProcessor(MessageReaderFactory messageReaderFactory, MessageProcessor messageProcessor)
			throws IOException {
		this.inboundChannelQueue = new ArrayBlockingQueue<>(1024);

		this.readMessageBuffer = new MessageBuffer();
		this.writeMessageBuffer = new MessageBuffer();
		this.writeProxy = new WriteProxy(writeMessageBuffer, this.outboundMessageQueue);

		this.messageReaderFactory = messageReaderFactory;

		this.messageProcessor = messageProcessor;

		this.readSelector = Selector.open();
	}

	public void run() {
		while (running) {
			try {
				executeCycle();
			} catch (IOException e) {
				logger.error("", e);
			}

			try {
				Thread.sleep(100);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	public void executeCycle() throws IOException {
		takeNewChannels();
		readFromChannels();
	}

	public void takeNewChannels() throws IOException {
		ChannelContext chanelContext = null;

		while ((chanelContext = this.inboundChannelQueue.poll()) != null) {
			chanelContext.setChannelId(nextSocketId++);
			chanelContext.getChannel().configureBlocking(false);

			MessageReader messageReader = messageReaderFactory.createMessageReader();
			messageReader.init(this.readMessageBuffer);
			chanelContext.setMessageReader(messageReader);

			chanelContext.setMessageWriter(new MessageWriter());

			this.socketCache.put(chanelContext.getChannelId(), chanelContext);

			chanelContext.getChannel().register(readSelector, SelectionKey.OP_READ, chanelContext);
		}
	}

	public void readFromChannels() throws IOException {
		logger.info("start read from channel");
		int readReady = this.readSelector.select();
		logger.info("read ready size : {}", readReady);
		if (readReady > 0) {
			Iterator<SelectionKey> it = readSelector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				logger.info("read selectionKey, valid:{}, readable:{}, writable:{}", key.isValid(), key.isReadable(),
						key.isWritable());

				try {
					readFromSocket(key);
					writeToChannels(key);
				} catch (Exception e) {
					logger.error("", e);
				} finally {
					logger.info("valid:{}", key.isValid());
					key.cancel();
					key.channel().close();
					it.remove();
				}

			}
		}
	}

	private void readFromSocket(SelectionKey key) throws IOException {
		ChannelContext socket = (ChannelContext) key.attachment();
		socket.getMessageReader().read(socket, readByteBuffer);

		List<Message> fullMessages = socket.getMessageReader().getMessages();
		if (fullMessages.size() > 0) {
			for (Message message : fullMessages) {
				message.setChannelId(socket.getChannelId());
				messageProcessor.process(message, writeProxy);
			}
			fullMessages.clear();
		}

	}

	public void writeToChannels(SelectionKey key) throws IOException {

		// Take all new messages from outboundMessageQueue
		takeNewOutboundMessages();

		ChannelContext socket = (ChannelContext) key.attachment();
		logger.info("Socket : {}, Message Writer : {}", socket);
		socket.getMessageWriter().write(socket, this.writeByteBuffer);

	}

	private void takeNewOutboundMessages() {
		Message outMessage = null;
		while ((outMessage = outboundMessageQueue.poll()) != null) {
			ChannelContext socket = this.socketCache.get(outMessage.getChannelId());

			if (socket != null) {
				MessageWriter messageWriter = socket.getMessageWriter();
				messageWriter.enqueue(outMessage);
			}

		}
	}

	public void add(ChannelContext channelContext) {
		inboundChannelQueue.add(channelContext);
		readSelector.wakeup();
	}

}
