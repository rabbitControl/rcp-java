package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.TypeDefinition;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;

import java.io.IOException;
import java.io.OutputStream;

public class GroupDefinition extends TypeDefinition {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public static GroupDefinition parse(final KaitaiStream _io) throws RCPDataErrorException {

        final GroupDefinition type = new GroupDefinition();

        // parse optionals
        while (true) {

            int          did    = _io.readU1();

            if (did == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            // no other property
            // .. error?
            System.err.println("error: invalid options for groupdefinition. " + did);

        }

        return type;
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    public GroupDefinition() {

        super(Datatype.GROUP);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        return false;
    }

    @Override
    public void write(final OutputStream _outputStream) throws IOException {

        // write mandatory datatype
        _outputStream.write((int)getDatatype().id());

        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }
}
