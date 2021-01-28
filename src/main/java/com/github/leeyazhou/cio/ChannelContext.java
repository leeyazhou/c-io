package com.github.leeyazhou.cio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.github.leeyazhou.cio.message.MessageReader;
import com.github.leeyazhou.cio.message.MessageWriter;

public class ChannelContext {

	private long socketId;

	private SocketChannel socketChannel = null;
	private MessageReader messageReader = null;
	private MessageWriter messageWriter = null;

	private boolean endOfStreamReached = false;

	public ChannelContext() {
	}

	public ChannelContext(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public int read(ByteBuffer byteBuffer) throws IOException {
		int bytesRead = 0;
		int totalBytesRead = bytesRead;
		while ((bytesRead = socketChannel.read(byteBuffer)) > 0) {
			totalBytesRead += bytesRead;
		}
		if (bytesRead == -1) {
			this.endOfStreamReached = true;
		}

		return totalBytesRead;
	}

	public int write(ByteBuffer byteBuffer) throws IOException {
		int bytesWritten = this.socketChannel.write(byteBuffer);
		int totalBytesWritten = bytesWritten;

		while (bytesWritten > 0 && byteBuffer.hasRemaining()) {
			bytesWritten = this.socketChannel.write(byteBuffer);
			totalBytesWritten += bytesWritten;
		}

		return totalBytesWritten;
	}

	public long getSocketId() {
		return socketId;
	}

	public void setSocketId(long socketId) {
		this.socketId = socketId;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
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

}
