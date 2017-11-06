package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.NumberOptions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class UInt32Definition extends NumberDefinition<Long> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public UInt32Definition() {

        super(Datatype.UINT32);
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
                setDefault(_io.readU4be());
                return true;
            case MINIMUM:
                setMinimum(_io.readU4be());
                return true;
            case MAXIMUM:
                setMaximum(_io.readU4be());
                return true;
            case MULTIPLEOF:
                setMultipleof(_io.readU4be());
                return true;
        }

        return false;
    }

    @Override
    public Long readValue(final KaitaiStream _io) {

        return _io.readU4be();
    }

    @Override
    public void writeValue(final Long _value, final OutputStream _outputStream) throws
                                                                                 IOException {

        _outputStream.write(ByteBuffer.allocate(4).putInt(_value.intValue()).array());
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public void setMin(final Number _value) {

        setMinimum((long)_value.intValue());
    }

    @Override
    public void setMax(final Number _value) {

        setMaximum((long)_value.intValue());
    }

    @Override
    public void setMult(final Number _value) {

        setMultipleof((long)_value.intValue());
    }

}
