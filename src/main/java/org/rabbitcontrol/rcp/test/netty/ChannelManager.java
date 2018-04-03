package org.rabbitcontrol.rcp.test.netty;

import io.netty.channel.Channel;

public interface ChannelManager {

    void addChannel(final Channel _channel);

    void removeChannel(final Channel _channel);
}
