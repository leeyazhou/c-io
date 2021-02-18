package com.github.leeyazhou.cio.example;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.WriteProxy;
import com.github.leeyazhou.cio.channel.ChannelHandler;
import com.github.leeyazhou.cio.channel.DefaultChannelContext;
import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageProcessor;

public class MessageHandler implements MessageProcessor, ChannelHandler {
	private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
	private byte[] httpResponseBytes;

	public MessageHandler() {
		String httpResponse = "HTTP/1.1 200 OK\r\n" + "Content-Length: 38\r\n" + "Content-Type: text/html\r\n" + "\r\n"
				+ "<html><body>Hello World!</body></html>";

		try {
			this.httpResponseBytes = httpResponse.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		}
	}

	@Override
	public void process(Message request, WriteProxy writeProxy) {
		logger.info("请求信息：{}", request);
		logger.info("Message Received from socket: " + request.getChannelId());

		Message response = writeProxy.getMessage();
		response.setChannelId(request.getChannelId());
		response.writeToMessage(httpResponseBytes);

		writeProxy.enqueue(response);
	}

	@Override
	public void channelRegistered(DefaultChannelContext context) {
		
	}

	@Override
	public void channelUnregistered(DefaultChannelContext context) {
		
	}

	@Override
	public void channelClosed(DefaultChannelContext context) {
		
	}

	@Override
	public void channelActive(DefaultChannelContext channelContext) {
		
	}

}
