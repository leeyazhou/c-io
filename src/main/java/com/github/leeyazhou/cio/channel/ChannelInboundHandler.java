package com.github.leeyazhou.cio.channel;

public interface ChannelInboundHandler extends ChannelHandler {

	void channelRead(DefaultChannelContext context, Object message);
}
