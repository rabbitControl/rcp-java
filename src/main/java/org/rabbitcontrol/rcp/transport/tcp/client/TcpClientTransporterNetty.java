package org.rabbitcontrol.rcp.transport.tcp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.rabbitcontrol.rcp.transport.ClientTransporter;
import org.rabbitcontrol.rcp.transport.ClientTransporterListener;

public class TcpClientTransporterNetty implements ClientTransporter {


    private EventLoopGroup group;

    private       Channel   ch;

    private final Bootstrap bootstrap;

    private ClientTransporterListener listener;

    public  TcpClientTransporterNetty() {

        bootstrap = new Bootstrap();
    }

    @Override
    public void connect(final String host, final int port) {

        disconnect();

        if (group == null)
        {
            group = new NioEventLoopGroup();
        }

        bootstrap.group(group)
                 .channel(NioSocketChannel.class)
                 .handler(new TcpClientInitializer(listener));

        try {
            ch = bootstrap.connect(host, port).sync().channel();

            if (listener != null)
            {
                listener.connected();
            }
        }
        catch (final Exception _e) {
            System.err.println(_e.getMessage());
            ch = null;
        }
    }

    @Override
    public void disconnect() {

        if (isConnected()) {
            ch.close();
        }

        if (group != null)
        {
            group.shutdownGracefully();
            group = null;
        }
    }

    @Override
    public boolean isConnected() {

        return (ch != null) && ch.isOpen();
    }

    @Override
    public void send(final byte[] _data) {

        if (isConnected()) {

            if (ch.isWritable()) {
                ch.writeAndFlush(Unpooled.wrappedBuffer(_data));
            }
            else {
                System.err.println("channel not writeable!");
            }
        } else {
            System.err.println("client not connected");
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
