package org.rabbitcontrol.rcp.transport.websocket.client;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import org.rabbitcontrol.rcp.transport.netty.*;
import org.rabbitcontrol.rcp.transport.ClientTransporterListener;

import java.net.URI;
import java.util.List;

/**
 * Created by inx on 28/10/17.
 */
public class WebsocketClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    private final URI uri;

    private final WebSocketClientHandler handler;

    private final ClientTransporterListener listener;

    public WebsocketClientInitializer(final SslContext _sslCtx, final URI _uri, final
                                      WebSocketClientHandler _handler,
                                      ClientTransporterListener _listener) {

        sslCtx = _sslCtx;
        uri = _uri;
        handler = _handler;
        listener = _listener;
    }

    @Override
    protected void initChannel(final SocketChannel ch) throws Exception {

        final ChannelPipeline pipeline = ch.pipeline();

        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc(), uri.getHost(), uri.getPort()));
        }

        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast(WebSocketClientCompressionHandler.INSTANCE);

        pipeline.addLast(handler);

        pipeline.addLast(new MessageToMessageDecoder<WebSocketFrame>() {

            @Override
            protected void decode(
                    final ChannelHandlerContext ctx,
                    final WebSocketFrame msg,
                    final List<Object> out) throws Exception {

                //out.add(msg.content().retain());

                if (listener != null) {

                    byte[] array = new byte[msg.content().readableBytes()];
                    msg.content().getBytes(0, array);

                    listener.received(array);
                }
            }
        });

//        pipeline.addLast(new RCPPacketDecoder());
//        pipeline.addLast(new RCPPacketHandler(listener));



        pipeline.addLast(new BinaryWebSocketFrameEncoder());
        pipeline.addLast(new StringTextWebSocketFrameEncoder());
        pipeline.addLast(new ByteArrayTextWebSocketFrameEncoder());
        pipeline.addLast(new RCPPacketEncoder());

    }
}
