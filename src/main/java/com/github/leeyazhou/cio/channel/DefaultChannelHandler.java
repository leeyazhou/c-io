package com.github.leeyazhou.cio.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultChannelHandler implements ChannelInboundHandler {
	private static final Logger logger = LoggerFactory.getLogger(DefaultChannelHandler.class);

	@Override
	public void channelRegistered(DefaultChannelContext context) {
		logger.info("注册通道：{}", context);
	}

	@Override
	public void channelUnregistered(DefaultChannelContext context) {
		logger.info("取消注册:{}", context);
	}

	@Override
	public void channelRead(DefaultChannelContext context, Object message) {
		logger.info("读取通道:{}", context);

	}

	@Override
	public void channelClosed(DefaultChannelContext context) {
		logger.info("关闭通道:{}", context);

	}

	@Override
	public void channelActive(DefaultChannelContext context) {
		logger.info("active通道:{}", context);
	}

}
