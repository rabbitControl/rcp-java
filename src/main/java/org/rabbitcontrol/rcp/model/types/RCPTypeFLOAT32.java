package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RCPTypeDefinition;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.gen.RcpTypes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by inx on 13/06/17.
 */
public class RCPTypeFLOAT32 extends RCPTypeNumber<Float> {

    public static RCPTypeFLOAT32 parse(final KaitaiStream _io) throws RCPDataErrorException {

        final RCPTypeFLOAT32 type = new RCPTypeFLOAT32();

        // parse optionals
        while (true) {

            int          did    = _io.readU1();

            if (did == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            final RcpTypes.NumberProperty dataid = RcpTypes.NumberProperty.byId(did);

            if (dataid == null) {
                throw new RCPDataErrorException();
            }

            switch (dataid) {

                case DEFAULT:
                    type.setDefaultValue(_io.readF4be());
                    break;
                case MINIMUM:
                    type.setMin(_io.readF4be());
                    break;
                case MAXIMUM:
                    type.setMax(_io.readF4be());
                    break;
                case MULTIPLEOF:
                    type.setMultipleof(_io.readF4be());
                    break;

                default:
                    RCPTypeNumber.parseOption(type, dataid, _io);
            }

        }

        return type;
    }

    public RCPTypeFLOAT32() {
        super(RcpTypes.Datatype.FLOAT32);
    }

    @Override
    public RCPTypeDefinition<Float> cloneEmpty() {

        return new RCPTypeFLOAT32();
    }

    @Override
    public void writeValue(final Float _value, final OutputStream _outputStream) throws IOException {
        _outputStream.write(ByteBuffer.allocate(4).putFloat(_value).array());
    }

    @Override
    public Float getTypeDefault() {

        return 0.f;
    }
}
