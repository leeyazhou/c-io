package com.github.leeyazhou.cio.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public interface Channel {

	int read(ByteBuffer byteBuffer) throws IOException;

	int write(ByteBuffer byteBuffer) throws IOException;

	SelectionKey register(Selector readSelector, int opRead, ChannelHandlerContext chanelContext) throws ClosedChannelException;

	void configureBlocking(boolean blocking) throws IOException;
	
	boolean isActive();
}
