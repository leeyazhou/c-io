package com.github.leeyazhou.cio.handler.http;

public class HttpResponse {
	private HttpRequest httpHeaders;

	public void setHttpHeaders(HttpRequest httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	public HttpRequest getHttpHeaders() {
		return httpHeaders;
	}
}
