package com.github.leeyazhou.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import com.github.leeyazhou.cio.handler.http.HttpRequest;

public class Request implements HttpServletRequest {
	private HttpRequest httpRequest;

	public Request(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	@Override
	public Object getAttribute(String name) {
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		return null;
	}

	@Override
	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

	}

	@Override
	public int getContentLength() {
		return httpRequest.getContentLength();
	}

	@Override
	public long getContentLengthLong() {
		return httpRequest.getContentLength();
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public String getParameter(String name) {
		return null;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return null;
	}

	@Override
	public String[] getParameterValues(String name) {
		return null;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return null;
	}

	@Override
	public String getProtocol() {
		return httpRequest.getVersion().getProtocolName();
	}

	@Override
	public String getScheme() {
		return null;
	}

	@Override
	public String getServerName() {
		return null;
	}

	@Override
	public int getServerPort() {
		return 0;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return null;
	}

	@Override
	public String getRemoteAddr() {
		return null;
	}

	@Override
	public String getRemoteHost() {
		return null;
	}

	@Override
	public void setAttribute(String name, Object o) {

	}

	@Override
	public void removeAttribute(String name) {

	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return null;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return null;
	}

	@Override
	public String getRealPath(String path) {
		return null;
	}

	@Override
	public int getRemotePort() {
		return 0;
	}

	@Override
	public String getLocalName() {
		return null;
	}

	@Override
	public String getLocalAddr() {
		return null;
	}

	@Override
	public int getLocalPort() {
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		return null;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return null;
	}

	@Override
	public AsyncContext startAsync(javax.servlet.ServletRequest servletRequest, ServletResponse servletResponse)
			throws IllegalStateException {
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		return false;
	}

	@Override
	public AsyncContext getAsyncContext() {
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		return null;
	}

	@Override
	public String getAuthType() {
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		return null;
	}

	@Override
	public long getDateHeader(String name) {
		return 0;
	}

	@Override
	public String getHeader(String name) {
		return null;
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return null;
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIntHeader(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserInRole(String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession(boolean create) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession() {
		return null;
	}

	@Override
	public String changeSessionId() {
		return null;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	@Override
	public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		return false;
	}

	@Override
	public void login(String username, String password) throws ServletException {

	}

	@Override
	public void logout() throws ServletException {

	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return null;
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		return null;
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
		return null;
	}

}
