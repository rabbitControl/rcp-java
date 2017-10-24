package org.rabbitcontrol.rcp.model.types;

import org.rabbitcontrol.rcp.model.RCPTypeDefinition;
import org.rabbitcontrol.rcp.model.RCPTypes.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import io.kaitai.struct.KaitaiStream;

import java.io.IOException;
import java.io.OutputStream;

public class RCPTypeBOOL extends RCPTypeDefinition<Boolean> {

    public static RCPTypeBOOL parse(final KaitaiStream _io) throws RCPDataErrorException {

        final RCPTypeBOOL type = new RCPTypeBOOL();

        // parse optionals
        while (true) {

            int          did    = _io.readU1();

            if (did == Packet.TERMINATOR.id()) {
                // terminator
                break;
            }

            final TypeDefinition dataid = TypeDefinition.byId(did);

            if (dataid == null) {
                throw new RCPDataErrorException();
            }

            switch (dataid) {

                case DEFAULTVALUE:
                    type.setDefaultValue((_io.readU1() > 0));
                    break;

                default:
                    throw new RCPDataErrorException();
            }

        }

        return type;
    }

    public RCPTypeBOOL() {

        super(Datatype.BOOL);
    }

    @Override
    public RCPTypeDefinition<Boolean> cloneEmpty() {

        return new RCPTypeBOOL();
    }

    @Override
    public void write(final OutputStream _outputStream) throws IOException {

        super.write(_outputStream);

        // finalize typedefinition with terminator
        _outputStream.write((int)Packet.TERMINATOR.id());
    }

    @Override
    public void writeValue(final Boolean _value, final OutputStream _outputStream) throws IOException {
        _outputStream.write(_value ? 1 : 0);
    }

    @Override
    public Boolean getTypeDefault() {

        return false;
    }
}
