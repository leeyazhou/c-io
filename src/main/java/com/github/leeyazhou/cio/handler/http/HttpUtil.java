package com.github.leeyazhou.cio.handler.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {
	static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	private static final byte[] GET = "GET".getBytes();
	private static final byte[] POST = new byte[] { 'P', 'O', 'S', 'T' };
	private static final byte[] PUT = new byte[] { 'P', 'U', 'T' };
	private static final byte[] HEAD = new byte[] { 'H', 'E', 'A', 'D' };
	private static final byte[] DELETE = new byte[] { 'D', 'E', 'L', 'E', 'T', 'E' };

	public static int parseHttpRequest(byte[] src, int startIndex, int endIndex, HttpRequest httpRequest) {
		// parse HTTP request line
		int endOfFirstLine = findNextLineBreak(src, startIndex, endIndex);
		if (endOfFirstLine == -1)
			return -1;
		logger.info("总数据：{}", new String(src, startIndex, endIndex));
		String head = new String(src, startIndex, endOfFirstLine - startIndex - 1);
		parseHead(head, httpRequest);

		// parse HTTP headers
		int prevEndOfHeader = endOfFirstLine + 1;
		int endOfHeader = findNextLineBreak(src, prevEndOfHeader, endIndex);
		int contentLength = 0;
		while (endOfHeader != -1 && endOfHeader != prevEndOfHeader + 1) { // prevEndOfHeader + 1 = end of previous
																			// header + 2 (+2 = CR + LF)

			String header = new String(src, prevEndOfHeader, endOfHeader - prevEndOfHeader - 1);
			HttpHeader httpHeader = new HttpHeader(header);
			httpRequest.addHeader(httpHeader);

			if (httpHeader.getName().equals("Content-Length")) {
				contentLength = Integer.parseInt(httpHeader.getValue());
				httpRequest.setContentLength(contentLength);
			}

			prevEndOfHeader = endOfHeader + 1;
			endOfHeader = findNextLineBreak(src, prevEndOfHeader, endIndex);
		}

		if (endOfHeader == -1) {
			return -1;
		}

		// check that byte array contains full HTTP message.
		int bodyStartIndex = endOfHeader + 1;
		int bodyEndIndex = bodyStartIndex + contentLength;

		if (bodyEndIndex <= endIndex) {
			// byte array contains a full HTTP request

			byte[] bodyBytes = new byte[contentLength];
			System.arraycopy(src, bodyStartIndex, bodyBytes, 0, contentLength);
			HttpRequestBody body = new HttpRequestBody(bodyBytes);
			httpRequest.setBody(body);

			return bodyEndIndex;
		}

		return -1;
	}

	private static void parseHead(String head, HttpRequest request) {
//		POST /user/current HTTP/1.1
		String[] heads = head.split(" ");
		request.setMethod(HttpMethod.valueOf(heads[0]));
		request.setRequestUri(heads[1]);
		if (heads[2].equals(HttpVersion.HTTP_1_1.string())) {
			request.setVersion(HttpVersion.HTTP_1_1);
		} else {
			request.setVersion(HttpVersion.HTTP_1_0);
		}
	}

	public static int findNext(byte[] src, int startIndex, int endIndex, byte value) {
		for (int index = startIndex; index < endIndex; index++) {
			if (src[index] == value)
				return index;
		}
		return -1;
	}

	public static int findNextLineBreak(byte[] src, int startIndex, int endIndex) {
		for (int index = startIndex; index < endIndex; index++) {
			if (src[index] == '\n') {
				if (src[index - 1] == '\r') {
					return index;
				}
			}
		}
		return -1;
	}

	public static void resolveHttpMethod(byte[] src, int startIndex, HttpRequest httpRequest) {
		if (matches(src, startIndex, GET)) {
			httpRequest.setMethod(HttpMethod.GET);
			return;
		}
		if (matches(src, startIndex, POST)) {
			httpRequest.setMethod(HttpMethod.POST);
			return;
		}
		if (matches(src, startIndex, PUT)) {
			httpRequest.setMethod(HttpMethod.PUT);
			return;
		}
		if (matches(src, startIndex, HEAD)) {
			httpRequest.setMethod(HttpMethod.HEAD);
			return;
		}
		if (matches(src, startIndex, DELETE)) {
			httpRequest.setMethod(HttpMethod.DELETE);
			return;
		}
	}

	public static boolean matches(byte[] src, int offset, byte[] value) {
		for (int i = offset, n = 0; n < value.length; i++, n++) {
			if (src[i] != value[n])
				return false;
		}
		return true;
	}
}
