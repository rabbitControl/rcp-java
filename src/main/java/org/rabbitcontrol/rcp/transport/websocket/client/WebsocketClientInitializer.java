package org.rabbitcontrol.rcp.transport.websocket.client;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.rabbitcontrol.rcp.transport.ClientTransporterListener;
import org.rabbitcontrol.rcp.transport.netty.BinaryWebSocketFrameEncoder;

import java.net.URI;
import java.util.List;

/**
 * Created by inx on 28/10/17.
 */
public class WebsocketClientInitializer extends ChannelInitializer<SocketChannel> {

    private final URI uri;

    private ChannelHandler handler;

    private final ClientTransporterListener listener;

    public WebsocketClientInitializer(final URI _uri, final
                                      ChannelHandler _handler,
                                      ClientTransporterListener _listener) {

        uri = _uri;
        handler = _handler;
        listener = _listener;
    }

    public void setHandler(ChannelHandler _handler)
    {
        handler = _handler;
    }

    @Override
    protected void initChannel(final SocketChannel ch) throws Exception
    {
        final ChannelPipeline pipeline = ch.pipeline();

        // in
        if (uri.getScheme().startsWith("wss"))
        {
            final SslContext sslCtx = SslContextBuilder.forClient().build();
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
                    final List<Object> out)
            {
                if (listener != null)
                {
                    byte[] array = new byte[msg.content().readableBytes()];
                    msg.content().getBytes(0, array);

                    listener.received(array);
                }
            }
        });

        // out
        pipeline.addLast(new BinaryWebSocketFrameEncoder());
        pipeline.addLast(new ByteArrayEncoder());
    }
}
