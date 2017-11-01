package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.NumberProperty;

import java.io.IOException;
import java.io.OutputStream;

public class UInt8Definition extends NumberDefinition<Short> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public UInt8Definition() {

        super(Datatype.UINT8);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        if (super.handleOption(_propertyId, _io)) {
            // already handled
            return true;
        }

        NumberProperty property = NumberProperty.byId(_propertyId);

        if (property == null) {
            return false;
        }

        switch (property) {
            case DEFAULT:
                setDefault((short)_io.readU1());
                return true;
            case MINIMUM:
                setMinimum((short)_io.readU1());
                return true;
            case MAXIMUM:
                setMaximum((short)_io.readU1());
                return true;
            case MULTIPLEOF:
                setMultipleof((short)_io.readU1());
                return true;
        }

        return false;
    }

    @Override
    public void writeValue(final Short _value, final OutputStream _outputStream) throws
                                                                                 IOException {

        _outputStream.write(_value.byteValue());
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public void setMin(final Number _value) {

        setMinimum((short)_value.byteValue());
    }

    @Override
    public void setMax(final Number _value) {

        setMaximum((short)_value.byteValue());
    }

    @Override
    public void setMult(final Number _value) {

        setMultipleof((short)_value.byteValue());
    }

}
