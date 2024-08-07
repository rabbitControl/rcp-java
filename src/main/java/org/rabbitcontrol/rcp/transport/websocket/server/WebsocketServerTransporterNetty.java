package org.rabbitcontrol.rcp.transport.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.rabbitcontrol.rcp.RCP;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.transport.ServerTransporter;
import org.rabbitcontrol.rcp.transport.ServerTransporterListener;
import org.rabbitcontrol.rcp.transport.netty.ChannelManager;

import javax.net.ssl.SSLException;
import java.io.InputStream;

public final class WebsocketServerTransporterNetty implements ServerTransporter, ChannelManager
{
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private SslContext sslCtx;

    private Channel ch;

    private ServerTransporterListener listener;

    private int serverPort;

    final ChannelGroup allClients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public WebsocketServerTransporterNetty() {}

    @Override
    public void dispose()
    {
        unbind();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void addChannel(final Channel _channel) {

        if (RCP.doDebugLogging) System.out.println("client connected: " + _channel.remoteAddress());

        allClients.add(_channel);
    }

    @Override
    public void removeChannel(final Channel _channel) {

        if (RCP.doDebugLogging) System.out.println("client disconnected: " + _channel.remoteAddress());

        allClients.remove(_channel);
    }

    @Override
    public void bind(final int port) throws RCPException
    {
        bind(port, null, null);
    }


    public void bind(final int port,
                     final InputStream keyCertChainInputStream,
                     final InputStream keyInputStream) throws RCPException {

        if (port == serverPort) {
            return;
        }

        // first unbind
        unbind();

        try {
            // try setup server
            if ((keyCertChainInputStream != null) && (keyInputStream != null))
            {
                sslCtx = SslContextBuilder.forServer(keyCertChainInputStream, keyInputStream).build();
            }
            else {
                sslCtx = null;
            }

            final ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                     //             .handler(new LoggingHandler(LogLevel.INFO))
                     .childHandler(new WebSocketServerInitializer(sslCtx, this, listener, this));

            ch = bootstrap.bind(port).sync().channel();
            serverPort = port;
        }
        catch (final InterruptedException _e) {
            throw new RCPException(_e);
        }
        catch (final SSLException _e) {
            throw new RCPException(_e);
        }

        // TODO: how to get the error out of it?
    }

    @Override
    public void unbind() {

        if (!allClients.isEmpty()) {
            allClients.close().awaitUninterruptibly();
        }

        if (ch != null) {

            ch.close();

            // TODO: get the error out of this...
            ch = null;
        }

        serverPort = 0;
    }

    @Override
    public void sendToOne(final byte[] _data, final Object _id) {

        if (_id instanceof Channel) {
            ((Channel)_id).writeAndFlush(_data);
        }
    }

    @Override
    public void sendToAll(final byte[] _data, final Object _excludeId) {

        if (_excludeId instanceof Channel) {

            allClients.writeAndFlush(_data, new ChannelMatcher() {

                @Override
                public boolean matches(final Channel channel) {

                    return (!channel.equals(_excludeId));
                }
            });
        }
        else {
            allClients.writeAndFlush(_data);
        }
    }

    @Override
    public int getConnectionCount() {

        return allClients.size();
    }

    @Override
    public void addListener(final ServerTransporterListener _listener) {

        listener = _listener;
    }

    @Override
    public void removeListener(final ServerTransporterListener _listener) {

        if ((listener != null) && listener.equals(_listener)) {
            listener = null;
        }

    }
}
