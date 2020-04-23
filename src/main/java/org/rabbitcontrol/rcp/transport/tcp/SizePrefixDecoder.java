package org.rabbitcontrol.rcp.transport.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class SizePrefixDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws
                                                                                       Exception
    {
        if (in.readableBytes() < 4) {
            return;
        }

        // read size of necessary data without size-prefix
        in.markReaderIndex();
        int expected_data_size = in.readInt();

        if (in.readableBytes() < expected_data_size) {
            in.resetReaderIndex();
            return;
        }

        // add data to output
        out.add(in.readBytes(expected_data_size));
    }
}
