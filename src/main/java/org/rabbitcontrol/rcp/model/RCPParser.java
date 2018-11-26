package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import io.kaitai.struct.RandomAccessFileKaitaiStream;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class RCPParser {

    public static final byte TERMINATOR = 0;

    public static final int TINY_STRING_LENGTH_MAX = 255;
    public static final int SHORT_STRING_LENGTH_MAX = 65535;

    public static void writeTinyString(
            final String _string, final OutputStream _outputStream) throws IOException {

        byte[] bytes = _string.getBytes(Charset.forName("UTF-8"));

        if (bytes.length > TINY_STRING_LENGTH_MAX) {
            // TODO log error
            System.err.println("unit string is too long - truncating");

            bytes = Arrays.copyOf(bytes, TINY_STRING_LENGTH_MAX);
        }

        // write length
        _outputStream.write(bytes.length);
        _outputStream.write(bytes);
    }

    public static void writeShortString(
            final String _string, final OutputStream _outputStream) throws IOException {

        byte[] bytes = _string.getBytes(Charset.forName("UTF-8"));

        if (bytes.length > SHORT_STRING_LENGTH_MAX) {
            // TODO log error
            System.err.println("unit string is too long for short string");

            bytes = Arrays.copyOf(bytes, SHORT_STRING_LENGTH_MAX);
        }

        // write length
        _outputStream.write(ByteBuffer.allocate(2).putShort((short)bytes.length).array());
        _outputStream.write(bytes);
    }

    public static void writeLongString(
            final String _string, final OutputStream _outputStream) throws IOException {

        final byte[] bytes = _string.getBytes(Charset.forName("UTF-8"));

        // write length
        _outputStream.write(ByteBuffer.allocate(4).putInt(bytes.length).array());
        _outputStream.write(bytes);
    }

    //----------------------------------------------------------------
    public static Packet fromFile(final String fileName) throws
                                                            IOException,
                                                            RCPUnsupportedFeatureException,
                                                            RCPDataErrorException {

        final KaitaiStream _io = new RandomAccessFileKaitaiStream(fileName);

        // we read from a file: no magic expected
        //        _io.ensureFixedContents(Packet.RABBIT_MAGIC);

        // got magic parse packet
        final Packet packet = Packet.parse(_io);

        return packet;
    }

}
