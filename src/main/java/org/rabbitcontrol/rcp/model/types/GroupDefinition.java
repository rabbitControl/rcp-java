package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.TypeDefinition;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;

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
    public void writeOptions(final OutputStream _outputStream, final boolean _all) {
    }
}
