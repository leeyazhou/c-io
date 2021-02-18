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

	public MessageWriter() {
	}

	public void enqueue(Message message) {
		this.writeQueue.add(message);
	}

	public void write(DefaultChannelContext context, ByteBuffer byteBuffer) throws IOException {
		if (writeQueue.isEmpty()) {
			logger.info("没有数据输出");
			return;
		}
		for (Message messageInProgress : writeQueue) {
			byteBuffer.put(messageInProgress.getSharedArray(), messageInProgress.getOffset() + this.bytesWritten,
					messageInProgress.getLength() - this.bytesWritten);
			byteBuffer.flip();

			this.bytesWritten += context.write(byteBuffer);
			byteBuffer.clear();
		}

	}

	public boolean isEmpty() {
		return this.writeQueue.isEmpty();
	}

}
