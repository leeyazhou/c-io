package com.github.leeyazhou.cio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.nio.ByteBuffer;

import org.junit.Test;

import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageBuffer;

public class MessageTest {

	@Test
	public void testWriteToMessage() {
		MessageBuffer messageBuffer = new MessageBuffer();

		Message message = messageBuffer.getMessage();
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);

		fill(byteBuffer, 4096);

		int written = message.writeToMessage(byteBuffer);
		assertEquals(4096, written);
		assertEquals(4096, message.length);
		assertSame(MessageBuffer.SMALL_MESSAGE_BUFFER, message.sharedArray);

		fill(byteBuffer, 124 * 1024);
		written = message.writeToMessage(byteBuffer);
		assertEquals(124 * 1024, written);
		assertEquals(128 * 1024, message.length);
		assertSame(MessageBuffer.MEDINUM_MESSAGE_BUFFER, message.sharedArray);

		fill(byteBuffer, (1024 - 128) * 1024);
		written = message.writeToMessage(byteBuffer);
		assertEquals(896 * 1024, written);
		assertEquals(1024 * 1024, message.length);
		assertSame(MessageBuffer.LARGE_MESSAGE_BUFFER, message.sharedArray);

		fill(byteBuffer, 1);
		written = message.writeToMessage(byteBuffer);
		assertEquals(-1, written);

	}

	private void fill(ByteBuffer byteBuffer, int length) {
		byteBuffer.clear();
		for (int i = 0; i < length; i++) {
			byteBuffer.put((byte) (i % 128));
		}
		byteBuffer.flip();
	}
}
