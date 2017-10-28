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
public class RCPTypeFLOAT64 extends RCPTypeNumber<Double> {

    public static RCPTypeFLOAT64 parse(final KaitaiStream _io) throws RCPDataErrorException {

        final RCPTypeFLOAT64 type = new RCPTypeFLOAT64();

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
                    type.setDefaultValue(_io.readF8be());
                    break;
                case MINIMUM:
                    type.setMin(_io.readF8be());
                    break;
                case MAXIMUM:
                    type.setMax(_io.readF8be());
                    break;
                case MULTIPLEOF:
                    type.setMultipleof(_io.readF8be());
                    break;

                default:
                    RCPTypeNumber.parseOption(type, dataid, _io);
            }

        }

        return type;
    }

    public RCPTypeFLOAT64() {

        super(Datatype.FLOAT64);
    }

    public RCPTypeFLOAT64(
            final Double _min, final Double _max) {

        super(Datatype.FLOAT64, _min, _max, null);
    }

    public RCPTypeFLOAT64(
            final Double _min, final Double _max, final Double _multipleof) {

        super(Datatype.FLOAT64, _min, _max, _multipleof);
    }

    @Override
    public RCPTypeDefinition<Double> cloneEmpty() {

        return new RCPTypeFLOAT64();
    }

    @Override
    public void writeValue(final Double _value, final OutputStream _outputStream) throws
                                                                                  IOException {

        _outputStream.write(ByteBuffer.allocate(8).putDouble(_value).array());
    }

    @Override
    public Double getTypeDefault() {

        return 0.D;
    }
}
