package com.github.leeyazhou.cio.handler.http;

import com.github.leeyazhou.cio.message.Message;

public class HttpResponse extends HttpObjectBase {

	private HttpStatus status;
	private HttpResponseBody body;
	private Message message;

	public HttpResponse() {
		this(HttpVersion.HTTP_1_1);
	}

	public HttpResponse(HttpVersion version) {
		super(version);
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setBody(HttpResponseBody body) {
		this.body = body;
		if (body != null && body.getBody() != null) {
			setContentLength(body.getBody().length);
		}
	}

	@Override
	public int getContentLength() {
		if (body != null && body.getBody() != null) {
			return body.getBody().length;
		}
		return super.getContentLength();
	}

	public HttpResponseBody getBody() {
		return body;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}
}
