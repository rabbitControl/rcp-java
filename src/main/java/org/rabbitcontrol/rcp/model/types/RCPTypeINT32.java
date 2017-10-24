package org.rabbitcontrol.rcp.model.types;

import org.rabbitcontrol.rcp.model.RCPTypeDefinition;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPTypes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by inx on 13/06/17.
 */
public class RCPTypeINT32 extends RCPTypeNumber<Integer> {

    public static RCPTypeINT32 parse(final KaitaiStream _io) throws RCPDataErrorException {

        final RCPTypeINT32 type = new RCPTypeINT32();

        // parse optionals
        while (true) {

            int          did    = _io.readU1();

            if (did == RCPTypes.Packet.TERMINATOR.id()) {
                // terminator
                break;
            }

            final RCPTypes.TypeNumber dataid = RCPTypes.TypeNumber.byId(did);

            if (dataid == null) {
                throw new RCPDataErrorException();
            }

            switch (dataid) {

                case DEFAULTVALUE:
                    type.setDefaultValue(_io.readS4be());
                    break;
                case MIN:
                    type.setMin(_io.readS4be());
                    break;
                case MAX:
                    type.setMax(_io.readS4be());
                    break;
                case MULT:
                    type.setMultipleof(_io.readS4be());
                    break;

                default:
                    RCPTypeNumber.parseOption(type, dataid, _io);
            }

        }

        return type;
    }

    public RCPTypeINT32() {
        super(RCPTypes.Datatype.INT32);
    }

    @Override
    public RCPTypeDefinition<Integer> cloneEmpty() {

        return new RCPTypeINT32();
    }

    @Override
    public void writeValue(final Integer _value, final OutputStream _outputStream) throws
                                                                                   IOException {
        _outputStream.write(ByteBuffer.allocate(4).putInt(_value).array());
    }

    @Override
    public Integer getTypeDefault() {

        return 0;
    }
}
