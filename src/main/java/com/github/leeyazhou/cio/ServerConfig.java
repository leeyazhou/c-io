package com.github.leeyazhou.cio;

public class ServerConfig {
	private String host;
	private int port;
	private int maxSocketSize = 2048;
	private int ioThread = Runtime.getRuntime().availableProcessors();

	public void setMaxSocketSize(int maxSocketSize) {
		this.maxSocketSize = maxSocketSize;
	}

	public int getMaxSocketSize() {
		return maxSocketSize;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setIoThread(int ioThread) {
		this.ioThread = ioThread;
	}

	public int getIoThread() {
		return ioThread;
	}

}
