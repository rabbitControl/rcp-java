package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberOptions;

import java.io.IOException;
import java.io.OutputStream;

public class Int8Definition extends NumberDefinition<Byte> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public Int8Definition() {

        super(Datatype.INT8, Byte.class);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        if (super.handleOption(_propertyId, _io)) {
            // already handled
            return true;
        }

        NumberOptions option = NumberOptions.byId(_propertyId);

        if (option == null) {
            return false;
        }

        switch (option) {
            case DEFAULT:
                setDefault(_io.readS1());
                return true;
            case MINIMUM:
                setMinimum(_io.readS1());
                return true;
            case MAXIMUM:
                setMaximum(_io.readS1());
                return true;
            case MULTIPLEOF:
                setMultipleof(_io.readS1());
                return true;
        }

        return false;
    }

    @Override
    public Byte readValue(final KaitaiStream _io) {

        return _io.readS1();
    }

    @Override
    public void writeValue(final Byte _value, final OutputStream _outputStream) throws IOException {

        if (_value != null) {
            _outputStream.write(_value);
        }
        else if (defaultValue != null) {
            _outputStream.write(defaultValue);
        }
        else {
            _outputStream.write(0);
        }

    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public void setMin(final Number _value) {

        setMinimum(_value.byteValue());
    }

    @Override
    public void setMax(final Number _value) {

        setMaximum(_value.byteValue());
    }

    @Override
    public void setMult(final Number _value) {

        setMultipleof(_value.byteValue());
    }

}
