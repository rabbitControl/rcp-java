package org.rabbitcontrol.rcp.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.rabbitcontrol.rcp.transport.ServerTransporter;
import org.rabbitcontrol.rcp.transport.ServerTransporterListener;

import java.util.List;

public class RCPServerDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final ServerTransporter transporter;

    private final ServerTransporterListener listener;

    private final ChannelManager channelManager;

    public RCPServerDecoder(final ServerTransporter _transporter,
                            final ServerTransporterListener _listener,
                            final ChannelManager _channelManager) {
        transporter = _transporter;
        listener = _listener;
        channelManager = _channelManager;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {


        System.out.println("tcp server channelActive...");

        if (channelManager != null) {
            channelManager.addChannel(ctx.channel());
        }

        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {

        System.out.println("tcp server channelInactive...");

        if (channelManager != null) {
            channelManager.removeChannel(ctx.channel());
        }

        super.channelInactive(ctx);
    }


    @Override
    protected void decode(
            final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws
                                                                                        Exception {
        if (listener != null) {

            byte[] array = new byte[msg.readableBytes()];
            msg.getBytes(0, array);

            listener.received(array, transporter, ctx.channel());
        }
    }
}
