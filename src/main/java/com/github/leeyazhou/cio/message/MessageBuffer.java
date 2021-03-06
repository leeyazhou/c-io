package com.github.leeyazhou.cio.message;

import com.github.leeyazhou.cio.QueueIntFlip;

/**
 * A shared buffer which can contain many messages inside. A message gets a
 * section of the buffer to use. If the message outgrows the section in size,
 * the message requests a larger section and the message is copied to that
 * larger section. The smaller section is then freed again.
 *
 *
 */
public class MessageBuffer {

	public static int KB = 1024;
	public static int MB = 1024 * KB;

	private static final int CAPACITY_SMALL = 4 * KB;
	private static final int CAPACITY_MEDIUM = 128 * KB;
	private static final int CAPACITY_LARGE = 1024 * KB;

	// package scope (default) - so they can be accessed from unit tests.
	public static final byte[] SMALL_MESSAGE_BUFFER = new byte[1024 * 4 * KB]; // 1024 x 4KB messages = 4MB.
	public static final byte[] MEDINUM_MESSAGE_BUFFER = new byte[128 * 128 * KB]; // 128 x 128KB messages = 16MB.
	public static final byte[] LARGE_MESSAGE_BUFFER = new byte[16 * 1 * MB]; // 16 * 1MB messages = 16MB.

	private QueueIntFlip smallMessageBufferFreeBlocks = new QueueIntFlip(1024); // 1024 free sections
	private QueueIntFlip mediumMessageBufferFreeBlocks = new QueueIntFlip(128); // 128 free sections
	private QueueIntFlip largeMessageBufferFreeBlocks = new QueueIntFlip(16); // 16 free sections

	// todo make all message buffer capacities and block sizes configurable
	// todo calculate free block queue sizes based on capacity and block size of
	// buffers.
	public static final MessageBuffer DEFAULT = new MessageBuffer();

	public MessageBuffer() {
		// add all free sections to all free section queues.
		for (int i = 0; i < SMALL_MESSAGE_BUFFER.length; i += CAPACITY_SMALL) {
			this.smallMessageBufferFreeBlocks.put(i);
		}
		for (int i = 0; i < MEDINUM_MESSAGE_BUFFER.length; i += CAPACITY_MEDIUM) {
			this.mediumMessageBufferFreeBlocks.put(i);
		}
		for (int i = 0; i < LARGE_MESSAGE_BUFFER.length; i += CAPACITY_LARGE) {
			this.largeMessageBufferFreeBlocks.put(i);
		}
	}

	public Message newMessage() {
		int nextFreeSmallBlock = this.smallMessageBufferFreeBlocks.take();

		if (nextFreeSmallBlock == -1)
			return null;

		Message message = new Message(this); // todo get from Message pool - caps memory usage.

		message.setSharedArray(SMALL_MESSAGE_BUFFER);
		message.setCapacity(CAPACITY_SMALL);
		message.setOffset(nextFreeSmallBlock);
		message.setLength(0);

		return message;
	}

	public boolean expandMessage(Message message) {
		if (message.getCapacity() == CAPACITY_SMALL) {
			return moveMessage(message, this.smallMessageBufferFreeBlocks, this.mediumMessageBufferFreeBlocks,
					MEDINUM_MESSAGE_BUFFER, CAPACITY_MEDIUM);
		} else if (message.getCapacity() == CAPACITY_MEDIUM) {
			return moveMessage(message, this.mediumMessageBufferFreeBlocks, this.largeMessageBufferFreeBlocks,
					LARGE_MESSAGE_BUFFER, CAPACITY_LARGE);
		} else {
			return false;
		}
	}

	private boolean moveMessage(Message message, QueueIntFlip srcBlockQueue, QueueIntFlip destBlockQueue, byte[] dest,
			int newCapacity) {
		int nextFreeBlock = destBlockQueue.take();
		if (nextFreeBlock == -1)
			return false;

		System.arraycopy(message.getSharedArray(), message.getOffset(), dest, nextFreeBlock, message.getLength());

		srcBlockQueue.put(message.getOffset()); // free smaller block after copy

		message.setSharedArray(dest);
		message.setOffset(nextFreeBlock);
		message.setCapacity(newCapacity);
		return true;
	}

}
