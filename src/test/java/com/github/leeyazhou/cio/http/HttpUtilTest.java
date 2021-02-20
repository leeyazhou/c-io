package com.github.leeyazhou.cio.http;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.github.leeyazhou.cio.handler.http.HttpRequest;
import com.github.leeyazhou.cio.handler.http.HttpMethod;
import com.github.leeyazhou.cio.handler.http.HttpUtil;

public class HttpUtilTest {

	@Test
	public void testResolveHttpMethod() throws UnsupportedEncodingException {
		assertHttpMethod("GET / HTTP/1.1\r\n", HttpMethod.GET);
		assertHttpMethod("POST / HTTP/1.1\r\n", HttpMethod.POST);
		assertHttpMethod("PUT / HTTP/1.1\r\n", HttpMethod.PUT);
		assertHttpMethod("HEAD / HTTP/1.1\r\n", HttpMethod.HEAD);
		assertHttpMethod("DELETE / HTTP/1.1\r\n", HttpMethod.DELETE);
	}

	private void assertHttpMethod(String httpRequest, HttpMethod httpMethod) throws UnsupportedEncodingException {
		byte[] source = httpRequest.getBytes("UTF-8");
		HttpRequest httpHeaders = new HttpRequest();

		HttpUtil.resolveHttpMethod(source, 0, httpHeaders);
		assertEquals(httpMethod, httpHeaders.getMethod());
	}

	@Test
	public void testParseHttpRequest() throws UnsupportedEncodingException {
		String httpRequestStr = "GET / HTTP/1.1\r\n\r\n";

		byte[] source = httpRequestStr.getBytes("UTF-8");
		HttpRequest httpRequest = new HttpRequest();

		HttpUtil.parseHttpRequest(source, 0, source.length, httpRequest);

		assertEquals(0, httpRequest.getContentLength());

		httpRequestStr = "GET / HTTP/1.1\r\n" + "Content-Length: 5\r\n" + "\r\n1234";
		source = httpRequestStr.getBytes("UTF-8");

		assertEquals(-1, HttpUtil.parseHttpRequest(source, 0, source.length, httpRequest));
		assertEquals(5, httpRequest.getContentLength());

		httpRequestStr = "GET / HTTP/1.1\r\n" + "Content-Length: 5\r\n" + "\r\n12345";
		source = httpRequestStr.getBytes("UTF-8");

		assertEquals(42, HttpUtil.parseHttpRequest(source, 0, source.length, httpRequest));
		assertEquals(5, httpRequest.getContentLength());

		httpRequestStr = "GET / HTTP/1.1\r\n" + "Content-Length: 5\r\n" + "\r\n12345" + "GET / HTTP/1.1\r\n"
				+ "Content-Length: 5\r\n" + "\r\n12345";

		source = httpRequestStr.getBytes("UTF-8");

		assertEquals(42, HttpUtil.parseHttpRequest(source, 0, source.length, httpRequest));
		assertEquals(5, httpRequest.getContentLength());
//		assertEquals(37, httpHeaders.bodyStartIndex);
//		assertEquals(42, httpHeaders.bodyEndIndex);
	}

}
