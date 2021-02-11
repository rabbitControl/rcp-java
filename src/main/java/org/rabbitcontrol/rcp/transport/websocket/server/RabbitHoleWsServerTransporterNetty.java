package org.rabbitcontrol.rcp.transport.websocket.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.transport.*;
import org.rabbitcontrol.rcp.transport.websocket.client.WebSocketClientHandler;
import org.rabbitcontrol.rcp.transport.websocket.client.WebsocketClientInitializer;

import java.net.URI;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class RabbitHoleWsServerTransporterNetty implements ServerTransporter,
                                                                 ClientTransporterListener {

    private Channel ch;

    private final Bootstrap bootstrap;

    private final EventLoopGroup group = new NioEventLoopGroup();

    private ServerTransporterListener listener;

    private final URI uri;

    private final WebsocketClientInitializer initializer;

    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    private final int port;

    public RabbitHoleWsServerTransporterNetty(final URI uri)
    {
        this.uri = uri;

        // figure out the port
        final String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        if (uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = uri.getPort();
        }

        System.out.println("----- RabbitHoleWsServerTransporterNetty");
        System.out.println("uri: " + uri);
        System.out.println("port: " + port);

        bootstrap = new Bootstrap();
        initializer = new WebsocketClientInitializer(uri,null, this);
        bootstrap.group(group)
                 .channel(NioSocketChannel.class)
                 .handler(initializer);
    }

    @Override
    public void bind(final int port) throws RCPException
    {
        connectDelayed();
    }

    private void connectDelayed()
    {
        executor.schedule(new Runnable()
        {
            @Override
            public void run() {
                connect();
            }
        }, 2, TimeUnit.SECONDS);
    }

    private void connect()
    {
        try
        {
            final WebSocketClientHandler websocketHandler = new WebSocketClientHandler(
                    WebSocketClientHandshakerFactory.newHandshaker(uri,
                                                                   WebSocketVersion.V13,
                                                                   null,
                                                                   true,
                                                                   new DefaultHttpHeaders()),
                    this);

            initializer.setHandler(websocketHandler);

            ch = bootstrap.connect(uri.getHost(), port).addListener(new ChannelFutureListener()
            {
                @Override
                public void operationComplete(final ChannelFuture future)
                {
                    if (!future.channel().isOpen())
                    {
                        unbind();
                        connectDelayed();
                    }
                }
            }).sync().channel();

            websocketHandler.handshakeFuture().sync();
        }
        catch (final InterruptedException _e)
        {
            unbind();
        }
        catch (final WebSocketHandshakeException _e)
        {
            unbind();
            connectDelayed();
        }
    }


    @Override
    public void unbind()
    {
        if (ch != null)
        {
            ch.close();
            ch = null;
        }

        initializer.setHandler(null);

        executor.shutdownNow();
        executor = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void sendToOne(final byte[] _data, final Object _id)
    {
        sendToAll(_data, null);
    }

    @Override
    public void sendToAll(final byte[] _data, final Object _excludeId)
    {
        if ((ch != null) && ch.isOpen() && ch.isWritable())
        {
            ch.writeAndFlush(_data);
        }
    }

    @Override
    public int getConnectionCount()
    {
        return ((ch != null) && ch.isOpen() && ch.isWritable()) ? 1 : 0;
    }

    @Override
    public void addListener(final ServerTransporterListener _listener)
    {
        listener = _listener;
    }

    @Override
    public void removeListener(final ServerTransporterListener _listener)
    {
        if ((listener != null) && listener.equals(_listener)) {
            listener = null;
        }
    }


    // ClientTransporterListener

    @Override
    public void connected()
    {
    }

    @Override
    public void disconnected()
    {
        connectDelayed();
    }

    @Override
    public void received(final byte[] _data)
    {
        if (listener != null)
        {
            try {
                listener.received(_data, this, null);
            }
            catch (final RCPException _e) {
                // nop
            }
            catch (final RCPDataErrorException _e) {
                // nop
            }
        }
    }
}
