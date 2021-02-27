/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.rabbitcontrol.rcp.transport.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.transport.*;
import org.rabbitcontrol.rcp.transport.netty.ChannelManager;

public final class TcpServerTransporterNetty implements ServerTransporter, ChannelManager {

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel ch;

    private ServerTransporterListener listener;

    private int serverPort;



    final ChannelGroup allClients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public TcpServerTransporterNetty() {
    }

    @Override
    public void dispose() {

        unbind();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void addChannel(final Channel _channel) {

        allClients.add(_channel);
    }

    @Override
    public void removeChannel(final Channel _channel) {

        allClients.remove(_channel);
    }

    @Override
    public void bind(final int port) throws RCPException {

        if (port == serverPort) {
            return;
        }

        // first unbind
        unbind();

        try {

            final ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .childHandler(new TcpServerInitializer(this, listener, this));

            ch = bootstrap.bind(port).sync().channel();
            serverPort = port;
        }
        catch (InterruptedException _e) {
            _e.printStackTrace();
        }
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
        else {
            System.err.println("not a Channel object??");
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
