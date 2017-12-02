package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.*;
import org.rabbitcontrol.rcp.model.interfaces.INumberDefinition;

import java.io.IOException;
import java.io.OutputStream;

public abstract class NumberDefinition<T extends Number> extends DefaultDefinition<T> implements
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

    //------------------------------------------------------------
    //------------------------------------------------------------
    public NumberDefinition(final Datatype _datatype) {

        super(_datatype);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        // handle option scale, unit

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

    @Override
    public void write(final OutputStream _outputStream, final boolean all) throws IOException {

        // write mandatory fields and defaultValue
        _outputStream.write((int)getDatatype().id());

        //
        // default
        //
        if (getDefault() != null) {

            if (all || defaultValueChanged) {

                // use any of the default values id
                _outputStream.write((int)NumberOptions.DEFAULT.id());
                writeValue(getDefault(), _outputStream);

                if (!all) {
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

            if (all || minimumChanged) {

                _outputStream.write((int)NumberOptions.MINIMUM.id());
                writeValue(minimum, _outputStream);

                if (!all) {
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

            if (all || maximumChanged) {

                _outputStream.write((int)NumberOptions.MAXIMUM.id());
                writeValue(maximum, _outputStream);

                if (!all) {
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

            if (all || multipleofChanged) {

                _outputStream.write((int)NumberOptions.MULTIPLEOF.id());
                writeValue(multipleof, _outputStream);

                if (!all) {
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

            if (all || scaleChanged) {

                _outputStream.write((int)NumberOptions.SCALE.id());
                _outputStream.write((int)scale.id());

                if (!all) {
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

            if (all || unitChanged) {

                _outputStream.write((int)NumberOptions.UNIT.id());
                RCPParser.writeTinyString(unit, _outputStream);

                if (!all) {
                    unitChanged = false;
                }
            }
        } else if (unitChanged) {

            _outputStream.write((int)NumberOptions.UNIT.id());
            RCPParser.writeTinyString("", _outputStream);

            unitChanged = false;
        }

        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    private T toType(final Number _number) {

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

        if ((minimum == _minimum) || ((minimum != null) && minimum.equals(_minimum))) {
            return;
        }

        minimum = _minimum;
        minimumChanged = true;
    }

    @Override
    public void setMin(final Number _minimum) {

        setMinimum(toType(_minimum));
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
    }

    @Override
    public void setMax(final Number _maximum) {

        setMaximum(toType(_maximum));
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
    }

    @Override
    public void setMult(final Number _multipleof) {
        setMultipleof(toType(_multipleof));
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
    }
}
