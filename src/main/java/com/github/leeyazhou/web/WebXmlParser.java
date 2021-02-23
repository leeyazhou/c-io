package com.github.leeyazhou.web;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WebXmlParser {

	private static final Logger logger = LoggerFactory.getLogger(WebXmlParser.class);
	private String xmlLocation;
	private WebXml webXml;

	public WebXmlParser(String xmlLocation) {
		this.xmlLocation = xmlLocation;
	}

	public WebXml parse() {
		webXml = new WebXml();
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder db = builderFactory.newDocumentBuilder();
			doParse(db);
		} catch (Exception e) {
			logger.error("file : " + xmlLocation, e);
		}

		return webXml;
	}

	private void doParse(DocumentBuilder db) throws Exception {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(xmlLocation);
		Document document = db.parse(inputStream);
		parseDisplayName(document.getElementsByTagName("display-name"));
		parseWelcomeFiles(document.getElementsByTagName("welcome-file-list"));
		parseContextParams(document.getElementsByTagName("context-param"));
		parseServlet(document);
		parseFilter(document);
		parseListener(document.getElementsByTagName("listener"));
	}

	private void parseListener(NodeList listenerNodes) {
		for (int i = 0; i < listenerNodes.getLength(); i++) {
			Node node = listenerNodes.item(i);
			Map<String, Object> result = toMap(node, null);
			String listenerClass = (String) result.get("listener-class");
			logger.info("add listener : {}", listenerClass);
			webXml.addListener(listenerClass);
		}
	}

	private void parseFilter(Document document) {
		NodeList filterNodes = document.getElementsByTagName("filter");
		for (int i = 0; i < filterNodes.getLength(); i++) {
			Node node = filterNodes.item(i);
			Map<String, Object> result = toMap(node, null);
			String name = (String) result.get("filter-name");
			String filterClass = (String) result.get("filter-class");
			FilterDefinition filter = new FilterDefinition();
			filter.setFilterName(name);
			filter.setFilterClass(filterClass);
			webXml.addFilter(name, filter);
			logger.info("add filter, {}:{}", name, filterClass);
		}
		NodeList filterMappingNodes = document.getElementsByTagName("filter-mapping");
		for (int i = 0; i < filterMappingNodes.getLength(); i++) {
			Node node = filterMappingNodes.item(i);
			Map<String, Object> result = toMap(node, null);
			String name = (String) result.get("filter-name");
			String pattern = (String) result.get("url-pattern");
			webXml.addFilterMapping(name, pattern);
			logger.info("add filter mapping, {}:{}", name, pattern);
		}
	}

	private void parseServlet(Document document) {
		NodeList servletNodes = document.getElementsByTagName("servlet");
		for (int i = 0; i < servletNodes.getLength(); i++) {
			Node node = servletNodes.item(i);
			Map<String, Object> result = toMap(node, "init-param");
			ServletDefinition definition = new ServletDefinition();
			String servletName = (String) result.get("servlet-name");
			definition.setDescription((String) result.get("description"));
			definition.setServletName(servletName);
			definition.setServletClass((String) result.get("servlet-class"));
			@SuppressWarnings("unchecked")
			Map<String, Object> initParams = (Map<String, Object>) result.get("init-param");
			if (initParams != null) {
				definition.addParameter((String) initParams.get("param-name"), (String) initParams.get("param-value"));
			}
			webXml.addServlet(servletName, definition);
			logger.info("add servlet, {}:{}", servletName, definition.getServletClass());
		}

		NodeList servletMappingNodes = document.getElementsByTagName("servlet-mapping");
		for (int i = 0; i < servletMappingNodes.getLength(); i++) {
			Node node = servletMappingNodes.item(i);
			Map<String, Object> result = toMap(node, null);
			String name = (String) result.get("servlet-name");
			String pattern = (String) result.get("url-pattern");
			webXml.addServletMapping(name, pattern);
			
			logger.info("add servlet mapping, {}:{}", name, pattern);
		}
	}

	private Map<String, Object> toMap(Node node, String param) {
		Map<String, Object> result = new HashMap<>();
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node item = nodeList.item(i);
			if (item.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			String key = item.getNodeName();
			Object value = item.getTextContent();
			if (param != null && param.equals(key)) {
				value = toMap(item, null);
			}
//			logger.info("参数，{}:{}", key, value);
			result.put(key, value);
		}
		return result;
	}

	private void parseContextParams(NodeList contextParamNode) {
		if (contextParamNode == null || contextParamNode.getLength() == 0) {
			return;
		}

		for (int i = 0; i < contextParamNode.getLength(); i++) {
			Node node = contextParamNode.item(i);
			Map<String, Object> result = toMap(node, null);
			String nodename = (String) result.get("param-name");
			String nodevalue = (String) result.get("param-value");
			logger.info("add context param, {} : {}", nodename, nodevalue);
			webXml.addContextParam(nodename, nodevalue);
		}
	}

	private void parseWelcomeFiles(NodeList welcomeNodes) {
		if (welcomeNodes == null || welcomeNodes.getLength() == 0) {
			return;
		}
		NodeList welcomeNodeList = welcomeNodes.item(0).getChildNodes();
		for (int i = 0; i < welcomeNodeList.getLength(); i++) {
			Node no = welcomeNodeList.item(i);
			if (no.getNodeType() == Node.ELEMENT_NODE) {
				String wel = no.getTextContent();
				webXml.addWelcomeFile(wel);
				logger.info("add welcome file :{}", wel);
			}
		}

	}

	private void parseDisplayName(NodeList displayName) {
		if (displayName == null || displayName.getLength() == 0) {
			return;
		}
		Node node = displayName.item(0);
		String name = node.getTextContent();
		logger.info("displayName : {}", name);
		webXml.setDisplayName(name);
	}
}
