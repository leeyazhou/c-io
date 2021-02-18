package com.github.leeyazhou.cio.channel;

public class ChannelHandlerContext {
	private String name;
	private ChannelHandler handler;
	protected ChannelHandlerContext next;
	protected ChannelHandlerContext prefix;

	public ChannelHandlerContext(String name, ChannelHandler handler) {
		this.name = name;
		this.handler = handler;
	}

	public ChannelHandler getHandler() {
		return handler;
	}

	public void setHandler(ChannelHandler handler) {
		this.handler = handler;
	}

	public ChannelHandlerContext getNext() {
		return next;
	}

	public void setNext(ChannelHandlerContext next) {
		this.next = next;
	}

	public ChannelHandlerContext getPrefix() {
		return prefix;
	}

	public void setPrefix(ChannelHandlerContext prefix) {
		this.prefix = prefix;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void fireChannelRegistered(DefaultChannelContext context) {
		handler.channelRegistered(context);
		if (next != null)
			next.fireChannelRegistered(context);
	}

	public void fireChannelUnregistered(DefaultChannelContext context) {
		handler.channelUnregistered(context);
		if (next != null)
			next.fireChannelUnregistered(context);
	}

	public void fireChannelRead(DefaultChannelContext context, Object message) {
		((ChannelInboundHandler) handler).channelRead(context, message);
		if (next != null)
			next.fireChannelRead(context, message);
	}

	public void fireChannelClosed(DefaultChannelContext context) {
		handler.channelClosed(context);
		if (next != null)
			next.fireChannelClosed(context);
	}

	public void fireChannelActive(DefaultChannelContext context) {
		handler.channelActive(context);
		if (next != null)
			next.fireChannelActive(context);
	}

}
