package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.NumberProperty;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class UInt64Definition extends NumberDefinition<Long> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public UInt64Definition() {

        super(Datatype.UINT64);
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
                setDefault(_io.readU8be());
                return true;
            case MINIMUM:
                setMinimum(_io.readU8be());
                return true;
            case MAXIMUM:
                setMaximum(_io.readU8be());
                return true;
            case MULTIPLEOF:
                setMultipleof(_io.readU8be());
                return true;
        }

        return false;
    }

    @Override
    public void writeValue(final Long _value, final OutputStream _outputStream) throws
                                                                                 IOException {

        _outputStream.write(ByteBuffer.allocate(8).putLong(_value).array());
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public void setMin(final Number _value) {

        setMinimum(_value.longValue());
    }

    @Override
    public void setMax(final Number _value) {

        setMaximum(_value.longValue());
    }

    @Override
    public void setMult(final Number _value) {

        setMultipleof(_value.longValue());
    }

}