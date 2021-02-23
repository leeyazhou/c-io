package com.github.leeyazhou.cio.message;

import com.github.leeyazhou.cio.channel.ChannelContext;

public interface MessageReaderFactory {

	MessageReader createMessageReader(ChannelContext channelContext);

}
