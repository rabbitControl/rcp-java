package org.rabbitcontrol.rcp.test.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * Created by inx on 08/04/17.
 */
public class StringTextWebSocketFrameEncoder extends MessageToMessageEncoder<String> {

    @Override
    protected void encode(
            final ChannelHandlerContext ctx, final String msg, final List<Object> out) throws
                                                                                       Exception {
        out.add(new TextWebSocketFrame(msg));
    }
}
