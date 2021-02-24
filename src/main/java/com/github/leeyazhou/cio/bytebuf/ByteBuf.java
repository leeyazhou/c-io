package com.github.leeyazhou.cio.bytebuf;

import java.nio.ByteBuffer;

public interface ByteBuf {

	public static ByteBuf wrap(ByteBuffer byteBuffer) {
		return new DefaultByteBuf(byteBuffer);
	}

	byte read();

	int readInt();

	long readLong();

	short readShort();

	ByteBuf write(byte b);

	ByteBuf writeInt(int b);

	ByteBuf writeLong(long b);

	ByteBuf writeShort(short b);
}
