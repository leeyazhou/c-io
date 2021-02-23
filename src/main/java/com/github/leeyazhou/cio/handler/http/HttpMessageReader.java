package com.github.leeyazhou.cio.handler.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.channel.ChannelContext;
import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageBuffer;
import com.github.leeyazhou.cio.message.MessageReader;

public class HttpMessageReader implements MessageReader {
	private static final Logger logger = LoggerFactory.getLogger(HttpMessageReader.class);
	private MessageBuffer messageBuffer = null;

	private List<Message> completeMessages = new ArrayList<Message>();
	private Message nextMessage = null;
	private ChannelContext channelContext;

	public HttpMessageReader(ChannelContext channelContext) {
		this.channelContext = channelContext;
	}

	@Override
	public void init(MessageBuffer messageBuffer) {
		this.messageBuffer = messageBuffer;
		this.nextMessage = messageBuffer.newMessage();
		this.nextMessage.setMetaData(new HttpRequest());
	}

	@Override
	public int read(ByteBuffer byteBuffer) throws IOException {
		int bytesRead = channelContext.read(byteBuffer);
		logger.info("读取字节数:{}", bytesRead);
		byteBuffer.flip();

		if (byteBuffer.remaining() == 0) {
			byteBuffer.clear();
			return bytesRead;
		}

		nextMessage.writeToMessage(byteBuffer);
		int endIndex = HttpUtil.parseHttpRequest(nextMessage.getSharedArray(), nextMessage.getOffset(),
				nextMessage.getOffset() + nextMessage.getLength(), (HttpRequest) this.nextMessage.getMetaData());
		logger.info("解析http结果：{}", endIndex);
		if (endIndex != -1) {
			Message message = messageBuffer.newMessage();
			message.setMetaData(new HttpRequest());

			message.writePartialMessageToMessage(nextMessage, endIndex);

			completeMessages.add(nextMessage);
			nextMessage = message;
		}
		byteBuffer.clear();
		return bytesRead;
	}

	@Override
	public List<Message> getMessages() {
		return this.completeMessages;
	}

}
