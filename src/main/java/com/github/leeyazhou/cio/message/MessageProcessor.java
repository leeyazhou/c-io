package com.github.leeyazhou.cio.message;

@FunctionalInterface
public interface MessageProcessor {

	Object process(Message message);

}
