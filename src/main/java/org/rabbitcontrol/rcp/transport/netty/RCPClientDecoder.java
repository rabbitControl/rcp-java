package org.rabbitcontrol.rcp.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.rabbitcontrol.rcp.transport.*;

import java.util.List;

public class RCPClientDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final ClientTransporterListener listener;

    public RCPClientDecoder(final ClientTransporterListener _listener) {
        listener = _listener;
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {

        System.out.println("RCPClientDecoder client channel channelInactive");

        super.channelInactive(ctx);
    }

    @Override
    protected void decode(
            final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws
                                                                                        Exception {
        if (listener != null) {

            try {
                byte[] array = new byte[msg.readableBytes()];
                msg.getBytes(0, array);
                listener.received(array);
            }
            catch (IndexOutOfBoundsException _e)
            {
                System.err.println(_e.getMessage());
            }

        }
    }
}
