package com.github.leeyazhou.cio.util.concurrent;

public class WorkerThread extends Thread {
	private Runnable task;

	public WorkerThread() {
	}

	public void setTask(Runnable task) {
		this.task = task;
	}

	@Override
	public void run() {
		task.run();
	}

}
