package com.github.leeyazhou.demo;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServlet extends GenericServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(HelloServlet.class);

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		logger.info("service");
		String data = "Hello world";
		res.getWriter().write(data);
		res.getWriter().flush();
	}

}
