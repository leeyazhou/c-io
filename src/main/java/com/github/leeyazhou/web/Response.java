package com.github.leeyazhou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.handler.http.HttpResponse;

public class Response implements HttpServletResponse {
	static final Logger logger = LoggerFactory.getLogger(Response.class);
	private Request request;
	private HttpResponse httpResponse;

	public Response(Request request) {
		this.request = request;
	}

	public void setHttpResponse(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
	}

	@Override
	public String getCharacterEncoding() {
		return httpResponse.getCharacterEncoding();
	}

	@Override
	public String getContentType() {
		return httpResponse.getContentType();
	}

	private ServletOutputStream outputStream;

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (outputStream == null) {
			this.outputStream = new ServletOutputStream() {

				@Override
				public void write(int b) throws IOException {
					httpResponse.getBody().write(b);
				}

				@Override
				public void setWriteListener(WriteListener writeListener) {

				}

				@Override
				public boolean isReady() {
					return true;
				}
			};
		}
		return outputStream;
	}

	private PrintWriter writer;

	@Override
	public PrintWriter getWriter() throws IOException {
		if (writer == null) {
			this.writer = new PrintWriter(getOutputStream());
		}
		return writer;
	}

	@Override
	public void setCharacterEncoding(String charset) {
		httpResponse.setCharacterEncoding(charset);
	}

	@Override
	public void setContentLength(int len) {
		httpResponse.setContentLength(len);
	}

	@Override
	public void setContentLengthLong(long len) {
		httpResponse.setContentLength((int) len);
	}

	@Override
	public void setContentType(String type) {
		httpResponse.setContentType(type);
	}

	@Override
	public void setBufferSize(int size) {

	}

	@Override
	public int getBufferSize() {
		return 0;
	}

	@Override
	public void flushBuffer() throws IOException {

	}

	@Override
	public void resetBuffer() {

	}

	@Override
	public boolean isCommitted() {
		return false;
	}

	@Override
	public void reset() {

	}

	@Override
	public void setLocale(Locale loc) {

	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public void addCookie(Cookie cookie) {

	}

	@Override
	public boolean containsHeader(String name) {
		return false;
	}

	@Override
	public String encodeURL(String url) {
		return null;
	}

	@Override
	public String encodeRedirectURL(String url) {
		return null;
	}

	@Override
	public String encodeUrl(String url) {
		return null;
	}

	@Override
	public String encodeRedirectUrl(String url) {
		return null;
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {

	}

	@Override
	public void sendError(int sc) throws IOException {

	}

	@Override
	public void sendRedirect(String location) throws IOException {

	}

	@Override
	public void setDateHeader(String name, long date) {

	}

	@Override
	public void addDateHeader(String name, long date) {

	}

	@Override
	public void setHeader(String name, String value) {

	}

	@Override
	public void addHeader(String name, String value) {

	}

	@Override
	public void setIntHeader(String name, int value) {

	}

	@Override
	public void addIntHeader(String name, int value) {

	}

	@Override
	public void setStatus(int sc) {

	}

	@Override
	public void setStatus(int sc, String sm) {

	}

	@Override
	public int getStatus() {
		return 0;
	}

	@Override
	public String getHeader(String name) {
		return null;
	}

	@Override
	public Collection<String> getHeaders(String name) {
		return null;
	}

	@Override
	public Collection<String> getHeaderNames() {
		return null;
	}

	public Request getRequest() {
		return request;
	}

}
