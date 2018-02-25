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
package org.rabbitcontrol.rcp.test.netty;

import io.kaitai.struct.ByteBufferKaitaiStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.rabbitcontrol.rcp.model.Packet;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class RCPPacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    public RCPPacketDecoder() {
    }

    @Override
    protected void decode(
            final ChannelHandlerContext ctx,
            final ByteBuf msg,
            final List<Object> out) throws Exception {


        final byte[] data;

        if (msg.nioBufferCount() > 0) {
            final ByteBuffer _buf = msg.nioBuffer();
            _buf.rewind();

            data = new byte[_buf.capacity()];
            _buf.get(data);

            //            if (_buf.hasArray()) {
            //                data = _buf.array();
            //            }
            //            else {
            //
            //            }

        }
        else {
            data = msg.toString(Charset.defaultCharset()).getBytes();
        }

        try {
            final Packet packet = Packet.parse(new ByteBufferKaitaiStream(data));
            out.add(packet);
        }
        catch (final RCPUnsupportedFeatureException _e) {
            throw new Exception(_e);
        }


    }
}
