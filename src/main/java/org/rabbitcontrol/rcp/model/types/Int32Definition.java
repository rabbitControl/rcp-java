package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberOptions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Int32Definition extends NumberDefinition<Integer> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public Int32Definition() {

        super(Datatype.INT32, Integer.class);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        if (super.handleOption(_propertyId, _io)) {
            // already handled
            return true;
        }

        final NumberOptions option = NumberOptions.byId(_propertyId);

        if (option == null) {
            return false;
        }

        switch (option) {
            case DEFAULT:
                setDefault(_io.readS4be());
                return true;
            case MINIMUM:
                setMinimum(_io.readS4be());
                return true;
            case MAXIMUM:
                setMaximum(_io.readS4be());
                return true;
            case MULTIPLEOF:
                setMultipleof(_io.readS4be());
                return true;
        }

        return false;
    }

    @Override
    public Integer readValue(final KaitaiStream _io) {

        return _io.readS4be();
    }

    @Override
    public void writeValue(final Integer _value, final OutputStream _outputStream) throws
                                                                                   IOException {

        if (_value != null) {
            _outputStream.write(ByteBuffer.allocate(4).putInt(_value).array());
        }
        else if (defaultValue != null) {
            _outputStream.write(ByteBuffer.allocate(4).putInt(defaultValue).array());
        }
        else {
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());
        }
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public void setMin(final Number _value) {

        setMinimum(_value.intValue());
    }

    @Override
    public void setMax(final Number _value) {

        setMaximum(_value.intValue());
    }

    @Override
    public void setMult(final Number _value) {

        setMultipleof(_value.intValue());
    }

}
