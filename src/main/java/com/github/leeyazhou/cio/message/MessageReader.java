package com.github.leeyazhou.cio.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import com.github.leeyazhou.cio.channel.DefaultChannelContext;

public interface MessageReader {

	void init(MessageBuffer readMessageBuffer);

	int read(DefaultChannelContext socket, ByteBuffer byteBuffer) throws IOException;

	List<Message> getMessages();

}
