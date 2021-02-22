package com.github.leeyazhou.cio.example;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.cio.channel.ChannelContext;
import com.github.leeyazhou.cio.channel.ChannelHandlerContext;
import com.github.leeyazhou.cio.channel.ChannelInboundHandler;
import com.github.leeyazhou.cio.handler.http.HttpHeader;
import com.github.leeyazhou.cio.handler.http.HttpRequest;
import com.github.leeyazhou.cio.handler.http.HttpResponse;
import com.github.leeyazhou.cio.handler.http.HttpResponseBody;
import com.github.leeyazhou.cio.handler.http.HttpStatus;
import com.github.leeyazhou.cio.handler.http.HttpVersion;
import com.github.leeyazhou.cio.message.Message;
import com.github.leeyazhou.cio.message.MessageBuffer;

public class MessageHandler implements ChannelInboundHandler {
	private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
	private HttpResponse response;

	public MessageHandler() {
		response = new HttpResponse(HttpVersion.HTTP_1_1);
		response.addHeader("Content-Length", "38");
		response.addHeader("Content-Type", "text/html");
		response.setStatus(new HttpStatus(200));
		String respnseBody = "<html><body>Hello World!</body></html>";
		response.setBody(new HttpResponseBody(respnseBody.getBytes()));

	}

	@Override
	public void channelRegistered(ChannelHandlerContext context) {
		context.fireChannelRegistered();
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext context) {
		context.fireChannelUnregistered();
	}

	@Override
	public void channelClosed(ChannelHandlerContext context) {
		context.fireChannelClosed();
	}

	@Override
	public void channelActive(ChannelHandlerContext context) {
		context.fireChannelActive();
	}

	@Override
	public void channelRead(ChannelHandlerContext context, Object msg) {
		Message message = (Message) msg;
		context.fireChannelRead(message);

		HttpRequest httpRequest = (HttpRequest) message.getMetaData();
		logger.info("读取数据channelRead: {}", httpRequest);
		ChannelContext channelContext = context.getChannelContext();
		Message responseMessage = MessageBuffer.DEFAULT.newMessage();
		responseMessage.setChannelId(channelContext.getChannel().getId());
		doWriteMessage(responseMessage, response);

		channelContext.write(responseMessage);
	}

	static final String lineSepator = "\r\n";

	void doWriteMessage(Message responseMessage, HttpResponse response) {
//		responseMessage.writeToMessage(httpResponseBytes);
//		String httpResponse = "HTTP/1.1 200 OK\r\n" + "Content-Length: 38\r\n" + "Content-Type: text/html\r\n" + "\r\n"
//				+ "<html><body>Hello World!</body></html>";

		String statusLine = response.getVersion().string() + " " + response.getStatus().getCode() + " OK" + lineSepator;
		responseMessage.writeToMessage(statusLine.getBytes());
		List<HttpHeader> headers = response.getHeaders();
		if (headers != null && headers.size() > 0) {
			for (HttpHeader header : headers) {
				String headerLine = header.getName() + ": " + header.getValue() + lineSepator;
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
