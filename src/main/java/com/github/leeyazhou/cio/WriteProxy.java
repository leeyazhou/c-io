package com.github.leeyazhou.cio;

import java.util.Queue;

import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageBuffer;

public class WriteProxy {

	private MessageBuffer messageBuffer = null;
	private Queue<Message> writeQueue = null;

	public WriteProxy(MessageBuffer messageBuffer, Queue<Message> writeQueue) {
		this.messageBuffer = messageBuffer;
		this.writeQueue = writeQueue;
	}

	public Message getMessage() {
		return this.messageBuffer.getMessage();
	}

	public boolean enqueue(Message message) {
		return this.writeQueue.offer(message);
	}

}
