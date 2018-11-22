package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.*;
import org.rabbitcontrol.rcp.model.TypeDefinition;

import java.io.IOException;
import java.io.OutputStream;

public class BangDefinition extends TypeDefinition {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public BangDefinition() {

        super(Datatype.BANG);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        return false;
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        // write mandatory fields and defaultValue
        _outputStream.write((int)getDatatype().id());

        if (!_all) {
            initialWrite = false;
        }

        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }
}
