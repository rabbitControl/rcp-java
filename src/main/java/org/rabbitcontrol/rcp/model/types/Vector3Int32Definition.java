package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.VectorOptions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Vector3Int32Definition extends NumberDefinition<VectorBase> {

    public Vector3Int32Definition() {

        super(Datatype.VECTOR3I32, VectorBase.class);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        if (super.handleOption(_propertyId, _io)) {
            // already handled
            return true;
        }

        VectorOptions property = VectorOptions.byId(_propertyId);

        if (property == null) {
            return false;
        }

        switch (property) {
            case DEFAULT: {
                setDefault(readValue(_io));
                return true;
            }
            case MINIMUM: {
                setMinimum(readValue(_io));
                return true;
            }
            case MAXIMUM: {
                setMaximum(readValue(_io));
                return true;
            }
            case MULTIPLEOF: {
                setMultipleof(readValue(_io));
                return true;
            }
        }

        return false;
    }

    @Override
    public void setMin(final Number _minimum) {

    }

    @Override
    public void setMax(final Number _maximum) {

    }

    @Override
    public void setMult(final Number _multipleof) {

    }

    @Override
    public Vector3<Integer> readValue(final KaitaiStream _io) {

        return new Vector3<Integer>(_io.readS4be(),
                                  _io.readS4be(),
                                  _io.readS4be());
    }

    @Override
    public void writeValue(final VectorBase _value, final OutputStream _outputStream) throws
                                                                                      IOException {

        if (_value != null) {
            Vector3<Integer> vec = (Vector3<Integer>)_value;
            _outputStream.write(ByteBuffer.allocate(4).putInt(vec.getX()).array());
            _outputStream.write(ByteBuffer.allocate(4).putInt(vec.getY()).array());
            _outputStream.write(ByteBuffer.allocate(4).putInt(vec.getZ()).array());
        }
        else if (defaultValue != null) {
            Vector3<Integer> vec = (Vector3<Integer>)defaultValue;
            _outputStream.write(ByteBuffer.allocate(4).putInt(vec.getX()).array());
            _outputStream.write(ByteBuffer.allocate(4).putInt(vec.getY()).array());
            _outputStream.write(ByteBuffer.allocate(4).putInt(vec.getZ()).array());
        }
        else {
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());
        }
    }
}
