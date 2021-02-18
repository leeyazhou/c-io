package com.github.leeyazhou.cio.channel;

public class ChannelHandlerContext {
	private String name;
	protected ChannelHandler handler;
	protected ChannelHandlerContext next;
	protected ChannelHandlerContext prev;
	protected ChannelContext channelContext;

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

	public ChannelHandlerContext getPrev() {
		return prev;
	}

	public void setPrev(ChannelHandlerContext prev) {
		this.prev = prev;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static void invokeChannelRegistered(ChannelHandlerContext ctx) {
		ctx.invokeChannelRegistered();
	}

	public void invokeChannelRegistered() {
		handler.channelRegistered(this);
	}

	public void fireChannelRegistered() {
		invokeChannelRegistered(next);
	}

	public static void invokeChannelUnregistered(ChannelHandlerContext ctx) {
		ctx.invokeChannelRegistered();
	}

	public void invokeChannelUnregistered() {
		handler.channelUnregistered(this);
	}

	public void fireChannelUnregistered() {
		invokeChannelUnregistered(next);
	}

	public static void invokeChannelRead(ChannelHandlerContext ctx, Object message) {
		ctx.invokeChannelRead(message);
	}

	public void invokeChannelRead(Object message) {
		((ChannelInboundHandler) handler).channelRead(this, message);
	}

	public void fireChannelRead(Object message) {
		invokeChannelRead(next, message);
	}

	public static void invokeChannelClosed(ChannelHandlerContext ctx) {
		ctx.invokeChannelClosed();
	}

	public void invokeChannelClosed() {
		handler.channelClosed(this);
	}

	public void fireChannelClosed() {
		invokeChannelClosed(next);
	}

	
	public static void invokeChannelActive(ChannelHandlerContext ctx) {
		ctx.invokeChannelActive();
	}

	public void invokeChannelActive() {
		handler.channelActive(this);
	}
	
	public void fireChannelActive() {
		next.handler.channelActive(next);
	}

	public void setChannelContext(ChannelContext channelContext) {
		this.channelContext = channelContext;
	}

	public ChannelContext getChannelContext() {
		return channelContext;
	}

}
