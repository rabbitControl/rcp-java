package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.TypeDefinition;

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
    public void writeOptions(final OutputStream _outputStream, final boolean _all) {
    }
}
