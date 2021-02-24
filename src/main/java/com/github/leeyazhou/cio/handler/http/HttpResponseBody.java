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

	public void write(int b) {
		byte[] bb = intToByte(b);
		if (this.body == null) {
			this.body = bb;
		} else {
			byte[] newBody = new byte[this.body.length + 4];
			System.arraycopy(body, 0, newBody, 0, body.length);
			System.arraycopy(bb, 0, newBody, body.length, bb.length);
			this.body = newBody;
		}
	}

	private byte[] intToByte(int val) {
		byte[] data = new byte[4];
		data[0] = (byte) (val & 0xff);
		data[1] = (byte) ((val >> 8) & 0xff);
		data[2] = (byte) ((val >> 16) & 0xff);
		data[3] = (byte) ((val >> 24) & 0xff);
		return data;
	}
}
