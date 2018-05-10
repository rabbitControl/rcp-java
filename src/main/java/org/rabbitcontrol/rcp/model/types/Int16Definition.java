package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberOptions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Int16Definition extends NumberDefinition<Short> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public Int16Definition() {

        super(Datatype.INT16);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        if (super.handleOption(_propertyId, _io)) {
            // already handled
            return true;
        }

        NumberOptions property = NumberOptions.byId(_propertyId);

        if (property == null) {
            return false;
        }

        switch (property) {
            case DEFAULT:
                setDefault(_io.readS2be());
                return true;
            case MINIMUM:
                setMinimum(_io.readS2be());
                return true;
            case MAXIMUM:
                setMaximum(_io.readS2be());
                return true;
            case MULTIPLEOF:
                setMultipleof(_io.readS2be());
                return true;
        }

        return false;
    }

    @Override
    public Short readValue(final KaitaiStream _io) {

        return _io.readS2be();
    }

    @Override
    public void writeValue(final Short _value, final OutputStream _outputStream) throws
                                                                                 IOException {

        if (_value != null) {
            _outputStream.write(ByteBuffer.allocate(2).putShort(_value).array());
        } else {
            _outputStream.write(ByteBuffer.allocate(2).putShort((short)0).array());
        }
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public void setMin(final Number _value) {

        setMinimum(_value.shortValue());
    }

    @Override
    public void setMax(final Number _value) {

        setMaximum(_value.shortValue());
    }

    @Override
    public void setMult(final Number _value) {

        setMultipleof(_value.shortValue());
    }

}
