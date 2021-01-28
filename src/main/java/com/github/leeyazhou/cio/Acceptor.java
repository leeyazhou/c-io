package com.github.leeyazhou.cio;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Acceptor implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(Acceptor.class);
	private final String host;
	private final int tcpPort;

	private ServerSocketChannel serverSocket = null;
	private NioChannelProcessor[] processors;
	private AtomicInteger ioIndex = new AtomicInteger();

	public Acceptor(String host, int tcpPort, NioChannelProcessor[] processors) {
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
		} catch (Exception e) {
			logger.error("开启ServerSocketChannel失败", e);
		}
	}

	public void run() {

		while (true) {
			try {
				SocketChannel socketChannel = this.serverSocket.accept();
				logger.info("Socket accepted: {}", socketChannel);
				nextWoker().add(new ChannelContext(socketChannel));
			} catch (Throwable e) {
				logger.error("", e);
			}

		}

	}

	private NioChannelProcessor nextWoker() {
		return processors[ioIndex.incrementAndGet() % processors.length];
	}
}