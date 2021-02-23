package com.github.leeyazhou.cio.handler.http;

import java.util.HashMap;
import java.util.Map;

public class HttpObjectBase {
	protected HttpVersion version;
	protected Map<String, String> headers = new HashMap<>();
	protected String contentType;
	protected int contentLength;

	public HttpObjectBase(HttpVersion version) {
		this.version = version;
	}

	public void addHeader(String name, String value) {
		headers.put(name, value);
	}

	public void addHeader(HttpHeader header) {
		headers.put(header.getName(), header.getValue());
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public HttpVersion getVersion() {
		return version;
	}

	public void setVersion(HttpVersion version) {
		this.version = version;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	
	

}
