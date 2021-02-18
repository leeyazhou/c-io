package com.github.leeyazhou.cio.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import com.github.leeyazhou.cio.message.Message;

public interface Channel {

	int read(ByteBuffer byteBuffer) throws IOException;

	int write(ByteBuffer byteBuffer) throws IOException;

	SelectionKey register(Selector readSelector, int opRead, DefaultChannelContext chanelContext)
			throws ClosedChannelException;

	void configureBlocking(boolean blocking) throws IOException;

	boolean isActive();

	void close(DefaultChannelContext context);

	long getId();

	ChannelContext context();

	void write(Message response);
}
