package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Command;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by inx on 13/06/17.
 */
public class Packet implements RCPWritable {

    public static final byte[] TOI_MAGIC = { 4, 15, 5, 9 };

    public static byte[] serialize(final Packet _packet) throws IOException {

        byte[] result = null;
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            _packet.write(os);
            result = os.toByteArray();
        }
        catch (IOException _e) {
            _e.printStackTrace();
        }
        finally {
            os.close();
        }

        return result;
    }

    public static Packet parse(final KaitaiStream _io) throws
                                                          RCPUnsupportedFeatureException,
                                                          RCPDataErrorException {

        // get mandatory
        final Command cmd = Command.byId(_io.readU1());

        if (cmd == null) {
            throw new RCPDataErrorException();
        }

        final Packet packet = new Packet(cmd);

        // read packet options
        while (!_io.isEof()) {

            final int property_id = _io.readU1();

            if (property_id == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            final org.rabbitcontrol.rcp.model.gen.RcpTypes.Packet
                    property = org.rabbitcontrol.rcp.model.gen.RcpTypes.Packet.byId(property_id);

            if (property == null) {
                // wrong data id... skip whole packet?
                throw new RCPDataErrorException();
            }

            switch (property) {
                case DATA:

                    if (packet.getData() != null) {
                        throw new RCPDataErrorException();
                    }

                    switch (cmd) {
                        case INITIALIZE:
                            // init - shout not happen
                            throw new RCPDataErrorException();

                        case ADD:
                        case REMOVE:
                        case UPDATE:
                            // expect parameter
                            packet.setData(Parameter.parse(_io));
                            break;

                        case VERSION:
                            // version: expect meta
                            // TODO: implement
                            break;
                    }

                    break;

                case ID:
                    packet.setPacketId(_io.readU4be());
                    break;

                case TIMESTAMP:
                    packet.setTimestamp(_io.readU8be());
                    break;
                default:
                    throw new RCPDataErrorException();
            }

        }

        return packet;
    }

    //--------------------------------------------------------
    // mandatory
    private final Command cmd;

    // options
    private Long packetId;

    private Long timestamp;

    private RCPWritable data;

    //--------------------------------------------------------
    //--------------------------------------------------------
    public Packet(final Command _cmd) {

        this(_cmd, null);
    }

    public Packet(final Command _cmd, final RCPWritable _data) {

        cmd = _cmd;
        data = _data;
    }

    //--------------------------------------------------------
    public void write(final boolean _magic, final OutputStream _outputStream) throws IOException {

        if (_magic) {
            // write magic
            _outputStream.write(TOI_MAGIC);
        }

        write(_outputStream);
    }

    @Override
    public void write(final OutputStream _outputStream) throws IOException {

        // write mandatory command
        _outputStream.write((int)cmd.id());

        if (packetId != null) {
            _outputStream.write((int)org.rabbitcontrol.rcp.model.gen.RcpTypes.Packet.ID.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(packetId.intValue()).array());
        }

        if (timestamp != null) {
            _outputStream.write((int)org.rabbitcontrol.rcp.model.gen.RcpTypes.Packet.TIMESTAMP.id());
            _outputStream.write(ByteBuffer.allocate(8).putLong(timestamp).array());
        }

        if (data != null) {
            _outputStream.write((int)org.rabbitcontrol.rcp.model.gen.RcpTypes.Packet.DATA.id());
            data.write(_outputStream);
        }

        // finalize packet with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    //--------------------------------------------------------

    public Command getCmd() {

        return cmd;
    }

    public Long getPacketId() {

        return packetId;
    }

    public void setPacketId(final long _packetId) {

        packetId = _packetId;
    }

    public Long getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(final long _timestamp) {

        timestamp = _timestamp;
    }

    public RCPWritable getData() {

        return data;
    }

    public void setData(final RCPWritable _data) {

        data = _data;
    }
}
