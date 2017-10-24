package org.rabbitcontrol.rcp.test.netty;

import org.rabbitcontrol.rcp.transport.RCPTransporter;
import org.rabbitcontrol.rcp.model.RCPPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public interface RCPTransporterNetty extends RCPTransporter {

    void received(final ChannelHandlerContext ctx, final RCPPacket _packet);

    void addChannel(final Channel _channel);

    void removeChannel(final Channel _channel);
}
