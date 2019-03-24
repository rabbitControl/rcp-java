package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.VectorOptions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Vector2Float32Definition extends NumberDefinition<VectorBase> {

    public Vector2Float32Definition() {

        super(Datatype.VECTOR2F32, VectorBase.class);
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
    public Vector2<Float> readValue(final KaitaiStream _io) {

        return new Vector2<Float>(_io.readF4be(),
                                  _io.readF4be());
    }

    @Override
    public void writeValue(final VectorBase _value, final OutputStream _outputStream) throws
                                                                                      IOException {

        if (_value != null) {
            Vector2<Float> vec = (Vector2<Float>)_value;
            _outputStream.write(ByteBuffer.allocate(4).putFloat(vec.getX()).array());
            _outputStream.write(ByteBuffer.allocate(4).putFloat(vec.getY()).array());
        }
        else if (defaultValue != null) {
            Vector2<Float> vec = (Vector2<Float>)defaultValue;
            _outputStream.write(ByteBuffer.allocate(4).putFloat(vec.getX()).array());
            _outputStream.write(ByteBuffer.allocate(4).putFloat(vec.getY()).array());
        }
        else {
            _outputStream.write(ByteBuffer.allocate(4).putFloat(0).array());
            _outputStream.write(ByteBuffer.allocate(4).putFloat(0).array());
        }
    }
}
