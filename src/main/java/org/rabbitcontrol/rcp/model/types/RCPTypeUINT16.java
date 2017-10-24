package org.rabbitcontrol.rcp.model.types;

import org.rabbitcontrol.rcp.model.RCPTypeDefinition;
import org.rabbitcontrol.rcp.model.RCPTypes.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import io.kaitai.struct.KaitaiStream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by inx on 13/06/17.
 */
public class RCPTypeUINT16 extends RCPTypeNumber<Integer> {

    public static RCPTypeUINT16 parse(final KaitaiStream _io) throws RCPDataErrorException {

        final RCPTypeUINT16 type = new RCPTypeUINT16();

        // parse optionals
        while (true) {

            int did = _io.readU1();

            if (did == Packet.TERMINATOR.id()) {
                // terminator
                break;
            }

            final TypeNumber dataid = TypeNumber.byId(did);

            if (dataid == null) {
                throw new RCPDataErrorException();
            }

            switch (dataid) {

                case DEFAULTVALUE:
                    type.setDefaultValue(_io.readU2be());
                    break;
                case MIN:
                    type.setMin(_io.readU2be());
                    break;
                case MAX:
                    type.setMax(_io.readU2be());
                    break;
                case MULT:
                    type.setMultipleof(_io.readU2be());
                    break;

                default:
                    RCPTypeNumber.parseOption(type, dataid, _io);
            }

        }

        return type;
    }

    public RCPTypeUINT16() {

        super(Datatype.UINT16);
    }

    @Override
    public RCPTypeDefinition<Integer> cloneEmpty() {

        return new RCPTypeUINT16();
    }

    @Override
    public void writeValue(final Integer _value, final OutputStream _outputStream) throws
                                                                                   IOException {

        _outputStream.write(ByteBuffer.allocate(2).putShort(_value.shortValue()).array());
    }

    @Override
    public Integer getTypeDefault() {

        return 0;
    }
}
