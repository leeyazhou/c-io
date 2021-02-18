package com.github.leeyazhou.cio.channel;

public class ChannelHandlerChain {

	private ChannelHandlerContext head = new ChannelHandlerContext("defaultHandler", new DefaultChannelHandler());
	private ChannelContext channelContext;

	public ChannelHandlerChain(ChannelContext channelContext) {
		this.channelContext = channelContext;
	}

	public ChannelHandlerChain add(String name, ChannelHandler channelHandler) {
		ChannelHandlerContext handlerContext = newHandlerContext(name, channelHandler);

		head.next = handlerContext;
		head = handlerContext;
		return this;
	}

	public ChannelHandlerContext newHandlerContext(String name, ChannelHandler channelHandler) {
		return new ChannelHandlerContext(name, channelHandler);
	}

	public void fireChannelRegistered(DefaultChannelContext context) {
		head.fireChannelRegistered(context);
	}

	public void fireChannelUnregistered(DefaultChannelContext context) {
		head.fireChannelUnregistered(context);
	}

	public void fireChannelRead(DefaultChannelContext context, Object message) {
		head.fireChannelRead(context, message);
	}

	public void fireChannelClosed(DefaultChannelContext context) {
		head.fireChannelClosed(context);
	}

	public void fireChannelActive(DefaultChannelContext context) {
		head.fireChannelActive(context);
	}

	public ChannelContext getChannelContext() {
		return channelContext;
	}
}
