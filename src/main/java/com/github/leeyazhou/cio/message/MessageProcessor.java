package com.github.leeyazhou.cio.message;

import com.github.leeyazhou.cio.WriteProxy;

@FunctionalInterface
public interface MessageProcessor {

	void process(Message message, WriteProxy writeProxy);

}
