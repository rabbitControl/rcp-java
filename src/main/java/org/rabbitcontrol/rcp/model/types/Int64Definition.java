package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberOptions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Int64Definition extends NumberDefinition<Long> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public Int64Definition() {

        super(Datatype.INT64);
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
                setDefault(_io.readS8be());
                return true;
            case MINIMUM:
                setMinimum(_io.readS8be());
                return true;
            case MAXIMUM:
                setMaximum(_io.readS8be());
                return true;
            case MULTIPLEOF:
                setMultipleof(_io.readS8be());
                return true;
        }

        return false;
    }

    @Override
    public Long readValue(final KaitaiStream _io) {

        return _io.readS8be();
    }

    @Override
    public void writeValue(final Long _value, final OutputStream _outputStream) throws IOException {

        if (_value != null) {
            _outputStream.write(ByteBuffer.allocate(8).putLong(_value).array());
        } else {
            _outputStream.write(ByteBuffer.allocate(8).putLong(0).array());
        }
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
