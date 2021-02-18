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

import com.github.leeyazhou.cio.channel.DefaultChannelContext;
import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageBuffer;
import com.github.leeyazhou.cio.message.MessageReader;
import com.github.leeyazhou.cio.message.MessageReaderFactory;
import com.github.leeyazhou.cio.message.MessageWriter;

public class NioChannelProcessor implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(NioChannelProcessor.class);
	private Queue<DefaultChannelContext> inboundChannelQueue = null;

	private MessageBuffer readMessageBuffer = null;
	private MessageBuffer writeMessageBuffer = null;
	private MessageReaderFactory messageReaderFactory = null;

	private Queue<Message> outboundMessageQueue = new LinkedList<>();

	private Map<Long, DefaultChannelContext> socketCache = new HashMap<>();

	private ByteBuffer readByteBuffer = ByteBuffer.allocate(1024 * 1024);
	private ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024 * 1024);
	private Selector selector = null;


	private boolean running = true;

	public NioChannelProcessor(MessageReaderFactory messageReaderFactory)
			throws IOException {
		this.inboundChannelQueue = new ArrayBlockingQueue<>(10240);

		this.readMessageBuffer = new MessageBuffer();
		this.writeMessageBuffer = new MessageBuffer();

		this.messageReaderFactory = messageReaderFactory;

		this.selector = Selector.open();
	}

	public void run() {
		while (running) {
			try {
				executeCycle();
			} catch (IOException e) {
				logger.error("", e);
			}

		}
		if (!running) {
			try {
				selector.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

	public void executeCycle() throws IOException {
		takeNewChannels();
		processSelectionKeys();
	}

	public void takeNewChannels() throws IOException {
		DefaultChannelContext channelContext = null;

		while ((channelContext = this.inboundChannelQueue.poll()) != null) {
			channelContext.getChannel().configureBlocking(false);

			MessageReader messageReader = messageReaderFactory.createMessageReader();
			messageReader.init(this.readMessageBuffer);
			channelContext.setMessageReader(messageReader);
			channelContext.setOutboundMessageQueue(outboundMessageQueue);

			this.socketCache.put(channelContext.getChannel().getId(), channelContext);

			channelContext.getChannel().register(selector, SelectionKey.OP_READ, channelContext);
		}
	}

	public void processSelectionKeys() throws IOException {
		int readReady = this.selector.select();
		if (readReady > 0) {
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();
				DefaultChannelContext context = (DefaultChannelContext) key.attachment();
				try {
					readFromSocket(key);
					writeToChannel(key);
				} catch (Exception e) {
					logger.error("", e);
				} finally {
					if (context.isEndOfStreamReached()) {
						context.close();
					}
				}

			}
		}
	}

	private void readFromSocket(SelectionKey key) throws IOException {
		DefaultChannelContext context = (DefaultChannelContext) key.attachment();
		context.getMessageReader().read(context, readByteBuffer);

		List<Message> messages = context.getMessageReader().getMessages();
		if (messages.size() > 0) {
			for (Message message : messages) {
				message.setChannelId(context.getChannel().getId());
				context.channelChain().fireChannelRead(message);
			}
			messages.clear();
		}

	}

	public void writeToChannel(SelectionKey key) throws IOException {
		// Take all new messages from outboundMessageQueue
		takeNewOutboundMessages();

		DefaultChannelContext channelContext = (DefaultChannelContext) key.attachment();
		logger.info("Socket : {}, Message Writer : {}", channelContext);

		channelContext.getMessageWriter().write(channelContext, writeByteBuffer);
	}

	private void takeNewOutboundMessages() {
		Message outMessage = null;
		while ((outMessage = outboundMessageQueue.poll()) != null) {
			DefaultChannelContext channelContext = this.socketCache.get(outMessage.getChannelId());

			if (channelContext != null) {
				MessageWriter messageWriter = channelContext.getMessageWriter();
				messageWriter.enqueue(outMessage);
			}

		}
	}

	public void addChannel(DefaultChannelContext channelContext) {
		inboundChannelQueue.add(channelContext);
		selector.wakeup();
	}

}
