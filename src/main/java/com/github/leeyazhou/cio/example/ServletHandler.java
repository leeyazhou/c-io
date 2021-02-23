package com.github.leeyazhou.cio.example;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.channel.ChannelContext;
import com.github.leeyazhou.cio.channel.ChannelHandlerContext;
import com.github.leeyazhou.cio.channel.ChannelInboundHandler;
import com.github.leeyazhou.cio.handler.http.HttpRequest;
import com.github.leeyazhou.cio.handler.http.HttpResponse;
import com.github.leeyazhou.cio.handler.http.HttpResponseBody;
import com.github.leeyazhou.cio.handler.http.HttpStatus;
import com.github.leeyazhou.cio.handler.http.HttpVersion;
import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageBuffer;
import com.github.leeyazhou.web.WebLoader;

public class ServletHandler implements ChannelInboundHandler {

	private static final Logger logger = LoggerFactory.getLogger(ServletHandler.class);
	private HttpResponse response;
	private WebLoader webLoader;

	public ServletHandler() {
		response = new HttpResponse(HttpVersion.HTTP_1_1);
		response.addHeader("Content-Length", "38");
		response.addHeader("Content-Type", "text/html");
		response.setStatus(HttpStatus.SC_OK);
		String respnseBody = "<html><body>Hello World!</body></html>";
		response.setBody(new HttpResponseBody(respnseBody.getBytes()));
		this.webLoader = new WebLoader();
		this.webLoader.init();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext context) {

	}

	@Override
	public void channelUnregistered(ChannelHandlerContext context) {

	}

	@Override
	public void channelClosed(ChannelHandlerContext context) {

	}

	@Override
	public void channelActive(ChannelHandlerContext context) {

	}

	@Override
	public void channelRead(ChannelHandlerContext context, Object message) {
		logger.info("servlet收到消息：{}", message);
		HttpRequest httpRequest = (HttpRequest) message;
		logger.info("读取数据channelRead: {}", httpRequest);
		ChannelContext channelContext = context.getChannelContext();
		Message responseMessage = MessageBuffer.DEFAULT.newMessage();
		responseMessage.setChannelId(channelContext.getChannel().getId());

		doWriteMessage(responseMessage, response);

		webLoader.execute(httpRequest, response);

		channelContext.write(responseMessage);

	}

	static final String lineSepator = "\r\n";

	void doWriteMessage(Message responseMessage, HttpResponse response) {
//		responseMessage.writeToMessage(httpResponseBytes);
//		String httpResponse = "HTTP/1.1 200 OK\r\n" + "Content-Length: 38\r\n" + "Content-Type: text/html\r\n" + "\r\n"
//				+ "<html><body>Hello World!</body></html>";
		String statusLine = response.getVersion().string() + " " + response.getStatus() + lineSepator;

		response.addHeader("Content-Length", String.valueOf(response.getContentLength()));

		responseMessage.writeToMessage(statusLine.getBytes());
		Map<String, String> headers = response.getHeaders();
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				String headerLine = entry.getKey() + ": " + entry.getValue() + lineSepator;
				responseMessage.writeToMessage(headerLine.getBytes());
			}
			responseMessage.writeToMessage(lineSepator.getBytes());
		} else {
			responseMessage.writeToMessage(lineSepator.getBytes());
			responseMessage.writeToMessage(lineSepator.getBytes());
		}
		responseMessage.writeToMessage(response.getBody().getBody());
	}

}
