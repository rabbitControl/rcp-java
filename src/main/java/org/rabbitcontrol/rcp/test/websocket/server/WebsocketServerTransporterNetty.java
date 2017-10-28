package org.rabbitcontrol.rcp.test.websocket.server;

import org.rabbitcontrol.rcp.transport.RCPTransporterListener;
import org.rabbitcontrol.rcp.model.RCPPacket;
import org.rabbitcontrol.rcp.test.netty.RCPTransporterNetty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.GlobalEventExecutor;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public final class WebsocketServerTransporterNetty implements RCPTransporterNetty {

    static final boolean SSL = System.getProperty("ssl") != null;
    //    static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" :
    // "8080"));

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private final SslContext sslCtx;

    private final Channel ch;

    private RCPTransporterListener listener;

    final ChannelGroup allClients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public WebsocketServerTransporterNetty(final int _port) throws
                                                            CertificateException,
                                                            SSLException,
                                                            InterruptedException {

        if (SSL) {
            final SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        }
        else {
            sslCtx = null;
        }

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                 //             .handler(new LoggingHandler(LogLevel.INFO))
                 .childHandler(new WebSocketServerInitializer(sslCtx, this));

        ch = bootstrap.bind(_port).sync().channel();

        System.out.println("Open your web browser and navigate to " +
                           (SSL ? "https" : "http") +
                           "://127.0.0.1:" +
                           _port +
                           '/');
    }

    public void stop() throws InterruptedException {

        allClients.close().awaitUninterruptibly();

        ch.closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    // TODO: resolve this!
    ChannelHandlerContext lastCtx;

    @Override
    public void received(final ChannelHandlerContext ctx, final RCPPacket _packet) {

        lastCtx = ctx;
        received(_packet);

        // done
        lastCtx = null;
    }

    @Override
    public void received(final RCPPacket _packet) {

        if (listener != null) {
            listener.received(_packet);
        }
    }

    @Override
    public void send(final RCPPacket _packet) {

        if (lastCtx != null) {

            lastCtx.channel().writeAndFlush(_packet);
        }
        else {
            // send to all
            System.out.println(" ->> send to all");

            allClients.writeAndFlush(_packet);
        }
    }

    public void sendAll(final RCPPacket _packet) {
        allClients.writeAndFlush(_packet);
    }

    @Override
    public void setListener(final RCPTransporterListener _listener) {

        listener = _listener;
    }

    @Override
    public void addChannel(final Channel _channel) {

        allClients.add(_channel);
    }

    @Override
    public void removeChannel(final Channel _channel) {

        allClients.remove(_channel);
    }
}
