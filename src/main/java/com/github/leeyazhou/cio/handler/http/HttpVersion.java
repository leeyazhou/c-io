package com.github.leeyazhou.cio.handler.http;

public class HttpVersion {
	private final String protocolName;
	private final int majorVersion;
	private final int minorVersion;

	/**
	 * HTTP/1.0
	 */
	public static final HttpVersion HTTP_1_0 = new HttpVersion("HTTP", 1, 0);

	/**
	 * HTTP/1.1
	 */
	public static final HttpVersion HTTP_1_1 = new HttpVersion("HTTP", 1, 1);

	public HttpVersion(String protocolName, int majorVersion, int minorVersion) {
		this.protocolName = protocolName;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public String string() {
		return protocolName + "/" + majorVersion + "." + minorVersion;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpVersion{");
		builder.append(protocolName);
		builder.append("/");
		builder.append(majorVersion);
		builder.append(".");
		builder.append(minorVersion);
		builder.append("}");
		return builder.toString();
	}
	
	

}
