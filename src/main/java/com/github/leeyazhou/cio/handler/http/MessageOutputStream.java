package com.github.leeyazhou.cio.handler.http;

import java.io.IOException;
import java.io.OutputStream;

import com.github.leeyazhou.cio.message.Message;

public class MessageOutputStream extends OutputStream {
	private final Message message;

	public MessageOutputStream(Message message) {
		this.message = message;
	}

	@Override
	public void write(int b) throws IOException {
		byte[] data = intToByte(b);
		message.writeToMessage(data);
	}

	private byte[] intToByte(int val) {
		byte[] data = new byte[4];
		data[0] = (byte) (val & 0xff);
		data[1] = (byte) ((val >> 8) & 0xff);
		data[2] = (byte) ((val >> 16) & 0xff);
		data[3] = (byte) ((val >> 24) & 0xff);
		return data;
	}
}
