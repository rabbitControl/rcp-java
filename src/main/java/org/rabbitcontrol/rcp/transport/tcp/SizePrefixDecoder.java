package org.rabbitcontrol.rcp.transport.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.rabbitcontrol.rcp.RCP;

import java.util.List;

import static org.rabbitcontrol.rcp.RCP.bytesToHex;

public class SizePrefixDecoder extends ByteToMessageDecoder {

    static final byte[] kZeroData = {};

    //
    private boolean sendPong;

    public SizePrefixDecoder(final boolean _sendPong) {
        sendPong = _sendPong;
    }

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws
                                                                                       Exception
    {
        if (in.readableBytes() < 4) {
            return;
        }

        // read size of necessary data without size-prefix
        in.markReaderIndex();

        if (RCP.doDebugLogging)
        {
            byte[] data = new byte[4];
            in.getBytes(0, data, 0, 4);

            System.out.println("SizePrefixDecoder - 4 bytes: " + bytesToHex(data));
        }

        int expected_data_size = in.readInt();

        if (RCP.doDebugLogging)
        {
            System.out.println("SizePrefixDecoder - readable bytes: " + in.readableBytes() + " " +
                               "expected: " + expected_data_size);
        }

        if (expected_data_size == 0)
        {
            if (sendPong) {
                // no data - answer with "pong"
                ctx.channel().writeAndFlush(kZeroData);
            }

            return;
        }

        if (in.readableBytes() < expected_data_size)
        {
            in.resetReaderIndex();
            return;
        }

        if (RCP.doDebugLogging)
        {
            System.out.println("SizePrefixDecoder - reading: " + expected_data_size);
        }

        // add data to output
        out.add(in.readBytes(expected_data_size));
    }
}
