package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.*;
import org.rabbitcontrol.rcp.model.interfaces.INumberDefinition;

import java.io.IOException;
import java.io.OutputStream;

public abstract class NumberDefinition<T extends Number> extends DefaultDefinition<T>
implements
                                                                             INumberDefinition<T> {

    public static <T extends Number> NumberDefinition<T> create(final Datatype _datatype) {

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

            // vectors
            case VECTOR2F32:
                return (NumberDefinition<T>)new Vector2Float32Definition();
            case VECTOR2I32:
                return (NumberDefinition<T>)new Vector2Int32Definition();
            case VECTOR3F32:
                return (NumberDefinition<T>)new Vector3Float32Definition();
            case VECTOR3I32:
                return (NumberDefinition<T>)new Vector3Int32Definition();
            case VECTOR4F32:
                return (NumberDefinition<T>)new Vector4Float32Definition();
            case VECTOR4I32:
                return (NumberDefinition<T>)new Vector4Int32Definition();
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
    private boolean minimumChanged;

    private T maximum;
    private boolean maximumChanged;

    private T multipleof;
    private boolean multipleofChanged;

    private NumberScale scale;
    private boolean scaleChanged;

    private String unit;
    private boolean unitChanged;

    final Class<T> typeClass;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public NumberDefinition(final Datatype _datatype, Class<T> _tClass) {

        super(_datatype);

        typeClass = _tClass;
    }

    @Override
    public boolean didChange()
    {
        return super.didChange() ||
               minimumChanged ||
               maximumChanged ||
               multipleofChanged ||
               scaleChanged ||
               unitChanged;
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        // handle option scale, unit
        // other options are handled in specific number implementations

        final NumberOptions option = NumberOptions.byId(_propertyId);

        if (option == null) {
            return false;
        }

        switch (option) {
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

    public T convertNumberValue(final Number num) {

        if (typeClass == num.getClass()) {
            return (T)num;
        }

        if (typeClass == Integer.class) {
            return (T)new Integer(num.intValue());
        }
        else if (typeClass ==  Short.class) {
            return (T)new Short(num.shortValue());
        }
        else if (typeClass ==  Byte.class) {
            return (T)new Byte(num.byteValue());
        }
        else if (typeClass ==  Long.class) {
            return (T)new Long(num.longValue());
        }
        else if (typeClass ==  Float.class) {
            return (T)new Float(num.floatValue());
        }
        else if (typeClass ==  Double.class) {
            return (T)new Double(num.doubleValue());
        }
        else if (typeClass ==  VectorBase.class) {
            throw new RuntimeException("vector not implementd");
        }
        else {
            throw new RuntimeException("number not handled");
        }
    }

    @Override
    public void writeOptions(final OutputStream _outputStream, final boolean _all) throws IOException {

        //
        // default
        //
        if (getDefault() != null) {

            if (_all || defaultValueChanged || initialWrite) {

                // use any of the default values id
                _outputStream.write((int)NumberOptions.DEFAULT.id());
                writeValue(getDefault(), _outputStream);

                if (!_all) {
                    defaultValueChanged = false;
                }
            }
        } else if (defaultValueChanged) {

            _outputStream.write((int)NumberOptions.DEFAULT.id());
            writeValue(null, _outputStream);

            defaultValueChanged = false;
        }


        // write other options

        //
        // minimum
        //
        if (getMinimum() != null) {

            if (_all || minimumChanged || initialWrite) {

                _outputStream.write((int)NumberOptions.MINIMUM.id());
                writeValue(minimum, _outputStream);

                if (!_all) {
                    minimumChanged = false;
                }
            }
        } else if (minimumChanged) {

            _outputStream.write((int)NumberOptions.MINIMUM.id());
            writeValue(null, _outputStream);

            minimumChanged = false;
        }

        //
        // maximum
        //
        if (getMaximum() != null) {

            if (_all || maximumChanged || initialWrite) {

                _outputStream.write((int)NumberOptions.MAXIMUM.id());
                writeValue(maximum, _outputStream);

                if (!_all) {
                    maximumChanged = false;
                }
            }
        } else if (maximumChanged) {

            _outputStream.write((int)NumberOptions.MAXIMUM.id());
            writeValue(null, _outputStream);

            maximumChanged = false;
        }

        //
        // multipleof
        //
        if (getMultipleof() != null) {

            if (_all || multipleofChanged || initialWrite) {

                _outputStream.write((int)NumberOptions.MULTIPLEOF.id());
                writeValue(multipleof, _outputStream);

                if (!_all) {
                    multipleofChanged = false;
                }
            }
        } else if (multipleofChanged) {

            _outputStream.write((int)NumberOptions.MULTIPLEOF.id());
            writeValue(null, _outputStream);

            multipleofChanged = false;
        }

        //
        // scale
        //
        if (scale != null) {

            if (_all || scaleChanged || initialWrite) {

                _outputStream.write((int)NumberOptions.SCALE.id());
                _outputStream.write((int)scale.id());

                if (!_all) {
                    scaleChanged = false;
                }
            }
        } else if (scaleChanged) {

            _outputStream.write((int)NumberOptions.SCALE.id());
            _outputStream.write((int)NumberScale.LINEAR.id());

            scaleChanged = false;
        }

        //
        // unit
        //
        if (unit != null) {

            if (_all || unitChanged || initialWrite) {

                _outputStream.write((int)NumberOptions.UNIT.id());
                RCPParser.writeTinyString(unit, _outputStream);

                if (!_all) {
                    unitChanged = false;
                }
            }
        } else if (unitChanged) {

            _outputStream.write((int)NumberOptions.UNIT.id());
            RCPParser.writeTinyString("", _outputStream);

            unitChanged = false;
        }

    }

//    private T toType(final Number _number) {
//
//
//        if (getDefault() instanceof Byte) {
//            return (T)(Byte)_number.byteValue();
//        }
//        else if (getDefault() instanceof Short) {
//            return (T)(Short)_number.shortValue();
//        }
//        else if (getDefault() instanceof Integer) {
//            return (T)(Integer)_number.intValue();
//        }
//        else if (getDefault() instanceof Long) {
//            return (T)(Long)_number.longValue();
//        }
//        else if (getDefault() instanceof Float) {
//            return (T)(Float)_number.floatValue();
//        }
//        else if (getDefault() instanceof Byte) {
//            return (T)(Double)_number.doubleValue();
//        }
//
//        throw new NumberFormatException();
//    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public T getMinimum() {

        return minimum;
    }

    @Override
    public void setMinimum(final T _minimum) {

        if ((minimum == _minimum) || ((minimum != null) && minimum.equals(_minimum))) {
            return;
        }

        minimum = _minimum;
        minimumChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }


    @Override
    public T getMaximum() {

        return maximum;
    }

    @Override
    public void setMaximum(final T _maximum) {

        if ((maximum == _maximum) || ((maximum != null) && maximum.equals(_maximum))) {
            return;
        }

        maximum = _maximum;
        maximumChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    @Override
    public T getMultipleof() {

        return multipleof;
    }

    @Override
    public void setMultipleof(final T _multipleof) {

        if ((multipleof == _multipleof) || (multipleof != null && multipleof.equals(_multipleof))) {
            return;
        }

        multipleof = _multipleof;
        multipleofChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    @Override
    public NumberScale getScale() {

        return scale;
    }

    @Override
    public void setScale(final NumberScale _scale) {

        if ((scale == _scale) || ((scale != null) && scale.equals(_scale))) {
            return;
        }

        scale = _scale;
        scaleChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    @Override
    public String getUnit() {

        return unit;
    }

    @Override
    public void setUnit(final String _unit) {

        if ((unit == _unit) || ((unit != null) && unit.equals(_unit))) {
            return;
        }

        unit = _unit;
        unitChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    @Override
    public void dump() {

        super.dump();
        System.out.println("minimum: " + getMinimum());
        System.out.println("maximum: " + getMaximum());
        System.out.println("multipleof: " + getMultipleof());
        System.out.println("scale: " + getScale());
        System.out.println("unit: " + getUnit());
    }
}
