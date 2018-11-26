package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.BooleanOptions;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;

import java.io.IOException;
import java.io.OutputStream;

public class BooleanDefinition extends DefaultDefinition<Boolean> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public BooleanDefinition() {

        super(Datatype.BOOLEAN);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final BooleanOptions option = BooleanOptions.byId(_propertyId);

        if (option == null) {
            return false;
        }

        switch (option) {
            case DEFAULT:
                setDefault(readValue(_io));
                return true;
        }

        return false;
    }

    @Override
    public Boolean readValue(final KaitaiStream _io) {

        return _io.readS1() != 0;
    }

    @Override
    public void writeValue(final Boolean _value, final OutputStream _outputStream) throws
                                                                                   IOException {

        if (_value != null) {
            _outputStream.write(_value ? 1 : 0);
        }
        else if (defaultValue != null) {
            _outputStream.write(defaultValue ? 1 : 0);
        }
        else {
            _outputStream.write(0);
        }

    }

    @Override
    public void writeOptions(final OutputStream _outputStream, final boolean _all) throws
                                                                                   IOException {

        if (getDefault() != null) {

            if (_all || defaultValueChanged || initialWrite) {

                // use any of the default values id
                _outputStream.write((int)BooleanOptions.DEFAULT.id());
                writeValue(getDefault(), _outputStream);

                if (!_all) {
                    defaultValueChanged = false;
                }
            }
        }
        else if (defaultValueChanged) {

            _outputStream.write((int)BooleanOptions.DEFAULT.id());
            writeValue(null, _outputStream);

            defaultValueChanged = false;
        }

    }
}
