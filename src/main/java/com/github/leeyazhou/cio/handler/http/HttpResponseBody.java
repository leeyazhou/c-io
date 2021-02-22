package com.github.leeyazhou.cio.handler.http;

public class HttpResponseBody {

	private byte[] body;

	public HttpResponseBody(byte[] body) {
		this.body = body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public byte[] getBody() {
		return body;
	}
	
	public String string() {
		return new String(body);
	}
}
