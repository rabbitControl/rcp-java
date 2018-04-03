package org.rabbitcontrol.rcp.test.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.rabbitcontrol.rcp.test.netty.*;

public class TCPServerInitializer extends ChannelInitializer<SocketChannel> {

    private final ChannelManager server;

    public TCPServerInitializer(final ChannelManager _server) {

        server = _server;
    }

    @Override
    public void initChannel(final SocketChannel ch) throws Exception {

        final ChannelPipeline pipeline = ch.pipeline();

        // TODO.... no JSON here..
        // TODO: we need a streaming decoder here
        pipeline.addLast(new JsonObjectDecoder()); // default 1024 x 1024
        pipeline.addLast(new RCPPacketDecoder());
        pipeline.addLast(new RCPPacketHandler(server));

        pipeline.addLast(new RCPPacketEncoder());
    }

}
