package com.github.leeyazhou.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

public class FilterDefinition {
	private String description;
	private String displayName;
	private transient Filter filter;
	private String filterClass;
	private String filterName;
	private final Map<String, String> parameters = new HashMap<>();

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public String getFilterClass() {
		return filterClass;
	}

	public void setFilterClass(String filterClass) {
		this.filterClass = filterClass;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

}
