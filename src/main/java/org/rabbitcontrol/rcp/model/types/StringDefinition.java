package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.*;

import java.io.IOException;
import java.io.OutputStream;

public class StringDefinition extends DefaultDefinition<String> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public StringDefinition() {

        super(Datatype.STRING);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final StringOptions option = StringOptions.byId(_propertyId);

        if (option == null) {
            return false;
        }

        switch (option) {
            case DEFAULT:
                final LongString longString = new LongString(_io);
                setDefault(longString.data());
                return true;
        }

        return false;
    }

    @Override
    public String readValue(final KaitaiStream _io) {

        final LongString longString = new LongString(_io);
        return longString.data();
    }

    @Override
    public void writeValue(final String _value, final OutputStream _outputStream) throws
                                                                                  IOException {
        RCPParser.writeLongString(_value, _outputStream);
    }

    @Override
    public void write(final OutputStream _outputStream) throws IOException {

        // write mandatory fields and defaultValue
        _outputStream.write((int)getDatatype().id());

        if (getDefault() != null) {
            // use any of the default values id
            _outputStream.write((int)StringOptions.DEFAULT.id());
            writeValue(getDefault(), _outputStream);
        }

        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }
}
