package org.rabbitcontrol.rcp.transport.websocket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.rabbitcontrol.rcp.transport.ServerTransporter;
import org.rabbitcontrol.rcp.transport.ServerTransporterListener;
import org.rabbitcontrol.rcp.transport.netty.ChannelManager;

import java.util.List;

public class RCPWebSocketServerDecoder extends MessageToMessageDecoder<WebSocketFrame> {

    private final ServerTransporterListener listener;

    private final ServerTransporter transporter;

    private final ChannelManager channelManager;

    public RCPWebSocketServerDecoder(
            final ServerTransporterListener _listener,
            final ServerTransporter _transporter,
            final ChannelManager _channelManager) {

        listener = _listener;
        transporter = _transporter;
        channelManager = _channelManager;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {

        if (channelManager != null) {
            channelManager.addChannel(ctx.channel());
        }

        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {

        if (channelManager != null) {
            channelManager.removeChannel(ctx.channel());
        }

        super.channelInactive(ctx);
    }

    @Override
    protected void decode(
            final ChannelHandlerContext ctx,
            final WebSocketFrame msg,
            final List<Object> out) throws Exception {

        //out.add(msg.content().retain());

        if (listener != null) {

            byte[] array = new byte[msg.content().readableBytes()];
            msg.content().getBytes(0, array);

            listener.received(array, transporter, ctx.channel());
        }
    }
}
