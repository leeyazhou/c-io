package com.github.leeyazhou.cio.example;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.NioTcpServer;
import com.github.leeyazhou.cio.ServerConfig;
import com.github.leeyazhou.cio.handler.http.HttpMessageReaderFactory;

public class ServerMain {
	static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

	public static void main(String[] args) throws IOException {

		MessageHandler messageProcessor = new MessageHandler();

		ServerConfig serverConfig = new ServerConfig();
		serverConfig.setIoThread(1);
		serverConfig.setPort(9999);
		serverConfig.setMaxSocketSize(2048);
		NioTcpServer server = new NioTcpServer(serverConfig, new HttpMessageReaderFactory());
		server.setChannelInitializer(channel -> {
			logger.info("初始化通道");
			channel.context().channelChain().add("http", messageProcessor);
		});

		server.start();
	}

}
