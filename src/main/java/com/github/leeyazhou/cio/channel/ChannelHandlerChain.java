package com.github.leeyazhou.cio.channel;

public class ChannelHandlerChain {

	private ChannelHandler channelHandler = new DefaultChannelHandler();

	public void fireChannelRegistered(ChannelHandlerContext context) {
		channelHandler.channelRegistered(context);
	}

	public void fireChannelUnregistered(ChannelHandlerContext context) {
		channelHandler.channelUnregistered(context);
	}

	public void fireChannelRead(ChannelHandlerContext context) {
		channelHandler.channelRead(context);
	}

	public void fireChannelClosed(ChannelHandlerContext context) {
		channelHandler.channelClosed(context);
	}

	public void fireChannelActive(ChannelHandlerContext channelContext) {
		channelHandler.channelActive(channelContext);
	}
}
