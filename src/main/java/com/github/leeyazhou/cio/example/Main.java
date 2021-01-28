package com.github.leeyazhou.cio.example;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.ServerConfig;
import com.github.leeyazhou.cio.TcpServer;
import com.github.leeyazhou.cio.http.HttpMessageReaderFactory;
import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageProcessor;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws IOException {

		String httpResponse = "HTTP/1.1 200 OK\r\n" + "Content-Length: 38\r\n" + "Content-Type: text/html\r\n" + "\r\n"
				+ "<html><body>Hello World!</body></html>";

		byte[] httpResponseBytes = httpResponse.getBytes("UTF-8");

		MessageProcessor messageProcessor = (request, writeProxy) -> {
			logger.info("Message Received from socket: " + request.getChannelId());

			Message response = writeProxy.getMessage();
			response.setChannelId(request.getChannelId());
			response.writeToMessage(httpResponseBytes);

			writeProxy.enqueue(response);
		};

		ServerConfig serverConfig = new ServerConfig();
		serverConfig.setPort(9999);
		serverConfig.setMaxSocketSize(2048);
		TcpServer server = new TcpServer(serverConfig, new HttpMessageReaderFactory(), messageProcessor);

		server.start();

	}

}
