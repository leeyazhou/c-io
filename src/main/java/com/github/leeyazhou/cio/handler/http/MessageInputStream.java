package com.github.leeyazhou.cio.handler.http;

import java.io.IOException;
import java.io.InputStream;

import com.github.leeyazhou.cio.message.Message;

public class MessageInputStream extends InputStream {
	private final Message message;

	public MessageInputStream(Message message) {
		this.message = message;
	}

	@Override
	public int read() throws IOException {
		
		return 0;
	}

}
