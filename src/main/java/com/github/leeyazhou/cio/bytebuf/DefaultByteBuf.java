package com.github.leeyazhou.cio.bytebuf;

import java.nio.ByteBuffer;

public class DefaultByteBuf implements ByteBuf {

	private ByteBuffer buffer;

	public DefaultByteBuf(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public byte read() {
		return buffer.get();
	}

	@Override
	public int readInt() {
		return buffer.getInt();
	}

	@Override
	public long readLong() {
		return buffer.getLong();
	}

	@Override
	public short readShort() {
		return buffer.getShort();
	}

	@Override
	public ByteBuf write(byte b) {
		buffer.put(b);
		return this;
	}

	@Override
	public ByteBuf writeInt(int b) {
		buffer.putInt(b);
		return this;
	}

	@Override
	public ByteBuf writeLong(long b) {
		buffer.putLong(b);
		return this;
	}

	@Override
	public ByteBuf writeShort(short b) {
		buffer.putShort(b);
		return this;
	}

}
