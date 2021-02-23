package com.github.leeyazhou.web;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.handler.http.HttpRequest;
import com.github.leeyazhou.cio.handler.http.HttpResponse;

public class WebLoader implements Lifecycle {
	private static final Logger logger = LoggerFactory.getLogger(WebLoader.class);
	private WebXml webXml;
	private WebXmlParser webXmlParser;
	private Set<ServletContextListener> listeners = new LinkedHashSet<>();

	public WebLoader() {
		this.webXmlParser = new WebXmlParser("web.xml");
		this.webXml = webXmlParser.parse();
	}

	@Override
	public void init() {
		logger.info("初始化WebLoader");

		try {
			doInit();
		} catch (Exception e) {
			logger.error("初始化异常", e);
		}

	}

	public void doInit() throws Exception {
		Set<String> listeners = webXml.getListeners();
		for (String listenerStr : listeners) {
			Class<?> listener = Class.forName(listenerStr);
			ServletContextListener contextListener = (ServletContextListener) listener.newInstance();
			this.listeners.add(contextListener);
			contextListener.contextInitialized(null);
		}

		Map<String, FilterDefinition> filters = webXml.getFilters();
		for (Map.Entry<String, FilterDefinition> entry : filters.entrySet()) {
			Class<Filter> filterClazz = (Class<Filter>) Class.forName(entry.getValue().getFilterClass());
			Filter filter = filterClazz.newInstance();
			entry.getValue().setFilter(filter);
			filter.init(null);
		}

		for (Map.Entry<String, ServletDefinition> entry : webXml.getServlets().entrySet()) {
			Class<Servlet> servletClazz = (Class<Servlet>) Class.forName(entry.getValue().getServletClass());
			Servlet servlet = servletClazz.newInstance();
			entry.getValue().setServlet(servlet);
			servlet.init(null);
		}

	}

	@Override
	public void start() {

	}

	public void execute(HttpRequest httpRequest, HttpResponse httpResponse) {
		Servlet servlet = findServlet(httpRequest.getRequestUri());
		logger.info("查询到servlet : {}", servlet);

		Request request = new Request(httpRequest);
		Response response = new Response(request);
		response.setHttpResponse(httpResponse);
		
		FilterChain filterChain = new ApplicationFilterChain(findFilters());
		try {
			filterChain.doFilter(request, response);
			servlet.service(request, response);
		} catch (Exception e) {
			logger.error("", e);
		}

	}

	private Filter[] findFilters() {
		List<Filter> filters = new ArrayList<>();
		for (Map.Entry<String, FilterDefinition> entry : webXml.getFilters().entrySet()) {
			Filter filter = entry.getValue().getFilter();
			filters.add(filter);
		}
		return filters.toArray(new Filter[filters.size()]);
	}

	private Servlet findServlet(String uri) {
		String servletName = null;
		for (Map.Entry<String, String> entry : webXml.getServletMappings().entrySet()) {
			Pattern pa = Pattern.compile("\\*\\.do");
			if (pa.matcher(uri) != null) {
				servletName = entry.getKey();
				break;
			}
		}
		if (servletName == null) {
			return null;
		}
		return webXml.getServlets().get(servletName).getServlet();
	}

	@Override
	public void destory() {
		doDestory();
	}

	private void doDestory() {
		for (ServletContextListener listener : listeners) {
			listener.contextDestroyed(null);
		}

		for (Map.Entry<String, FilterDefinition> entry : webXml.getFilters().entrySet()) {
			entry.getValue().getFilter().destroy();
		}

		for (Map.Entry<String, ServletDefinition> entry : webXml.getServlets().entrySet()) {
			entry.getValue().getServlet().destroy();
		}
	}

}
