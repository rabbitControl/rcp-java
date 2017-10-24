package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by inx on 13/06/17.
 */
public class RCPParser {

    public static void writeTinyString(
            final String _string, final OutputStream _outputStream) throws IOException {

        final byte[] bytes = _string.getBytes(Charset.forName("UTF-8"));

        if (bytes.length > 255) {
            // TODO log error
            System.err.println("unit string is too long");
        }

        // write length
        _outputStream.write(bytes.length);
        _outputStream.write(bytes);
    }

    public static void writeShortString(
            final String _string, final OutputStream _outputStream) throws IOException {

        final byte[] bytes = _string.getBytes(Charset.forName("UTF-8"));

        if (bytes.length > 65535) {
            // TODO log error
            System.err.println("unit string is too long for short string");
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
    public static RCPPacket fromFile(final String fileName) throws
                                                            IOException,
                                                            RCPUnsupportedFeatureException,
                                                            RCPDataErrorException {

        final KaitaiStream _io = new KaitaiStream(fileName);

        // we read from a file: no magic expected
        //        _io.ensureFixedContents(RCPPacket.TOI_MAGIC);

        // got magic parse packet
        final RCPPacket packet = RCPPacket.parse(_io);

        return packet;
    }

}
