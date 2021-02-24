package com.github.leeyazhou.cio.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.channel.DefaultChannelContext;

public class MessageWriter {

	private static final Logger logger = LoggerFactory.getLogger(MessageWriter.class);
	private List<Message> writeQueue = new ArrayList<>();
	private int bytesWritten = 0;
	private DefaultChannelContext channelContext;

	public MessageWriter(DefaultChannelContext channelContext) {
		this.channelContext = channelContext;
	}

	public void enqueue(Message message) {
		this.writeQueue.add(message);
	}

	public void write(ByteBuffer byteBuffer) throws IOException {
		logger.info("输出数据, queueSize : {}", writeQueue.size());
		if (writeQueue.isEmpty()) {
			logger.info("没有数据输出");
			return;
		}
		for (Message message : writeQueue) {
			byteBuffer.put(message.getSharedArray(), message.getOffset() + this.bytesWritten,
					message.getLength() - this.bytesWritten);
//			byteBuffer.put(message.getSharedArray(), message.getOffset(), message.getLength());
			byteBuffer.flip();

			this.bytesWritten += channelContext.write(byteBuffer);
			byteBuffer.clear();
		}

	}

	public boolean isEmpty() {
		return this.writeQueue.isEmpty();
	}

	public int getBytesWritten() {
		return bytesWritten;
	}
}
