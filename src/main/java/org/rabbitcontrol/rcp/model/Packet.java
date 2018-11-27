package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.Command;
import org.rabbitcontrol.rcp.model.RcpTypes.PacketOptions;
import org.rabbitcontrol.rcp.model.exceptions.*;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by inx on 13/06/17.
 */
public class Packet implements RCPWritable {

    public static final byte[] RABBIT_MAGIC = { 4, 15, 5, 9 };

    public static byte[] serialize(final Packet _packet, final boolean _all) throws
                                                                             IOException,
                                                                             RCPException {

        byte[] result;
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            _packet.write(os, _all);
            result = os.toByteArray();
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

        // update value without options
        // handle separate
        if (cmd == Command.UPDATEVALUE) {

            final Parameter parameter = Parameter.parseValueUpdate(_io);
            packet.setData(parameter);

            // check for leftover data
            if (!_io.isEof()) {
                throw new RCPDataErrorException("UPDATEVALUE - still data to read!");
            }

            return packet;
        }



        // read packet options
        while (!_io.isEof()) {

            final int property_id = _io.readU1();

            if (property_id == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            final PacketOptions option = PacketOptions.byId(property_id);

            if (option == null) {
                // wrong data id... skip whole packet?
                throw new RCPDataErrorException();
            }

            switch (option) {
                case DATA:

                    if (packet.getData() != null) {
                        throw new RCPDataErrorException();
                    }

                    switch (cmd) {

                        case VERSION:
                            // version: expect meta
                            // TODO: implement
                            System.out.println("VERSION not implement");
                            break;

                        case INITIALIZE:
                            // init - shout not happen
                            // init could send ID-Data
                            // read id
                            final short init_id = _io.readS2be();
                            // read terminator?
                            throw new RCPDataErrorException();

                        case DISCOVER:
                            System.out.println("DISCOVER not implement");
                            break;

                        case REMOVE:
                        case UPDATE:
                            // expect parameter
                            packet.setData(Parameter.parse(_io));
                            break;

                        case INVALID:
                            break;
                    }

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

    public byte[] serialize(final boolean _all) throws IOException, RCPException {
        return Packet.serialize(this, _all);
    }

    //--------------------------------------------------------
    public void write(final boolean _magic,
                      final OutputStream _outputStream,
                      final boolean _all) throws IOException, RCPException {
        if (_magic) {
            // write magic
            _outputStream.write(RABBIT_MAGIC);
        }

        write(_outputStream, _all);
    }


    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws
                                                                            IOException,
                                                                            RCPException {

        // ignore flag "all" for packets:
        // packets are short living objects

        // write mandatory command
        _outputStream.write((int)cmd.id());

        // check for valueupdate
        if (cmd == Command.UPDATEVALUE) {

            if (data instanceof Parameter) {
                ((Parameter)data).writeUpdateValue(_outputStream);
            }

        } else {

            if (timestamp != null) {
                _outputStream.write((int)PacketOptions.TIMESTAMP.id());
                _outputStream.write(ByteBuffer.allocate(8).putLong(timestamp).array());
            }

            if (data != null) {
                _outputStream.write((int)PacketOptions.DATA.id());
                data.write(_outputStream, _all);
            }

            // finalize packet with terminator
            _outputStream.write(RCPParser.TERMINATOR);
        }

    }

    //--------------------------------------------------------

    public Command getCmd() {

        return cmd;
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

    public IParameter getDataAsParameter() throws ClassCastException {
        return (IParameter)data;
    }
}
