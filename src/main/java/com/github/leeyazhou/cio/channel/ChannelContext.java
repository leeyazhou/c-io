package com.github.leeyazhou.cio.channel;

public interface ChannelContext {

	Channel getChannel();

	ChannelHandlerChain channelChain();

}
