package com.github.leeyazhou.cio.util.concurrent;

public abstract class WorkerTask implements Runnable {

	@Override
	public void run() {
		worker();
	}

	abstract void worker();
}
