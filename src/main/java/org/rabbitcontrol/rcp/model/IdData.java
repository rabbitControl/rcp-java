package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class IdData implements RCPWritable {

    public static IdData parse(final KaitaiStream _io) {

        short id = _io.readS2be();

        return new IdData(id);
    }

    private short id;

    public IdData(final short _id) {
        id = _id;
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        _outputStream.write(ByteBuffer.allocate(2).putShort(id).array());
    }

    public short getId() {

        return id;
    }
}
