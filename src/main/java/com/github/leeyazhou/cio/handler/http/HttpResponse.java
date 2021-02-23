package com.github.leeyazhou.cio.handler.http;

public class HttpResponse extends HttpObjectBase {

	private HttpStatus status;
	private HttpResponseBody body;

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

	public HttpResponseBody getBody() {
		return body;
	}
}
