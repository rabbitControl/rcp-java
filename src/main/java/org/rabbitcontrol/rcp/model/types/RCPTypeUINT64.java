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
public class RCPTypeUINT64 extends RCPTypeNumber<Long> {

    public static RCPTypeUINT64 parse(final KaitaiStream _io) throws RCPDataErrorException {

        final RCPTypeUINT64 type = new RCPTypeUINT64();

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

                case DEFAULTVALUE:
                    type.setDefaultValue(_io.readU8be());
                    break;
                case MIN:
                    type.setMin(_io.readU8be());
                    break;
                case MAX:
                    type.setMax(_io.readU8be());
                    break;
                case MULT:
                    type.setMultipleof(_io.readU8be());
                    break;

                default:
                    RCPTypeNumber.parseOption(type, dataid, _io);
            }

        }

        return type;
    }

    public RCPTypeUINT64() {

        super(Datatype.UINT64);
    }

    @Override
    public RCPTypeDefinition<Long> cloneEmpty() {

        return new RCPTypeUINT64();
    }

    @Override
    public void writeValue(final Long _value, final OutputStream _outputStream) throws IOException {

        _outputStream.write(ByteBuffer.allocate(8).putLong(_value).array());
    }

    @Override
    public Long getTypeDefault() {

        return Long.valueOf(0);
    }
}
