package com.github.leeyazhou.cio.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageReader;
import com.github.leeyazhou.cio.message.MessageWriter;

public class DefaultChannelContext implements ChannelContext {
	static final Logger logger = LoggerFactory.getLogger(DefaultChannelContext.class);
	private NioSocketChannel channel = null;
	private MessageReader messageReader = null;
	private MessageWriter messageWriter = null;

	private boolean endOfStreamReached = false;
	private ChannelHandlerChain handlerChain;
	private ChannelInitializer channelInitializer;
	private Queue<Message> outboundMessageQueue;

	public DefaultChannelContext(NioSocketChannel channel) {
		this.channel = channel;
		this.handlerChain = new ChannelHandlerChain(this);
		this.messageWriter = new MessageWriter();
	}

	public void init() {
		this.channel.setChannelContext(this);
		this.channel.setChannelChain(handlerChain);
		this.channelInitializer.initChannel(channel);
	}

	public int read(ByteBuffer byteBuffer) throws IOException {
		int bytesRead = 0;
		int totalBytesRead = bytesRead;
		while ((bytesRead = channel.read(byteBuffer)) > 0) {
			totalBytesRead += bytesRead;
		}
		if (bytesRead == -1) {
			this.endOfStreamReached = true;
		}

		return totalBytesRead;
	}

	public int write(ByteBuffer byteBuffer) throws IOException {
		return channel.write(byteBuffer);
	}

	@Override
	public void write(Message response) {
		outboundMessageQueue.add(response);
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(NioSocketChannel socketChannel) {
		this.channel = socketChannel;
	}

	public MessageReader getMessageReader() {
		return messageReader;
	}

	public void setMessageReader(MessageReader messageReader) {
		this.messageReader = messageReader;
	}

	public MessageWriter getMessageWriter() {
		return messageWriter;
	}

	public void setMessageWriter(MessageWriter messageWriter) {
		this.messageWriter = messageWriter;
	}

	public boolean isEndOfStreamReached() {
		return endOfStreamReached;
	}

	public void setEndOfStreamReached(boolean endOfStreamReached) {
		this.endOfStreamReached = endOfStreamReached;
	}

	public void close() {
		channel.close(this);
	}

	public void setChannelInitializer(ChannelInitializer channelInitializer) {
		this.channelInitializer = channelInitializer;
	}

	@Override
	public ChannelHandlerChain channelChain() {
		return handlerChain;
	}

	public void setOutboundMessageQueue(Queue<Message> outboundMessageQueue) {
		this.outboundMessageQueue = outboundMessageQueue;
	}
}
