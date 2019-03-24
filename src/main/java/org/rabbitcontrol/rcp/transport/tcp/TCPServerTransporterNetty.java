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
package org.rabbitcontrol.rcp.transport.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.rabbitcontrol.rcp.transport.RCPTransporterListener;
import org.rabbitcontrol.rcp.transport.netty.ChannelManager;

public final class TCPServerTransporterNetty implements ChannelManager {

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private final Channel ch;

    private RCPTransporterListener listener;

    final ChannelGroup allClients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public TCPServerTransporterNetty(final int _port) throws InterruptedException {

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
         .channel(NioServerSocketChannel.class)
         .childHandler(new TCPServerInitializer(this));

        ch = bootstrap.bind(_port).sync().channel();
    }

    public void stop() throws InterruptedException {

        ch.closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

//    ChannelHandlerContext lastCtx;

//    public void received(final ChannelHandlerContext ctx, final Packet _packet) {
//
//        lastCtx = ctx;
//        received(_packet, this);
//
//        // done
//        lastCtx = null;
//    }
//
//    @Override
//    public void received(final Packet _packet, final RCPTransporter _transporter) {
//        if (listener != null) {
//            listener.received(_packet, this);
//        }
//    }
//
//
//    @Override
//    public void send(final byte[] _packet) {
//        // TODO
//    }
//
//    @Override
//    public void setListener(final RCPTransporterListener _listener) {
//
//        listener = _listener;
//    }

    @Override
    public void addChannel(final Channel _channel) {

        allClients.add(_channel);
    }

    @Override
    public void removeChannel(final Channel _channel) {

        allClients.remove(_channel);
    }
}
