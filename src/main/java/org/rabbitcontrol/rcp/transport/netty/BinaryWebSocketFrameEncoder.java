package org.rabbitcontrol.rcp.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * Created by inx on 08/04/17.
 */
public class BinaryWebSocketFrameEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(
            final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws
                                                                                        Exception {
        msg.retain();
        out.add(new BinaryWebSocketFrame(msg));
    }
}
