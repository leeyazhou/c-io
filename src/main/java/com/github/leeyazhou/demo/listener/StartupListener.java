package com.github.leeyazhou.demo.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartupListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(StartupListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("contextInitialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("contextDestroyed");
	}

}
