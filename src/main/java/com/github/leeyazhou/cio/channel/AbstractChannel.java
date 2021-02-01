package com.github.leeyazhou.cio.channel;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractChannel implements Channel {

	private long id;
	private static AtomicLong nextSocketId = new AtomicLong(16 * 1024);

	public AbstractChannel() {
		this.id = nextSocketId.getAndIncrement();
	}

	@Override
	public long getId() {
		return id;
	}
}
