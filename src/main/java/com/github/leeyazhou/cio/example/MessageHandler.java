package com.github.leeyazhou.cio.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.channel.ChannelHandlerContext;
import com.github.leeyazhou.cio.channel.ChannelInboundHandler;
import com.github.leeyazhou.cio.message.Message;

public class MessageHandler implements ChannelInboundHandler {
	static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

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
	public void channelRead(ChannelHandlerContext context, Object msg) {
		Message message = (Message) msg;
		context.fireChannelRead(message.getMetaData());
	}

}
