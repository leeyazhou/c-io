package com.github.leeyazhou.cio.handler.http;

public class HttpHeader {

	private String name;
	private String value;

	public HttpHeader(String header) {
		String temp[] = header.split(": ");
		this.name = temp[0];
		this.value = temp[1];
	}

	public HttpHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpHeader{");
		builder.append(name);
		builder.append(": ");
		builder.append(value);
		builder.append("}");
		return builder.toString();
	}
	
	

}
