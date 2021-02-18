package org.rabbitcontrol.rcp.transport.tcp.client;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.rabbitcontrol.rcp.transport.ClientTransporterListener;
import org.rabbitcontrol.rcp.transport.netty.*;
import org.rabbitcontrol.rcp.transport.tcp.SizePrefixDecoder;
import org.rabbitcontrol.rcp.transport.tcp.SizePrefixEncoder;

public class TcpClientInitializer extends ChannelInitializer<SocketChannel> {

    private final ClientTransporterListener listener;

    public TcpClientInitializer(ClientTransporterListener _listener) {

        listener = _listener;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {

        if (listener != null)
        {
            listener.connected();
        }

        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {

        if (listener != null)
        {
            listener.disconnected();
        }

        super.channelInactive(ctx);
    }

    @Override
    protected void initChannel(final SocketChannel ch) throws Exception {

        final ChannelPipeline pipeline = ch.pipeline();

        // in
        pipeline.addLast(new SizePrefixDecoder(),
                         new RCPClientDecoder(listener));

        // out
        pipeline.addLast(new SizePrefixEncoder());
        pipeline.addLast(new ByteArrayEncoder());
    }
}
