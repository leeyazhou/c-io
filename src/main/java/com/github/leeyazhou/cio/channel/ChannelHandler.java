package com.github.leeyazhou.cio.channel;

public interface ChannelHandler {

	void channelRegistered(DefaultChannelContext context);

	void channelUnregistered(DefaultChannelContext context);

	void channelClosed(DefaultChannelContext context);

	void channelActive(DefaultChannelContext channelContext);

}
