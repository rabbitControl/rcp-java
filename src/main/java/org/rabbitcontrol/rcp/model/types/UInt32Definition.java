package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberOptions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class UInt32Definition extends NumberDefinition<Integer> {

    public static long getUnsigned(int _value) {
        return ((long) _value) & 0xffffffffL;
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    public UInt32Definition() {

        super(Datatype.UINT32, Integer.class);
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
                setDefault((int)_io.readU4be());
                return true;
            case MINIMUM:
                setMinimum((int)_io.readU4be());
                return true;
            case MAXIMUM:
                setMaximum((int)_io.readU4be());
                return true;
            case MULTIPLEOF:
                setMultipleof((int)_io.readU4be());
                return true;
        }

        return false;
    }

    @Override
    public Integer readValue(final KaitaiStream _io) {

        return (int)_io.readU4be();
    }

    @Override
    public void writeValue(final Integer _value, final OutputStream _outputStream) throws
                                                                                 IOException {

        if (_value != null) {
            _outputStream.write(ByteBuffer.allocate(4).putInt(_value).array());
        } else {
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
