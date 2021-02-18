package com.github.leeyazhou.cio.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NioSocketChannel extends AbstractChannel {

	private static final Logger logger = LoggerFactory.getLogger(NioSocketChannel.class);
	private ChannelHandlerChain channelChain;
	private SocketChannel socketChannel;
	private ChannelContext channelContext;

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
	public SelectionKey register(Selector readSelector, int opRead, DefaultChannelContext channelContext)
			throws ClosedChannelException {
		SelectionKey key = socketChannel.register(readSelector, SelectionKey.OP_READ, channelContext);
		channelChain.fireChannelRegistered(channelContext);
		if (channelContext.getChannel().isActive()) {
			channelChain.fireChannelActive(channelContext);
		}
		return key;
	}

	@Override
	public void configureBlocking(boolean blocking) throws IOException {
		socketChannel.configureBlocking(blocking);
	}

	public boolean isActive() {
		return socketChannel.isOpen() && socketChannel.isConnected();
	}

	public void setChannelContext(ChannelContext channelContext) {
		this.channelContext = channelContext;
	}

	public ChannelContext getChannelContext() {
		return channelContext;
	}
	
	@Override
	public ChannelContext context() {
		return channelContext;
	}

	public void setChannelChain(ChannelHandlerChain channelChain) {
		this.channelChain = channelChain;
	}

	public ChannelHandlerChain getChannelChain() {
		return channelChain;
	}

	@Override
	public void close(DefaultChannelContext context) {
		try {
			socketChannel.close();
		} catch (IOException e) {
			logger.error("", e);
		}
		channelChain.fireChannelClosed(context);
	}
}
