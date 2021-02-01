package com.github.leeyazhou.cio.channel;

public interface ChannelHandler {

	void channelRegistered(ChannelHandlerContext context);

	void channelUnregistered(ChannelHandlerContext context);

	void channelRead(ChannelHandlerContext context);

	void channelClosed(ChannelHandlerContext context);

	void channelActive(ChannelHandlerContext channelContext);

}
