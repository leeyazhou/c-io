package com.github.leeyazhou.cio.channel;

public interface ChannelOutboundHandler {

	void write(ChannelContext context, Object message);
	
}
