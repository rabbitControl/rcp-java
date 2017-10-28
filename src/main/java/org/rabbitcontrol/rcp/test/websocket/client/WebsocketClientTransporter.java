package org.rabbitcontrol.rcp.test.websocket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.rabbitcontrol.rcp.model.RCPPacket;
import org.rabbitcontrol.rcp.test.netty.RCPTransporterNetty;
import org.rabbitcontrol.rcp.transport.RCPTransporterListener;

import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketClientTransporter implements RCPTransporterNetty {

    EventLoopGroup group = new NioEventLoopGroup();

    private Channel ch;

    private final URI uri;

    private final Bootstrap bootstrap;

    private RCPTransporterListener listener;

    private final WebSocketClientHandler websocketHandler;

    //    TOUISerializerFactory serializerFactory = new TOUISerializerFactory();

    public WebsocketClientTransporter(final String host, final int port) throws
                                                                         URISyntaxException,
                                                                         InterruptedException {

        uri = new URI("ws://" + host + ":" + port + "/");

        // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
        // If you change it to V00, ping is not supported and remember to change
        // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
        websocketHandler
                = new WebSocketClientHandler(WebSocketClientHandshakerFactory.newHandshaker(uri,
                                                                                            WebSocketVersion.V13,
                                                                                            null,
                                                                                            true,
                                                                                            new DefaultHttpHeaders()));

        bootstrap = new Bootstrap();
        bootstrap.group(group)
                 .channel(NioSocketChannel.class)
                 .handler(new WebsocketClientInitializer(null,
                                                         uri,
                                                         websocketHandler,
                                                         this));
    }

    public boolean connect() {

        try {

            ch = bootstrap.connect(uri.getHost(), uri.getPort()).sync().channel();
            websocketHandler.handshakeFuture().sync();
        }
        catch (Exception _e) {
            _e.printStackTrace();
            ch = null;
        }

        return ch != null;
    }

    public void disconnect() {

        if ((ch != null) && ch.isOpen()) {
            try {
                ch.close().sync();
            }
            catch (InterruptedException _e) {
                _e.printStackTrace();
            }
        }

    }

    @Override
    public void received(final RCPPacket _packet) {

        if (listener != null) {
            listener.received(_packet);
        }
    }

    @Override
    public void send(final RCPPacket _packet) {

        if (ch.isOpen() && ch.isWritable()) {
            ch.writeAndFlush(_packet);
        }
        else {
            System.err.println("channel not open or not writeable");
        }
    }

    @Override
    public void setListener(final RCPTransporterListener _listener) {

        listener = _listener;
    }

    @Override
    public void received(
            final ChannelHandlerContext ctx, final RCPPacket _packet) {

    }

    @Override
    public void addChannel(final Channel _channel) {
        // nop
    }

    @Override
    public void removeChannel(final Channel _channel) {
        // nop
    }
}
