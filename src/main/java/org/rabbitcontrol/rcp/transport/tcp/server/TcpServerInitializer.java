package org.rabbitcontrol.rcp.transport.tcp.server;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.rabbitcontrol.rcp.transport.ServerTransporter;
import org.rabbitcontrol.rcp.transport.ServerTransporterListener;
import org.rabbitcontrol.rcp.transport.netty.*;
import org.rabbitcontrol.rcp.transport.tcp.SizePrefixDecoder;
import org.rabbitcontrol.rcp.transport.tcp.SizePrefixEncoder;

public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final ServerTransporter transporter;

    private final ServerTransporterListener listener;

    private final ChannelManager channelManager;

    public TcpServerInitializer(final ServerTransporter _transporter,
                                final ServerTransporterListener _listener,
                                final ChannelManager _channelManager) {

        transporter = _transporter;
        listener = _listener;
        channelManager = _channelManager;
    }

    @Override
    public void initChannel(final SocketChannel ch) throws Exception {

        final ChannelPipeline pipeline = ch.pipeline();

        // in
        pipeline.addLast(new SizePrefixDecoder(),
                         new RCPServerDecoder(transporter, listener, channelManager));

        // out
        pipeline.addLast(new SizePrefixEncoder());
        pipeline.addLast(new ByteArrayEncoder());
    }

}
