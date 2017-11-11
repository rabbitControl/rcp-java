package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.NumberOptions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class UInt16Definition extends NumberDefinition<Integer> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public UInt16Definition() {

        super(Datatype.UINT16);
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
                setDefault(_io.readU2be());
                return true;
            case MINIMUM:
                setMinimum(_io.readU2be());
                return true;
            case MAXIMUM:
                setMaximum(_io.readU2be());
                return true;
            case MULTIPLEOF:
                setMultipleof(_io.readU2be());
                return true;
        }

        return false;
    }

    @Override
    public Integer readValue(final KaitaiStream _io) {

        return _io.readU2be();
    }

    @Override
    public void writeValue(final Integer _value, final OutputStream _outputStream) throws
                                                                                 IOException {

        if (_value != null) {
            _outputStream.write(ByteBuffer.allocate(2).putShort(_value.shortValue()).array());
        } else {
            _outputStream.write(ByteBuffer.allocate(2).putShort((short)0).array());
        }
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public void setMin(final Number _value) {

        setMinimum((int)_value.shortValue());
    }

    @Override
    public void setMax(final Number _value) {

        setMaximum((int)_value.shortValue());
    }

    @Override
    public void setMult(final Number _value) {

        setMultipleof((int)_value.shortValue());
    }

}
