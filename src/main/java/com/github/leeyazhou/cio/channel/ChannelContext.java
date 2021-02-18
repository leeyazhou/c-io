package com.github.leeyazhou.cio.channel;

import com.github.leeyazhou.cio.message.Message;

public interface ChannelContext {

	Channel getChannel();

	ChannelHandlerChain channelChain();

	void write(Message response);

}
