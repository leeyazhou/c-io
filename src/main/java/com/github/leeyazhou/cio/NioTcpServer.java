package com.github.leeyazhou.cio;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.channel.ChannelInitializer;
import com.github.leeyazhou.cio.message.MessageReaderFactory;
import com.github.leeyazhou.cio.util.concurrent.WorkerThread;

public class NioTcpServer {
	private static final Logger logger = LoggerFactory.getLogger(NioTcpServer.class);
	private Acceptor acceptor = null;

	private MessageReaderFactory messageReaderFactory = null;
	private ServerConfig serverConfig;
	private final WorkerThread[] ioThreads;
	private ChannelInitializer channelInitializer;

	public NioTcpServer(ServerConfig serverConfig, MessageReaderFactory messageReaderFactory) {
		this.messageReaderFactory = messageReaderFactory;
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

		NioChannelProcessor[] processors = new NioChannelProcessor[ioThreads.length];
		for (int i = 0; i < processors.length; i++) {
			NioChannelProcessor processor = new NioChannelProcessor(messageReaderFactory);
			processors[i] = processor;
			ioThreads[i].setTask(processor);
		}
		this.acceptor = new Acceptor(serverConfig.getHost(), serverConfig.getPort(), processors);
		acceptor.setChannelInitializer(channelInitializer);
		for (WorkerThread worker : ioThreads) {
			worker.start();
		}

		WorkerThread connectorThread = new WorkerThread();
		connectorThread.setTask(acceptor);
		acceptor.init();
		connectorThread.start();
	}

	public void setChannelInitializer(ChannelInitializer channelInitializer) {
		this.channelInitializer = channelInitializer;
	}

	public void shutdown() {
		acceptor.shutdown();
	}
}
