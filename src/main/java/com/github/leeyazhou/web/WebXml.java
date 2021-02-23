package com.github.leeyazhou.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class WebXml {

	private final Set<String> welcomeFiles = new LinkedHashSet<>();
	private String displayName;
	private final Map<String, String> contextParams = new HashMap<>();
	private final Map<String, FilterDefinition> filters = new LinkedHashMap<>();
	private final Map<String, String> filterMappings = new LinkedHashMap<>();
	private final Set<String> listeners = new LinkedHashSet<>();
	private final Map<String, ServletDefinition> servlets = new HashMap<>();
	private final Map<String, String> servletMappings = new HashMap<>();
	private final Set<String> servletMappingNames = new HashSet<>();

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Set<String> getWelcomeFiles() {
		return welcomeFiles;
	}

	public Map<String, String> getContextParams() {
		return contextParams;
	}

	public Map<String, FilterDefinition> getFilters() {
		return filters;
	}

	public Set<String> getListeners() {
		return listeners;
	}

	public Map<String, ServletDefinition> getServlets() {
		return servlets;
	}

	public Map<String, String> getServletMappings() {
		return servletMappings;
	}

	public Set<String> getServletMappingNames() {
		return servletMappingNames;
	}

	public void addWelcomeFile(String wel) {
		this.welcomeFiles.add(wel);
	}

	public void addContextParam(String nodename, String nodevalue) {
		contextParams.put(nodename, nodevalue);
	}

	public void addServlet(String name, ServletDefinition servlet) {
		this.servlets.put(name, servlet);
	}

	public void addServletMapping(String name, String pattern) {
		this.servletMappings.put(name, pattern);
	}

	public void addFilter(String name, FilterDefinition filter) {
		this.filters.put(name, filter);
	}

	public void addFilterMapping(String name, String pattern) {
		this.filterMappings.put(name, pattern);
	}

	public void addListener(String filterClass) {
		this.listeners.add(filterClass);
	}
}
