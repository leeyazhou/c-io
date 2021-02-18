package com.github.leeyazhou.cio.example;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.channel.ChannelContext;
import com.github.leeyazhou.cio.channel.ChannelHandlerContext;
import com.github.leeyazhou.cio.channel.ChannelInboundHandler;
import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageBuffer;

public class MessageHandler implements ChannelInboundHandler {
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
	public void channelRegistered(ChannelHandlerContext context) {
		context.fireChannelRegistered();
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext context) {
		context.fireChannelUnregistered();
	}

	@Override
	public void channelClosed(ChannelHandlerContext context) {
		context.fireChannelClosed();
	}

	@Override
	public void channelActive(ChannelHandlerContext context) {
		context.fireChannelActive();
	}

	@Override
	public void channelRead(ChannelHandlerContext context, Object message) {
		context.fireChannelRead(message);
		logger.info("channelRead");
		ChannelContext channelContext = context.getChannelContext();
		Message response = MessageBuffer.DEFAULT.newMessage();
		response.setChannelId(channelContext.getChannel().getId());
		response.writeToMessage(httpResponseBytes);
		channelContext.write(response);
	}

}
