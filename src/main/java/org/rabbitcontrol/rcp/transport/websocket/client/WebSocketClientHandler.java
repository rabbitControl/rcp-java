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
//The MIT License
//
//Copyright (c) 2009 Carl Bystrom
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.

package org.rabbitcontrol.rcp.transport.websocket.client;

import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.rabbitcontrol.rcp.RCP;
import org.rabbitcontrol.rcp.transport.ClientTransporterListener;

@Sharable
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private ChannelPromise handshakeFuture;
    private final WebSocketClientHandshaker handshaker;
    private final ClientTransporterListener listener;

    public WebSocketClientHandler(final WebSocketClientHandshaker handshaker,
                                  final ClientTransporterListener _listener) {
        this.handshaker = handshaker;
        listener = _listener;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx)
    {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception
    {
        super.channelInactive(ctx);

        if (listener != null)
        {
            listener.disconnected();
        }
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final Object msg) throws Exception
    {
        if (!handshaker.isHandshakeComplete() && (msg instanceof FullHttpResponse))
        {
            try
            {
                handshaker.finishHandshake(ctx.channel(), (FullHttpResponse) msg);
                handshakeFuture.setSuccess();
            }
            catch (final WebSocketHandshakeException e)
            {
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            final FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        if (msg instanceof WebSocketFrame)
        {
            final WebSocketFrame frame = (WebSocketFrame) msg;
            if (frame instanceof TextWebSocketFrame)
            {
                final TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
                if ( RCP.doDebugLogging) System.out.println("WebSocket Client received text " +
                                                           "message: " + textFrame.text());
            }
            else if (frame instanceof BinaryWebSocketFrame)
            {
                ctx.fireChannelRead(frame.retain());
            }
            else if (frame instanceof PongWebSocketFrame)
            {
                // nop
            }
            else if (frame instanceof PingWebSocketFrame)
            {
                // answer with pong
                ctx.channel().writeAndFlush(new PongWebSocketFrame());
            }
            else if (frame instanceof CloseWebSocketFrame) {
                ctx.close();
            }
        }
        //
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }
}
