package com.github.leeyazhou.cio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageBuffer;
import com.github.leeyazhou.cio.message.MessageProcessor;
import com.github.leeyazhou.cio.message.MessageReader;
import com.github.leeyazhou.cio.message.MessageReaderFactory;
import com.github.leeyazhou.cio.message.MessageWriter;

public class ChannelProcessor implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(ChannelProcessor.class);
	private Queue<ChannelContext> inboundChannelQueue = null;

	private MessageBuffer readMessageBuffer = null;
	private MessageBuffer writeMessageBuffer = null;
	private MessageReaderFactory messageReaderFactory = null;

	private Queue<Message> outboundMessageQueue = new LinkedList<>();

	private Map<Long, ChannelContext> socketMap = new HashMap<>();

	private ByteBuffer readByteBuffer = ByteBuffer.allocate(1024 * 1024);
	private ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024 * 1024);
	private Selector readSelector = null;
	private Selector writeSelector = null;

	private MessageProcessor messageProcessor = null;
	private WriteProxy writeProxy = null;

	private boolean running = true;
	private long nextSocketId = 16 * 1024; // start incoming socket ids from 16K - reserve bottom ids for pre-defined
											// sockets (servers).

	private Set<ChannelContext> emptyToNonEmptyChannels = new HashSet<>();
	private Set<ChannelContext> nonEmptyToEmptyChannels = new HashSet<>();

	public ChannelProcessor(MessageReaderFactory messageReaderFactory, MessageProcessor messageProcessor)
			throws IOException {
		this.inboundChannelQueue = new ArrayBlockingQueue<>(1024);

		this.readMessageBuffer = new MessageBuffer();
		this.writeMessageBuffer = new MessageBuffer();
		this.writeProxy = new WriteProxy(writeMessageBuffer, this.outboundMessageQueue);

		this.messageReaderFactory = messageReaderFactory;

		this.messageProcessor = messageProcessor;

		this.readSelector = Selector.open();
		this.writeSelector = Selector.open();
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
		writeToChannels();
	}

	public void takeNewChannels() throws IOException {
		ChannelContext newChanelContext = null;

		while ((newChanelContext = this.inboundChannelQueue.poll()) != null) {
			newChanelContext.setChannelId(nextSocketId++);
			newChanelContext.getChannel().configureBlocking(false);

			MessageReader messageReader = messageReaderFactory.createMessageReader();
			messageReader.init(this.readMessageBuffer);
			newChanelContext.setMessageReader(messageReader);

			newChanelContext.setMessageWriter(new MessageWriter());

			this.socketMap.put(newChanelContext.getChannelId(), newChanelContext);

			SelectionKey key = newChanelContext.getChannel().register(this.readSelector, SelectionKey.OP_READ);
			key.attach(newChanelContext);
		}
	}

	public void readFromChannels() throws IOException {
		logger.info("start read from channel");
		int readReady = this.readSelector.selectNow();
//		int readReady = this.readSelector.select();
		logger.info("read ready size : {}", readReady);
		if (readReady > 0) {
			Set<SelectionKey> selectedKeys = this.readSelector.selectedKeys();
			Iterator<SelectionKey> it = selectedKeys.iterator();

			while (it.hasNext()) {
				SelectionKey key = it.next();

				try {
					readFromSocket(key);
				} catch (Exception e) {
					logger.error("", e);
				} finally {
					it.remove();
				}

			}
//			selectedKeys.clear();
		}
	}

	private void readFromSocket(SelectionKey key) throws IOException {
		ChannelContext socket = (ChannelContext) key.attachment();
		socket.getMessageReader().read(socket, this.readByteBuffer);

		List<Message> fullMessages = socket.getMessageReader().getMessages();
		if (fullMessages.size() > 0) {
			for (Message message : fullMessages) {
				message.setChannelId(socket.getChannelId());
				this.messageProcessor.process(message, this.writeProxy); // the message processor will eventually push
																			// outgoing messages into an IMessageWriter
																			// for this socket.
			}
			fullMessages.clear();
		}

		if (socket.isEndOfStreamReached()) {
			logger.info("Socket closed: {}", socket.getChannelId());
			this.socketMap.remove(socket.getChannelId());
			key.attach(null);
			key.cancel();
			key.channel().close();
		}
	}

	public void writeToChannels() throws IOException {

		// Take all new messages from outboundMessageQueue
		takeNewOutboundMessages();

		// Cancel all sockets which have no more data to write.
		cancelEmptySockets();

		// Register all sockets that *have* data and which are not yet registered.
		registerNonEmptySockets();

		logger.info("start write out");
		// Select from the Selector.
		int writeReady = this.writeSelector.selectNow();
//		int writeReady = this.writeSelector.select();
		logger.info("write ready size : {}", writeReady);

		if (writeReady > 0) {
			Set<SelectionKey> selectionKeys = this.writeSelector.selectedKeys();
			Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

			while (keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();

				ChannelContext socket = (ChannelContext) key.attachment();

				socket.getMessageWriter().write(socket, this.writeByteBuffer);

				if (socket.getMessageWriter().isEmpty()) {
					this.nonEmptyToEmptyChannels.add(socket);
				}

				keyIterator.remove();
			}

//			selectionKeys.clear();

		}
	}

	private void registerNonEmptySockets() throws ClosedChannelException {
		for (ChannelContext socket : emptyToNonEmptyChannels) {
			socket.getChannel().register(this.writeSelector, SelectionKey.OP_WRITE, socket);
		}
		emptyToNonEmptyChannels.clear();
	}

	private void cancelEmptySockets() {
		for (ChannelContext socket : nonEmptyToEmptyChannels) {
			SelectionKey key = socket.getChannel().keyFor(this.writeSelector);

			key.cancel();
		}
		nonEmptyToEmptyChannels.clear();
	}

	private void takeNewOutboundMessages() {
		Message outMessage = null;
		while ((outMessage = outboundMessageQueue.poll()) != null) {
			ChannelContext socket = this.socketMap.get(outMessage.getChannelId());

			if (socket != null) {
				MessageWriter messageWriter = socket.getMessageWriter();
				if (messageWriter.isEmpty()) {
					messageWriter.enqueue(outMessage);
					nonEmptyToEmptyChannels.remove(socket);
					emptyToNonEmptyChannels.add(socket); // not necessary if removed from nonEmptyToEmptySockets in
															// prev.
															// statement.
				} else {
					messageWriter.enqueue(outMessage);
				}
			}

		}
	}

	public void add(ChannelContext channelContext) {
		inboundChannelQueue.add(channelContext);
//		readSelector.wakeup();
//		writeSelector.wakeup();
	}

}
