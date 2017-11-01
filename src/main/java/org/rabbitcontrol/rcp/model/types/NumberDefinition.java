package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.gen.RcpTypes;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.*;
import org.rabbitcontrol.rcp.model.interfaces.INumberDefinition;
import org.rabbitcontrol.rcp.model.DefaultDefinition;

import java.io.IOException;
import java.io.OutputStream;

public abstract class NumberDefinition<T extends Number> extends DefaultDefinition<T> implements
                                                                             INumberDefinition<T> {

    public static <T extends Number> NumberDefinition<T> create(Datatype _datatype) {

        switch (_datatype) {

            case INT8:
                return (NumberDefinition<T>)new Int8Definition();
            case UINT8:
                return (NumberDefinition<T>)new UInt8Definition();
            case INT16:
                return (NumberDefinition<T>)new Int16Definition();
            case UINT16:
                return (NumberDefinition<T>)new UInt16Definition();
            case INT32:
                return (NumberDefinition<T>)new Int32Definition();
            case UINT32:
                return (NumberDefinition<T>)new UInt32Definition();
            case INT64:
                return (NumberDefinition<T>)new Int64Definition();
            case UINT64:
                return (NumberDefinition<T>)new UInt64Definition();
            case FLOAT32:
                return (NumberDefinition<T>)new Float32Definition();
            case FLOAT64:
                return (NumberDefinition<T>)new Float64Definition();

        }

        throw new RuntimeException("datatype not handled");
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
//    public static void parseOption(
//            final NumberDefinition<?> _typeDefinition,
//            final NumberProperty _dataid,
//            final KaitaiStream _io) throws RCPDataErrorException {
//
//        switch (_dataid) {
//
//            case SCALE:
//                _typeDefinition.setScale(NumberScale.byId(_io.readU1()));
//                break;
//
//            case UNIT:
//                final TinyString tinyString = new TinyString(_io);
//                _typeDefinition.setUnit(tinyString.data());
//                break;
//
//            default:
//                // not a number data id!!
//                throw new RCPDataErrorException();
//        }
//    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    // optional
    //----------------------------------------------------
    private T minimum;

    private T maximum;

    private T multipleof;

    private NumberScale scale;

    private String unit;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public NumberDefinition(final Datatype _datatype) {

        super(_datatype);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        // handle option scale, unit

        RcpTypes.NumberProperty property = RcpTypes.NumberProperty.byId(_propertyId);

        if (property == null) {
            return false;
        }

        switch (property) {
            case SCALE:
                setScale(NumberScale.byId(_io.readU1()));
                return true;
            case UNIT:
                final TinyString tinyString = new TinyString(_io);
                setUnit(tinyString.data());
                return true;
        }

        return false;
    }

    @Override
    public void writeValue(final T _value, final OutputStream _outputStream) throws IOException {
        // nop
        throw new RuntimeException("not implemented");
    }

    @Override
    public void write(final OutputStream _outputStream) throws IOException {

        // write mandatory fields and defaultValue
        _outputStream.write((int)getDatatype().id());

        if (getDefault() != null) {
            // use any of the default values id
            _outputStream.write((int)RcpTypes.BooleanProperty.DEFAULT.id());
            writeValue(getDefault(), _outputStream);
        }

        // write other options
        if (getMinimum() != null) {
            _outputStream.write((int)NumberProperty.MINIMUM.id());
            writeValue(minimum, _outputStream);
        }

        if (getMaximum() != null) {
            _outputStream.write((int)NumberProperty.MAXIMUM.id());
            writeValue(maximum, _outputStream);
        }

        if (getMultipleof() != null) {
            _outputStream.write((int)NumberProperty.MULTIPLEOF.id());
            writeValue(multipleof, _outputStream);
        }

        if (scale != null) {
            _outputStream.write((int)NumberProperty.SCALE.id());
            _outputStream.write((int)scale.id());
        }

        if (unit != null) {
            _outputStream.write((int)NumberProperty.UNIT.id());
            RCPParser.writeTinyString(unit, _outputStream);
        }

        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    private T toType(Number _number) {

        if (getDefault() instanceof Byte) {
            return (T)(Byte)_number.byteValue();
        }
        else if (getDefault() instanceof Short) {
            return (T)(Short)_number.shortValue();
        }
        else if (getDefault() instanceof Integer) {
            return (T)(Integer)_number.intValue();
        }
        else if (getDefault() instanceof Long) {
            return (T)(Long)_number.longValue();
        }
        else if (getDefault() instanceof Float) {
            return (T)(Float)_number.floatValue();
        }
        else if (getDefault() instanceof Byte) {
            return (T)(Double)_number.doubleValue();
        }

        throw new NumberFormatException();
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public T getMinimum() {

        return minimum;
    }

    @Override
    public void setMinimum(final T _minimum) {

        minimum = _minimum;
    }

    @Override
    public void setMin(final Number _minimum) {

        minimum = toType(_minimum);
    }

    @Override
    public T getMaximum() {

        return maximum;
    }

    @Override
    public void setMaximum(final T _maximum) {

        maximum = _maximum;
    }

    @Override
    public void setMax(final Number _maximum) {

        maximum = toType(_maximum);
    }

    @Override
    public T getMultipleof() {

        return multipleof;
    }

    @Override
    public void setMultipleof(final T _multipleof) {

        multipleof = _multipleof;
    }

    @Override
    public void setMult(final Number _multipleof) {

        multipleof = toType(_multipleof);
    }

    @Override
    public NumberScale getScale() {

        return scale;
    }

    @Override
    public void setScale(final NumberScale _scale) {

        scale = _scale;
    }

    @Override
    public String getUnit() {

        return unit;
    }

    @Override
    public void setUnit(final String _unit) {

        unit = _unit;
    }
}
