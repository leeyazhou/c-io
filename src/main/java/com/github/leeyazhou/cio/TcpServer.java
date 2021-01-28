package com.github.leeyazhou.cio;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.message.MessageProcessor;
import com.github.leeyazhou.cio.message.MessageReaderFactory;
import com.github.leeyazhou.cio.util.concurrent.WorkerThread;

public class TcpServer {

	private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);
	private Connector connector = null;

	private MessageReaderFactory messageReaderFactory = null;
	private MessageProcessor messageProcessor = null;
	private ServerConfig serverConfig;
	private final WorkerThread[] ioThreads;
	private boolean running = true;

	public TcpServer(ServerConfig serverConfig, MessageReaderFactory messageReaderFactory,
			MessageProcessor messageProcessor) {
		this.messageReaderFactory = messageReaderFactory;
		this.messageProcessor = messageProcessor;
		this.serverConfig = serverConfig;
		this.ioThreads = new WorkerThread[serverConfig.getIoThread()];
		for (int i = 0; i < serverConfig.getIoThread(); i++) {
			ioThreads[i] = new WorkerThread();
		}
	}

	public void setServerConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	public void init() {

	}

	public void start() throws IOException {
		logger.info("start tcp server, listen {}", serverConfig.getPort());

		ChannelProcessor[] processors = new ChannelProcessor[ioThreads.length];
		for (int i = 0; i < processors.length; i++) {
			ChannelProcessor processor = new ChannelProcessor(messageReaderFactory, messageProcessor);
			processors[i] = processor;
			ioThreads[i].setTask(processor);
		}

		this.connector = new Connector(serverConfig.getHost(), serverConfig.getPort(), processors);

		for (WorkerThread worker : ioThreads) {
			worker.start();
		}

		WorkerThread connectorThread = new WorkerThread();
		connectorThread.setTask(connector);
		connector.init();
		connectorThread.start();
	}

	public void shutdown() {
		this.running = false;
	}
}
