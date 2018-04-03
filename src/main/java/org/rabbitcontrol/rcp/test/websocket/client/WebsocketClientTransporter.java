package org.rabbitcontrol.rcp.test.websocket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.rabbitcontrol.rcp.model.Packet;
import org.rabbitcontrol.rcp.transport.*;

import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketClientTransporter implements ClientTransporter {

    EventLoopGroup group = new NioEventLoopGroup();

    private Channel ch;

    private URI uri;

    private final Bootstrap bootstrap;

    private ClientTransporterListener listener;

    private WebSocketClientHandler websocketHandler;

    //    TOUISerializerFactory serializerFactory = new TOUISerializerFactory();

    public WebsocketClientTransporter() {

        bootstrap = new Bootstrap();
    }


    @Override
    public void connect(final String host, final int port) {

        try {
            uri = new URI("ws://" + host + ":" + port + "/");

            // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
            // If you change it to V00, ping is not supported and remember to change
            // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
            websocketHandler = new WebSocketClientHandler(WebSocketClientHandshakerFactory.newHandshaker(uri,
                                                                                                         WebSocketVersion.V13,
                                                                                                         null,
                                                                                                         true,
                                                                                                         new DefaultHttpHeaders()));


            bootstrap.group(group)
                     .channel(NioSocketChannel.class)
                     .handler(new WebsocketClientInitializer(null, uri, websocketHandler, listener));

            try {

                ch = bootstrap.connect(uri.getHost(), uri.getPort()).sync().channel();
                websocketHandler.handshakeFuture().sync();
            }
            catch (Exception _e) {
                _e.printStackTrace();
                ch = null;
            }

            //return ch != null;
        }
        catch (URISyntaxException _e) {
            _e.printStackTrace();
        }
    }

    @Override
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
    public boolean isConnected() {

        return ch.isOpen();
    }

    @Override
    public void send(final byte[] _packet) {

        if (ch.isOpen() && ch.isWritable()) {
            ch.writeAndFlush(Unpooled.wrappedBuffer(_packet));
        }
        else {
            System.err.println("channel not open or not writeable");
        }
    }

    @Override
    public void addListener(final ClientTransporterListener _listener) {

        listener = _listener;
    }

    @Override
    public void removeListener(final ClientTransporterListener _listener) {

        if ((listener != null) && listener.equals(_listener)) {
            listener = null;
        }
    }
}
