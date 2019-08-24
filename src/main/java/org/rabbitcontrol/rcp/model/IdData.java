package org.rabbitcontrol.rcp.model;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class IdData implements RCPWritable {

    public short id;

    public IdData(final short _id) {
        id = _id;
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        _outputStream.write(ByteBuffer.allocate(2).putShort(id).array());
    }
}
