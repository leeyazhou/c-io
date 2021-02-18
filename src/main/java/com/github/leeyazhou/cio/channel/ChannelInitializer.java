package com.github.leeyazhou.cio.channel;

@FunctionalInterface
public interface ChannelInitializer {

	/**
	 * init channel
	 * 
	 * @param channel
	 */
	void initChannel(Channel channel);
}
