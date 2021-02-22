package com.github.leeyazhou.cio.handler.http;

import java.util.ArrayList;
import java.util.List;

public class HttpObjectBase {
	protected HttpVersion version;
	protected List<HttpHeader> headers = new ArrayList<>();

	public HttpObjectBase(HttpVersion version) {
		this.version = version;
	}

	public void addHeader(String name, String value) {
		headers.add(new HttpHeader(name, value));
	}

	public void addHeader(HttpHeader header) {
		headers.add(header);
	}

	public void setHeaders(List<HttpHeader> headers) {
		this.headers = headers;
	}

	public List<HttpHeader> getHeaders() {
		return headers;
	}

	public HttpVersion getVersion() {
		return version;
	}

	public void setVersion(HttpVersion version) {
		this.version = version;
	}

}
