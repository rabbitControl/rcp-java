package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RcpTypes.ParameterOptions;
import org.rabbitcontrol.rcp.model.interfaces.*;
import org.rabbitcontrol.rcp.model.types.DefaultDefinition;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public abstract class ValueParameter<T> extends Parameter implements IValueParameter<T> {


    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    private final DefaultDefinition<T> typeDefinition;

    // optional
    private T value;

    private boolean valueChanged;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public ValueParameter(final short _id, final DefaultDefinition<T> _typeDefinition) {

        super(_id, _typeDefinition);

        typeDefinition = _typeDefinition;
    }

    @Override
    public IValueParameter<T> cloneEmpty() {

        return (IValueParameter<T>)RCPFactory.createParameter(id,
                                                              typeDefinition.getDatatype());
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final ParameterOptions option = ParameterOptions.byId(_propertyId);

        switch (option) {
            case VALUE:
                setValue(typeDefinition.readValue(_io));
                return true;
        }

        return false;
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        // write mandatory id
        writeId(id, _outputStream);

        // write mandatory typeDefinition
        typeDefinition.write(_outputStream, _all);

        // write all optionals
        if (value != null) {

            if (_all || valueChanged || initialWrite) {

                _outputStream.write((int)ParameterOptions.VALUE.id());
                typeDefinition.writeValue(value, _outputStream);

                if (!_all) {
                    valueChanged = false;
                }
            }
        }
        else if (valueChanged) {

            _outputStream.write((int)ParameterOptions.VALUE.id());
            typeDefinition.writeValue(typeDefinition.getDefault(), _outputStream);

            valueChanged = false;
        }

        // write other options
        super.write(_outputStream, _all);

        // finalize parameter with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    @Override
    public void dump() {

        super.dump();

        System.out.println("value:\t\t\t" + value);
        System.out.println();
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public IDefaultDefinition<T> getTypeDefinition() {

        return typeDefinition;
    }

    @Override
    public T getValue() {

        return value;
    }

    @Override
    public void setValue(final T _value) {

        if ((value == _value) || ((value != null) && value.equals(_value))) {
            return;
        }

        value = _value;
        valueChanged = true;

        setDirty();
    }

    @Override
    public void setObjectValue(final Object _value) {

        try {
            setValue((T)_value);
        }
        catch (ClassCastException _e) {
            _e.printStackTrace();
        }

    }

    @Override
    public void update(final IParameter _parameter) {

        // check id
        if (_parameter.getId() != id) {
            return;
        }

        boolean changed = false;

        // set fields directly, no change-flag ist set!

        if (_parameter instanceof ValueParameter) {

            final Object otherValue = ((ValueParameter)_parameter).getValue();

            if (otherValue != null) {

                try {
                    value = (T)value.getClass().cast(otherValue);
                    changed = true;
                }
                catch (final ClassCastException e) {

                    if ((value instanceof Number) && (otherValue instanceof Number)) {

                        if (value instanceof Integer) {
                            value = (T)new Integer(((Number)otherValue).intValue());
                            changed = true;
                        }
                        else if (value instanceof Short) {
                            value = (T)new Short(((Number)otherValue).shortValue());
                            changed = true;
                        }
                        else if (value instanceof Byte) {
                            value = (T)new Byte(((Number)otherValue).byteValue());
                            changed = true;
                        }
                        else if (value instanceof Long) {
                            value = (T)new Long(((Number)otherValue).longValue());
                            changed = true;
                        }
                        else if (value instanceof Float) {
                            value = (T)new Float(((Number)otherValue).floatValue());
                            changed = true;
                        }
                        else if (value instanceof Double) {
                            value = (T)new Double(((Number)otherValue).doubleValue());
                            changed = true;
                        }
                    }
                    else if (value instanceof Map) {

                        //TODO: do this better

                        if (otherValue instanceof Map) {

                            ((Map)value).clear();

                            for (Object key : ((Map)otherValue).keySet()) {
                                ((Map)value).put(key, ((Map)otherValue).get(key));
                            }

                            changed = true;

                        }
                        else {
                            System.err.println("other value is not a map");
                        }

                    }
                    else {
                        System.err.println("cannot updated value from type: " +
                                           value.getClass().getName() +
                                           " other: " +
                                           otherValue.getClass().getName());
                    }
                }

            }
        }

        if (changed) {
            callValueUpdateListener();
        }

        super.update(_parameter);
    }

    @Override
    public String getStringValue() {

        return value.toString();
    }
}
