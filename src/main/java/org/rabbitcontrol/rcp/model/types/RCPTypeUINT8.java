package org.rabbitcontrol.rcp.model.types;

import org.rabbitcontrol.rcp.model.RCPTypeDefinition;
import org.rabbitcontrol.rcp.model.RCPTypes.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import io.kaitai.struct.KaitaiStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by inx on 13/06/17.
 */
public class RCPTypeUINT8 extends RCPTypeNumber<Short> {

    public static RCPTypeUINT8 parse(final KaitaiStream _io) throws RCPDataErrorException {

        final RCPTypeUINT8 type = new RCPTypeUINT8();

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
                    type.setDefaultValue((short)_io.readU1());
                    break;
                case MIN:
                    type.setMin((short)_io.readU1());
                    break;
                case MAX:
                    type.setMax((short)_io.readU1());
                    break;
                case MULT:
                    type.setMultipleof((short)_io.readU1());
                    break;

                default:
                    RCPTypeNumber.parseOption(type, dataid, _io);
            }

        }

        return type;
    }

    public RCPTypeUINT8() {

        super(Datatype.UINT8);
    }

    @Override
    public RCPTypeDefinition<Short> cloneEmpty() {

        return new RCPTypeUINT8();
    }

    @Override
    public void writeValue(final Short _value, final OutputStream _outputStream) throws
                                                                                 IOException {

        _outputStream.write(_value);
    }

    @Override
    public Short getTypeDefault() {

        return 0;
    }
}
