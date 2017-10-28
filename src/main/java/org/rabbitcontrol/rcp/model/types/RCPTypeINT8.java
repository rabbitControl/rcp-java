package org.rabbitcontrol.rcp.model.types;

import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RCPTypeDefinition;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import io.kaitai.struct.KaitaiStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by inx on 13/06/17.
 */
public class RCPTypeINT8 extends RCPTypeNumber<Byte> {

    public static RCPTypeINT8 parse(final KaitaiStream _io) throws RCPDataErrorException {

        final RCPTypeINT8 type = new RCPTypeINT8();

        // parse optionals
        while (true) {

            int prop_id = _io.readU1();

            if (prop_id == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            final NumberProperty property = NumberProperty.byId(prop_id);

            //NumberProperty

            if (property == null) {
                throw new RCPDataErrorException();
            }

            switch (property) {

                case DEFAULTVALUE:
                    type.setDefaultValue(_io.readS1());
                    break;
                case MIN:
                    type.setMin(_io.readS1());
                    break;
                case MAX:
                    type.setMax(_io.readS1());
                    break;
                case MULT:
                    type.setMultipleof(_io.readS1());
                    break;

                default:
                    parseOption(type, property, _io);
            }

        }

        return type;
    }

    public RCPTypeINT8() {

        super(Datatype.INT8);
    }

    @Override
    public RCPTypeDefinition<Byte> cloneEmpty() {

        return new RCPTypeINT8();
    }

    @Override
    public void writeValue(final Byte _value, final OutputStream _outputStream) throws IOException {

        _outputStream.write(_value);
    }

    @Override
    public Byte getTypeDefault() {

        return 0;
    }
}
