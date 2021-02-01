package com.github.leeyazhou.cio.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NioSocketChannel implements Channel {

	private SocketChannel socketChannel;

	public NioSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	@Override
	public int read(ByteBuffer byteBuffer) throws IOException {
		return socketChannel.read(byteBuffer);
	}

	@Override
	public int write(ByteBuffer byteBuffer) throws IOException {
		return socketChannel.write(byteBuffer);
	}

	@Override
	public SelectionKey register(Selector readSelector, int opRead, ChannelHandlerContext chanelContext)
			throws ClosedChannelException {
		return socketChannel.register(readSelector, SelectionKey.OP_READ, chanelContext);
	}

	@Override
	public void configureBlocking(boolean blocking) throws IOException {
		socketChannel.configureBlocking(blocking);
	}
	
	public boolean isActive() {
		return socketChannel.isOpen() && socketChannel.isConnected();
	}
}
