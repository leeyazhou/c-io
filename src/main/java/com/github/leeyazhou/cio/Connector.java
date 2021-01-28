package com.github.leeyazhou.cio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connector implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(Connector.class);
	private final String host;
	private final int tcpPort;

	private ServerSocketChannel serverSocket = null;
	private SocketProcessor[] processors;
	private AtomicInteger ioIndex = new AtomicInteger();

	public Connector(String host, int tcpPort, SocketProcessor[] processors) {
		this.tcpPort = tcpPort;
		this.host = host;
		this.processors = processors;
	}

	public void init() {
		try {
			this.serverSocket = ServerSocketChannel.open();

			InetSocketAddress address = null;
			if (host == null || host.length() == 0) {
				address = new InetSocketAddress(tcpPort);
			} else {
				address = new InetSocketAddress(host, tcpPort);
			}
			this.serverSocket.bind(address);
			logger.info("开启ServerSocketChannel并绑定端口：{}", tcpPort);
		} catch (IOException e) {
			logger.error("开启ServerSocketChannel失败", e);
		}
	}

	public void run() {

		while (true) {
			try {
				SocketChannel socketChannel = this.serverSocket.accept();
				logger.info("Socket accepted: {}", socketChannel);
				nextWoker().add(new ChannelContext(socketChannel));
			} catch (IOException e) {
				logger.error("", e);
			}

		}

	}

	private SocketProcessor nextWoker() {
		return processors[ioIndex.incrementAndGet() % processors.length];
	}
}
