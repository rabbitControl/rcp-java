package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberOptions;

import java.io.IOException;
import java.io.OutputStream;

public class UInt8Definition extends NumberDefinition<Byte> {

    public static int getUnsigned(byte _value) {
        return ((int)_value) & 0xff;
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    public UInt8Definition() {

        super(Datatype.UINT8, Byte.class);
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
                setDefault((byte)_io.readU1());
                return true;
            case MINIMUM:
                setMinimum((byte)_io.readU1());
                return true;
            case MAXIMUM:
                setMaximum((byte)_io.readU1());
                return true;
            case MULTIPLEOF:
                setMultipleof((byte)_io.readU1());
                return true;
        }

        return false;
    }

    @Override
    public Byte readValue(final KaitaiStream _io) {

        return (byte)_io.readU1();
    }

    @Override
    public void writeValue(final Byte _value, final OutputStream _outputStream) throws
                                                                                 IOException {

        if (_value != null) {
            _outputStream.write(_value);
        } else {
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

    public int getMinimumUnsigned() {
        return getUnsigned(getMinimum());
    }
    public int getMaximumUnsigned() {
        return getUnsigned(getMaximum());
    }
    public int getMultipleofUnsigned() {
        return getUnsigned(getMultipleof());
    }

//    @Override
//    public void setMinimum(final long _minimum) {
//        setMinimum((Byte)Long.valueOf(_minimum).byteValue());
//    }
}
