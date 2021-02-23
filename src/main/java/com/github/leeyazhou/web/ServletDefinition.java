package com.github.leeyazhou.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

public class ServletDefinition {
	private String description;
	private String servletName;
	private String servletClass;
	private final Map<String, String> parameters = new HashMap<>();
	private Integer loadOnStartup;
	private Boolean enabled;
	private Servlet servlet;

	public String getServletName() {
		return servletName;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	public String getServletClass() {
		return servletClass;
	}

	public void setServletClass(String servletClass) {
		this.servletClass = servletClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLoadOnStartup() {
		return loadOnStartup;
	}

	public void setLoadOnStartup(Integer loadOnStartup) {
		this.loadOnStartup = loadOnStartup;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
	
	public void addParameter(String name, String object) {
		this.parameters.put(name, object);
	}
	
	public void setServlet(Servlet servlet) {
		this.servlet = servlet;
	}
	
	public Servlet getServlet() {
		return servlet;
	}

}
