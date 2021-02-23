package com.github.leeyazhou.web;

import org.junit.Test;

public class WebXmlParserTest {

	@Test
	public void testParse() {
		WebXmlParser parser = new WebXmlParser("web.xml");

		WebXml webXml = parser.parse();

	}

}
