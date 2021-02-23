package com.github.leeyazhou.cio.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public interface MessageReader {

	void init(MessageBuffer readMessageBuffer);

	int read(ByteBuffer byteBuffer) throws IOException;

	List<Message> getMessages();

}
