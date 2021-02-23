package com.github.leeyazhou.cio.handler.http;

import com.github.leeyazhou.cio.channel.ChannelContext;
import com.github.leeyazhou.cio.message.MessageReader;
import com.github.leeyazhou.cio.message.MessageReaderFactory;

public class HttpMessageReaderFactory implements MessageReaderFactory {

	public HttpMessageReaderFactory() {
	}

	@Override
	public MessageReader createMessageReader(ChannelContext channelContext) {
		return new HttpMessageReader(channelContext);
	}
}
