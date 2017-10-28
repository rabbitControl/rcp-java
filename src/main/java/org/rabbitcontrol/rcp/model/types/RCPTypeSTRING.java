package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RCPTypeDefinition;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by inx on 13/06/17.
 */
public class RCPTypeSTRING extends RCPTypeDefinition<String> {

    public static RCPTypeSTRING parse(final KaitaiStream _io) throws RCPDataErrorException {

        final RCPTypeSTRING type = new RCPTypeSTRING();

        // parse optionals
        while (true) {

            final StringProperty dataid = StringProperty.byId(_io.readU1());

            if (dataid == null) {
                break;
            }

            switch (dataid) {

                case DEFAULTVALUE:
                    LongString longString = new LongString(_io);
                    type.setDefaultValue(longString.data());
                    break;

                default:
                    throw new RCPDataErrorException();
            }

        }

        return type;
    }

    public RCPTypeSTRING() {

        super(Datatype.STRING);
    }

    @Override
    public RCPTypeDefinition<String> cloneEmpty() {

        return new RCPTypeSTRING();
    }

    @Override
    public void write(final OutputStream _outputStream) throws IOException {

        super.write(_outputStream);

        // finalize typedefinition with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    @Override
    public void writeValue(final String _value, final OutputStream _outputStream) throws
                                                                                  IOException {
        RCPParser.writeLongString(_value, _outputStream);
    }

    @Override
    public String getTypeDefault() {

        return "";
    }
}
