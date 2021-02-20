package com.github.leeyazhou.cio.handler.http;

import java.util.ArrayList;
import java.util.List;

public class HttpRequest {

	private HttpMethod method;
	private HttpVersion version;
	private List<HttpHeader> headers = new ArrayList<>();
	private HttpBody body;

	private int contentLength = 0;

	private String requestUri;

	public void addHeader(String name, String value) {
		headers.add(new HttpHeader(name, value));
	}

	public void addHeader(HttpHeader header) {
		headers.add(header);
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setVersion(HttpVersion version) {
		this.version = version;
	}

	public HttpVersion getVersion() {
		return version;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public void setBody(HttpBody body) {
		this.body = body;
	}

	public HttpBody getBody() {
		return body;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpRequest [method=");
		builder.append(method);
		builder.append(", requestUri=");
		builder.append(requestUri);
		builder.append(", version=");
		builder.append(version);
		builder.append(", headers=");
		builder.append(headers);
		builder.append(", body=");
		builder.append(body);
		builder.append("]");
		return builder.toString();
	}

}
