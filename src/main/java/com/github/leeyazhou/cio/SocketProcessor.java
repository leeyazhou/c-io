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

public class SocketProcessor implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SocketProcessor.class);
	private Queue<ChannelContext> inboundSocketQueue = null;

	private MessageBuffer readMessageBuffer = null; // todo Not used now - but perhaps will be later - to check for
													// space in the buffer before reading from sockets

	private MessageBuffer writeMessageBuffer = null; // todo Not used now - but perhaps will be later - to check for
														// space in the buffer before reading from sockets (space for
														// more to write?)

	private MessageReaderFactory messageReaderFactory = null;

	private Queue<Message> outboundMessageQueue = new LinkedList<>(); // todo use a better / faster queue.

	private Map<Long, ChannelContext> socketMap = new HashMap<>();

	private ByteBuffer readByteBuffer = ByteBuffer.allocate(1024 * 1024);
	private ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024 * 1024);
	private Selector readSelector = null;
	private Selector writeSelector = null;

	private MessageProcessor messageProcessor = null;
	private WriteProxy writeProxy = null;

	private long nextSocketId = 16 * 1024; // start incoming socket ids from 16K - reserve bottom ids for pre-defined
											// sockets (servers).

	private Set<ChannelContext> emptyToNonEmptySockets = new HashSet<>();
	private Set<ChannelContext> nonEmptyToEmptySockets = new HashSet<>();

	public SocketProcessor(MessageReaderFactory messageReaderFactory, MessageProcessor messageProcessor)
			throws IOException {
		this.inboundSocketQueue = new ArrayBlockingQueue<>(1024);

		this.readMessageBuffer = new MessageBuffer();
		this.writeMessageBuffer = new MessageBuffer();
		this.writeProxy = new WriteProxy(writeMessageBuffer, this.outboundMessageQueue);

		this.messageReaderFactory = messageReaderFactory;

		this.messageProcessor = messageProcessor;

		this.readSelector = Selector.open();
		this.writeSelector = Selector.open();
	}

	public void run() {
		while (true) {
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
		takeNewSockets();
		readFromSockets();
		writeToSockets();
	}

	public void takeNewSockets() throws IOException {
		ChannelContext newSocket = null;

		while ((newSocket = this.inboundSocketQueue.poll()) != null) {
			newSocket.setSocketId(nextSocketId++);
			newSocket.getSocketChannel().configureBlocking(false);

			MessageReader messageReader = messageReaderFactory.createMessageReader();
			messageReader.init(this.readMessageBuffer);
			newSocket.setMessageReader(messageReader);

			newSocket.setMessageWriter(new MessageWriter());

			this.socketMap.put(newSocket.getSocketId(), newSocket);

			SelectionKey key = newSocket.getSocketChannel().register(this.readSelector, SelectionKey.OP_READ);
			key.attach(newSocket);
		}
	}

	public void readFromSockets() throws IOException {
		int readReady = this.readSelector.selectNow();

		if (readReady > 0) {
			Set<SelectionKey> selectedKeys = this.readSelector.selectedKeys();
			Iterator<SelectionKey> it = selectedKeys.iterator();

			while (it.hasNext()) {
				SelectionKey key = it.next();

				readFromSocket(key);

				it.remove();
			}
			selectedKeys.clear();
		}
	}

	private void readFromSocket(SelectionKey key) throws IOException {
		ChannelContext socket = (ChannelContext) key.attachment();
		socket.getMessageReader().read(socket, this.readByteBuffer);

		List<Message> fullMessages = socket.getMessageReader().getMessages();
		if (fullMessages.size() > 0) {
			for (Message message : fullMessages) {
				message.socketId = socket.getSocketId();
				this.messageProcessor.process(message, this.writeProxy); // the message processor will eventually push
																			// outgoing messages into an IMessageWriter
																			// for this socket.
			}
			fullMessages.clear();
		}

		if (socket.isEndOfStreamReached()) {
			logger.info("Socket closed: {}", socket.getSocketId());
			this.socketMap.remove(socket.getSocketId());
			key.attach(null);
			key.cancel();
			key.channel().close();
		}
	}

	public void writeToSockets() throws IOException {

		// Take all new messages from outboundMessageQueue
		takeNewOutboundMessages();

		// Cancel all sockets which have no more data to write.
		cancelEmptySockets();

		// Register all sockets that *have* data and which are not yet registered.
		registerNonEmptySockets();

		// Select from the Selector.
		int writeReady = this.writeSelector.selectNow();

		if (writeReady > 0) {
			Set<SelectionKey> selectionKeys = this.writeSelector.selectedKeys();
			Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

			while (keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();

				ChannelContext socket = (ChannelContext) key.attachment();

				socket.getMessageWriter().write(socket, this.writeByteBuffer);

				if (socket.getMessageWriter().isEmpty()) {
					this.nonEmptyToEmptySockets.add(socket);
				}

				keyIterator.remove();
			}

			selectionKeys.clear();

		}
	}

	private void registerNonEmptySockets() throws ClosedChannelException {
		for (ChannelContext socket : emptyToNonEmptySockets) {
			socket.getSocketChannel().register(this.writeSelector, SelectionKey.OP_WRITE, socket);
		}
		emptyToNonEmptySockets.clear();
	}

	private void cancelEmptySockets() {
		for (ChannelContext socket : nonEmptyToEmptySockets) {
			SelectionKey key = socket.getSocketChannel().keyFor(this.writeSelector);

			key.cancel();
		}
		nonEmptyToEmptySockets.clear();
	}

	private void takeNewOutboundMessages() {
		Message outMessage = this.outboundMessageQueue.poll();
		while (outMessage != null) {
			ChannelContext socket = this.socketMap.get(outMessage.socketId);

			if (socket != null) {
				MessageWriter messageWriter = socket.getMessageWriter();
				if (messageWriter.isEmpty()) {
					messageWriter.enqueue(outMessage);
					nonEmptyToEmptySockets.remove(socket);
					emptyToNonEmptySockets.add(socket); // not necessary if removed from nonEmptyToEmptySockets in prev.
														// statement.
				} else {
					messageWriter.enqueue(outMessage);
				}
			}

			outMessage = this.outboundMessageQueue.poll();
		}
	}

	public void add(ChannelContext channelContext) {
		inboundSocketQueue.add(channelContext);
	}

}
