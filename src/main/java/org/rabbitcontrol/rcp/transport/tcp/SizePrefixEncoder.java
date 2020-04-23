package org.rabbitcontrol.rcp.transport.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class SizePrefixEncoder extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(
            final ChannelHandlerContext ctx, final ByteBuf msg, final ByteBuf out) throws
                                                                                   Exception {
        out.writeInt(msg.readableBytes());
        out.writeBytes(msg);
    }
}
