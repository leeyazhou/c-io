package com.github.leeyazhou.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ApplicationFilterChain implements FilterChain {

	private Filter[] filters;
	private int index;

	public ApplicationFilterChain(Filter[] filters) {
		this.filters = filters;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		if (filters == null || filters.length == 0 || index == filters.length - 1) {
			return;
		}
		filters[index++].doFilter(request, response, this);
	}
}
