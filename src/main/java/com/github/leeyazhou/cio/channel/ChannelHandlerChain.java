package com.github.leeyazhou.cio.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelHandlerChain {
	private static final Logger logger = LoggerFactory.getLogger(ChannelHandlerChain.class);
	private ChannelHandlerContext head;
	private ChannelHandlerContext tail;
	private DefaultChannelContext channelContext;

	public ChannelHandlerChain(DefaultChannelContext channelContext) {
		this.channelContext = channelContext;
		this.head = newHandlerContext("head", new HeadContext());
		this.tail = newHandlerContext("tail", new TailContext());

		head.next = tail;
		tail.prev = head;
	}

	public ChannelHandlerChain add(String name, ChannelHandler channelHandler) {
		ChannelHandlerContext newCtx = newHandlerContext(name, channelHandler);

		ChannelHandlerContext prev = tail.prev;
		newCtx.prev = prev;
		newCtx.next = tail;
		prev.next = newCtx;
		tail.prev = newCtx;
		logger.info("添加handler, {} : {}", name, channelHandler);
		return this;
	}

	public ChannelHandlerContext newHandlerContext(String name, ChannelHandler channelHandler) {
		ChannelHandlerContext handlerContext = new ChannelHandlerContext(name, channelHandler);
		handlerContext.setChannelContext(channelContext);
		return handlerContext;
	}

	public void fireChannelRegistered() {
		ChannelHandlerContext.invokeChannelRegistered(head);
	}

	public void fireChannelUnregistered() {
		ChannelHandlerContext.invokeChannelUnregistered(head);
	}

	public void fireChannelRead(Object message) {
		ChannelHandlerContext.invokeChannelRead(head, message);
	}

	public void fireChannelClosed() {
		ChannelHandlerContext.invokeChannelClosed(head);
	}

	public void fireChannelActive() {
		ChannelHandlerContext.invokeChannelActive(head);
	}

	public ChannelContext getChannelContext() {
		return channelContext;
	}

	private class HeadContext implements ChannelInboundHandler {

		@Override
		public void channelRegistered(ChannelHandlerContext context) {
			context.fireChannelRegistered();
			logger.info("HeadContext -> channelRegistered");
		}

		@Override
		public void channelUnregistered(ChannelHandlerContext context) {
			context.fireChannelUnregistered();
			logger.info("HeadContext -> channelUnregistered");
		}

		@Override
		public void channelClosed(ChannelHandlerContext context) {
			context.fireChannelClosed();
			logger.info("HeadContext -> channelClosed");
		}

		@Override
		public void channelActive(ChannelHandlerContext context) {
			context.fireChannelActive();
			logger.info("HeadContext -> channelActive");
		}

		@Override
		public void channelRead(ChannelHandlerContext context, Object message) {
			context.fireChannelRead(message);
			logger.info("HeadContext -> channelRead");
		}

	}

	private class TailContext implements ChannelInboundHandler {

		@Override
		public void channelRegistered(ChannelHandlerContext context) {
			logger.info("TailContext -> channelRegistered");
		}

		@Override
		public void channelUnregistered(ChannelHandlerContext context) {
			logger.info("TailContext -> channelUnregistered");
		}

		@Override
		public void channelClosed(ChannelHandlerContext context) {
			logger.info("TailContext -> channelClosed");
		}

		@Override
		public void channelActive(ChannelHandlerContext channelContext) {
			logger.info("TailContext -> channelActive");
		}

		@Override
		public void channelRead(ChannelHandlerContext context, Object message) {
			logger.info("TailContext -> channelRead");
		}

	}
}
