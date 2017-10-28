package org.rabbitcontrol.rcp.model.types;

import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RCPTypeDefinition;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import io.kaitai.struct.KaitaiStream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by inx on 13/06/17.
 */
public class RCPTypeINT16 extends RCPTypeNumber<Short> {

    //----------------------------------------------------------------
    //
    public static RCPTypeINT16 parse(final KaitaiStream _io) throws RCPDataErrorException {

        final RCPTypeINT16 type = new RCPTypeINT16();

        // parse optionals
        while (true) {

            int did = _io.readU1();

            if (did == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            final NumberProperty dataid = NumberProperty.byId(did);

            if (dataid == null) {
                throw new RCPDataErrorException();
            }

            switch (dataid) {

                case DEFAULT:
                    type.setDefaultValue(_io.readS2be());
                    break;
                case MINIMUM:
                    type.setMin(_io.readS2be());
                    break;
                case MAXIMUM:
                    type.setMax(_io.readS2be());
                    break;
                case MULTIPLEOF:
                    type.setMultipleof(_io.readS2be());
                    break;

                default:
                    parseOption(type, dataid, _io);
            }

        }

        return type;
    }

    //----------------------------------------------------------------
    //
    public RCPTypeINT16() {

        super(Datatype.INT16);
    }

    public RCPTypeINT16(final short _default) {

        this();

        setDefaultValue(_default);
    }

    public RCPTypeINT16(final short _default, final short _min, final short _max) {

        this(_default);

        setMin(_min);
        setMax(_max);
    }

    public RCPTypeINT16(
            final short _default, final short _min, final short _max, short _multipleof) {

        this(_default, _min, _max);

        setMultipleof(_multipleof);
    }

    @Override
    public RCPTypeDefinition<Short> cloneEmpty() {

        return new RCPTypeINT16();
    }

    //----------------------------------------------------------------
    //
    @Override
    public void writeValue(final Short _value, final OutputStream _outputStream) throws
                                                                                 IOException {

        _outputStream.write(ByteBuffer.allocate(2).putShort(_value).array());
    }

    @Override
    public Short getTypeDefault() {

        return 0;
    }
}
