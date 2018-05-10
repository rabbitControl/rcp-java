package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberOptions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class UInt16Definition extends NumberDefinition<Short> {

    public static int getUnsigned(short _value) {
        return ((int) _value) & 0xffff;
    }

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
                setDefault((short)_io.readU2be());
                return true;
            case MINIMUM:
                setMinimum((short)_io.readU2be());
                return true;
            case MAXIMUM:
                setMaximum((short)_io.readU2be());
                return true;
            case MULTIPLEOF:
                setMultipleof((short)_io.readU2be());
                return true;
        }

        return false;
    }

    @Override
    public Short readValue(final KaitaiStream _io) {

        return (short)_io.readU2be();
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
