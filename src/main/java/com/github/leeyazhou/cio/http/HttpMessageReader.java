package com.github.leeyazhou.cio.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.github.leeyazhou.cio.ChannelContext;
import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageBuffer;
import com.github.leeyazhou.cio.message.MessageReader;

public class HttpMessageReader implements MessageReader {

	private MessageBuffer messageBuffer = null;

	private List<Message> completeMessages = new ArrayList<Message>();
	private Message nextMessage = null;

	public HttpMessageReader() {
	}

	@Override
	public void init(MessageBuffer readMessageBuffer) {
		this.messageBuffer = readMessageBuffer;
		this.nextMessage = messageBuffer.getMessage();
		this.nextMessage.setMetaData(new HttpHeaders());
	}

	@Override
	public void read(ChannelContext socket, ByteBuffer byteBuffer) throws IOException {
		@SuppressWarnings("unused")
		int bytesRead = socket.read(byteBuffer);
		byteBuffer.flip();

		if (byteBuffer.remaining() == 0) {
			byteBuffer.clear();
			return;
		}

		this.nextMessage.writeToMessage(byteBuffer);

		int endIndex = HttpUtil.parseHttpRequest(this.nextMessage.getSharedArray(), this.nextMessage.getOffset(),
				this.nextMessage.getOffset() + this.nextMessage.getLength(),
				(HttpHeaders) this.nextMessage.getMetaData());
		if (endIndex != -1) {
			Message message = this.messageBuffer.getMessage();
			message.setMetaData(new HttpHeaders());

			message.writePartialMessageToMessage(nextMessage, endIndex);

			completeMessages.add(nextMessage);
			nextMessage = message;
		}
		byteBuffer.clear();
	}

	@Override
	public List<Message> getMessages() {
		return this.completeMessages;
	}

}
