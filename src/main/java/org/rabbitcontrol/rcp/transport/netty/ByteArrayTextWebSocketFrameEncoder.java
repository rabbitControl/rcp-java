package org.rabbitcontrol.rcp.transport.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * Created by inx on 08/04/17.
 */
public class ByteArrayTextWebSocketFrameEncoder extends MessageToMessageEncoder<byte[]> {


    @Override
    protected void encode(
            final ChannelHandlerContext ctx,
            final byte[] msg,
            final List<Object> out) throws Exception {

        out.add(new TextWebSocketFrame(Unpooled.wrappedBuffer(msg)));
    }
}
