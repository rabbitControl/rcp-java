package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.interfaces.ITypeDefinition;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.types.GroupDefinition;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class GroupParameter extends Parameter {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public GroupParameter(final int _id) {

        super(_id, new GroupDefinition());
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public ITypeDefinition getTypeDefinition() {

        return typeDefinition;
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        // throw error...
        return false;
    }

    @Override
    public void write(final OutputStream _outputStream) throws IOException {

        // write mandatory id
        _outputStream.write(ByteBuffer.allocate(4).putInt((int)id).array());

        // finalize parameter with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }
}
