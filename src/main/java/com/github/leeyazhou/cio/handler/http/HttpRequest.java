package com.github.leeyazhou.cio.handler.http;

import com.github.leeyazhou.cio.message.Message;

public class HttpRequest extends HttpObjectBase {

	private Message message;

	public HttpRequest() {
		this(HttpVersion.HTTP_1_1);
	}

	public HttpRequest(HttpVersion version) {
		super(version);
	}

	private HttpMethod method;
	private HttpRequestBody body;

	private String requestUri;

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public HttpMethod getMethod() {
		return method;
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

	public void setBody(HttpRequestBody body) {
		this.body = body;
	}

	public HttpRequestBody getBody() {
		return body;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Message getMessage() {
		return message;
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
