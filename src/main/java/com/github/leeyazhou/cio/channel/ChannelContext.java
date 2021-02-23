package com.github.leeyazhou.cio.channel;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.github.leeyazhou.cio.message.Message;

public interface ChannelContext {

	Channel getChannel();

	ChannelHandlerChain channelChain();

	void write(Message response);

	int read(ByteBuffer byteBuffer) throws IOException;

}
