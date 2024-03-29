package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RcpTypes.ParameterOptions;
import org.rabbitcontrol.rcp.model.exceptions.*;
import org.rabbitcontrol.rcp.model.interfaces.*;
import org.rabbitcontrol.rcp.model.types.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static org.rabbitcontrol.rcp.model.RcpTypes.ParameterOptions.VALUE;

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

        this(_id, _typeDefinition, null);
    }

    public ValueParameter(final short _id, final DefaultDefinition<T> _typeDefinition, final T _value) {

        super(_id, _typeDefinition);

        typeDefinition = _typeDefinition;
        value = _value;
    }

    @Override
    public IValueParameter<T> cloneEmpty() {

        return (IValueParameter<T>)RCPFactory.createParameter(id,
                                                              typeDefinition.getDatatype());
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) throws
                                                                                  RCPDataErrorException {

        if (ParameterOptions.byId(_propertyId) == VALUE) {
            setValueInternal(typeDefinition.readValue(_io));
            return true;
        }

        return false;
    }


    @Override
    public void writeUpdateValue(final OutputStream _outputStream) throws
                                                                   IOException,
                                                                   RCPException {

        super.writeUpdateValue(_outputStream);

        typeDefinition.writeValue(value, _outputStream);
    }

    @Override
    public void writeOptions(final OutputStream _outputStream, final boolean _all) throws
                                                                            IOException,
                                                                            RCPException {

        // write all optionals
        if (value != null) {

            if (_all || valueChanged || initialWrite) {

                _outputStream.write((int)VALUE.id());
                typeDefinition.writeValue(value, _outputStream);

                if (!_all) {
                    valueChanged = false;
                }
            }
        }
        else if (valueChanged) {

            _outputStream.write((int)VALUE.id());
            typeDefinition.writeValue(typeDefinition.getDefault(), _outputStream);

            valueChanged = false;
        }

        // write all other options
        super.writeOptions(_outputStream, _all);
    }

    @Override
    public void dump() {

        super.dump();

        System.out.println("value:\t\t\t" + getStringValue());
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


    protected void setValueInternal(final T _value) {
        this.setValue(_value);
    }

    @Override
    public boolean setObjectValue(final Object _value) {
        return setObjectValue(_value, true);
    }

    private boolean setObjectValue(final Object _value, boolean _allowDirty) {

        if (_value == null) {
            return false;
        }

        try {
            if (_allowDirty)
            {
                setValue((T)_value);
                return valueChanged;
            }
            else {
                if ((value == _value) || ((value != null) && value.equals(_value))) {
                    return false;
                }

                value = (T)_value;
                return true;
            }
        }
        catch (final ClassCastException _e) {

            if ((getTypeDefinition() instanceof NumberDefinition) &&
                (_value instanceof Number)) {

                T new_value = (T)((NumberDefinition)getTypeDefinition()).convertNumberValue
                                                                                ((Number)_value);

                if (_allowDirty)
                {
                    setValue(new_value);
                    return valueChanged;
                }
                else
                {
                    if ((value == new_value) || ((value != null) && value.equals(new_value))) {
                        return false;
                    }

                    value = new_value;
                    return true;
                }
            }
            else if (value instanceof Map) {

                //TODO: do this better

                if (_value instanceof Map) {

                    ((Map)value).clear();

                    for (final Object key : ((Map)_value).keySet()) {
                        ((Map)value).put(key, ((Map)_value).get(key));
                    }

                    return true;
                }
                else {
                    System.err.println("other value is not a map");
                }

            }
            else {
                System.err.println("cannot updated value from type: " +
                                   value.getClass().getName() +
                                   " other: " +
                                   _value.getClass().getName());
            }
        }

        return false;
    }

    @Override
    public void update(final IParameter _parameter) throws RCPException {

        // check id
        if (_parameter.getId() != id) {
            throw new RCPException("id missmatch");
        }

        if (_parameter.getTypeDefinition().getDatatype() != getTypeDefinition().getDatatype()) {
            throw new RCPException("type missmatch");
        }

        boolean changed = false;

        // set fields directly, no change-flag ist set!
        if (_parameter instanceof ValueParameter) {
            changed = setObjectValue(((ValueParameter)_parameter).getValue(), false);
        }

        if (changed) {
            callValueUpdateListener();
        }

        super.update(_parameter);
    }

    @Override
    public String getStringValue() {

        if (value == null) {
            return "";
        }

        return value.toString();
    }
}
