package com.github.leeyazhou.cio.channel;

public interface ChannelInboundHandler extends ChannelHandler {

	void channelRead(ChannelHandlerContext context, Object message);
}
