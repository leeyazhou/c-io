package com.github.leeyazhou.cio.channel;

@FunctionalInterface
public interface ChannelInitializer {

	void initChannel(Channel channel);
}
