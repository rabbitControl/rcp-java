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
package org.rabbitcontrol.rcp.transport.websocket.server;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression
        .WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import org.rabbitcontrol.rcp.transport.netty.BinaryWebSocketFrameEncoder;
import org.rabbitcontrol.rcp.transport.netty.ChannelManager;
import org.rabbitcontrol.rcp.transport.ServerTransporter;
import org.rabbitcontrol.rcp.transport.ServerTransporterListener;

import java.util.List;

public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final String WEBSOCKET_PATH = "/websocket";

    private final SslContext sslCtx;

    private final ServerTransporterListener listener;

    private final ServerTransporter transporter;

    private final ChannelManager channelManager;

    public WebSocketServerInitializer(
            final SslContext sslCtx,
            final ServerTransporter _transporter,
            final ServerTransporterListener listener,
            final ChannelManager _channelManager) {

        this.sslCtx = sslCtx;
        transporter = _transporter;
        this.listener = listener;
        channelManager = _channelManager;
    }

    @Override
    public void initChannel(final SocketChannel ch) throws Exception {

        final ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler("/", null, true));
        //        pipeline.addLast(new WebSocketIndexPageHandler(WEBSOCKET_PATH));

        pipeline.addLast(new MessageToMessageDecoder<WebSocketFrame>() {

            @Override
            public void channelActive(final ChannelHandlerContext ctx) throws Exception {

                if (channelManager != null) {
                    channelManager.addChannel(ctx.channel());
                }

                super.channelActive(ctx);
            }

            @Override
            public void channelInactive(final ChannelHandlerContext ctx) throws Exception {

                if (channelManager != null) {
                    channelManager.removeChannel(ctx.channel());
                }

                super.channelInactive(ctx);
            }

            @Override
            protected void decode(
                    final ChannelHandlerContext ctx,
                    final WebSocketFrame msg,
                    final List<Object> out) throws Exception {

                //out.add(msg.content().retain());

                if (listener != null) {

                    byte[] array = new byte[msg.content().readableBytes()];
                    msg.content().getBytes(0, array);

                    listener.received(array, transporter, ctx.channel());
                }
            }
        });

        //        pipeline.addLast(new ByteArrayDecoder());

        //        pipeline.addLast(new RCPPacketDecoder());
        //        pipeline.addLast(new RCPPacketHandler(listener));

        // encoder
        pipeline.addLast(new BinaryWebSocketFrameEncoder());
        pipeline.addLast(new ByteArrayEncoder());
        //        pipeline.addLast(new StringTextWebSocketFrameEncoder());
        //        pipeline.addLast(new ByteArrayTextWebSocketFrameEncoder());
        //pipeline.addLast(new RCPPacketEncoder());
    }

}
