package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.NumberProperty;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Float32Definition extends NumberDefinition<Float> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public Float32Definition() {

        super(Datatype.FLOAT32);
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
                setDefault(_io.readF4be());
                return true;
            case MINIMUM:
                setMinimum(_io.readF4be());
                return true;
            case MAXIMUM:
                setMaximum(_io.readF4be());
                return true;
            case MULTIPLEOF:
                setMultipleof(_io.readF4be());
                return true;
        }

        return false;
    }

    @Override
    public void writeValue(final Float _value, final OutputStream _outputStream) throws
                                                                                 IOException {

        _outputStream.write(ByteBuffer.allocate(4).putFloat(_value).array());
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public void setMin(final Number _value) {
        setMinimum(_value.floatValue());
    }

    @Override
    public void setMax(final Number _value) {
        setMaximum(_value.floatValue());
    }

    @Override
    public void setMult(final Number _value) {
        setMultipleof(_value.floatValue());
    }

}