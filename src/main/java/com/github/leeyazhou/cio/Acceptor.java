package com.github.leeyazhou.cio;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.channel.ChannelInitializer;
import com.github.leeyazhou.cio.channel.DefaultChannelContext;
import com.github.leeyazhou.cio.channel.NioSocketChannel;

public class Acceptor implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(Acceptor.class);
	private final String host;
	private final int tcpPort;
	private boolean running = true;

	private ServerSocketChannel serverSocket = null;
	private NioChannelProcessor[] processors;
	private AtomicInteger ioIndex = new AtomicInteger();
	private ChannelInitializer channelInitializer;

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

		while (running) {
			try {
				SocketChannel socketChannel = this.serverSocket.accept();
				logger.info("Socket accepted: {}", socketChannel);
				DefaultChannelContext channelContext = new DefaultChannelContext(new NioSocketChannel(socketChannel));
				channelContext.init();
				channelContext.setChannelInitializer(channelInitializer);
				nextWoker().addChannel(channelContext);
			} catch (Throwable e) {
				logger.error("", e);
			}

		}

	}

	public void shutdown() {
		this.running = false;
		logger.info("关闭Acceptor");
	}

	private NioChannelProcessor nextWoker() {
		return processors[ioIndex.incrementAndGet() % processors.length];
	}
	
	public void setChannelInitializer(ChannelInitializer channelInitializer) {
		this.channelInitializer = channelInitializer;
	}
}
