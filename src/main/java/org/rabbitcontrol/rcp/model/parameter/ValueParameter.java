package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import io.netty.util.internal.ConcurrentSet;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.ParameterOptions;
import org.rabbitcontrol.rcp.model.interfaces.*;
import org.rabbitcontrol.rcp.model.types.DefaultDefinition;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;

public abstract class ValueParameter<T> extends Parameter implements IValueParameter<T> {

    //------------------------------------------------------------
    // interfaces
    public interface VALUE_CHANGED<T> {

        void valueChanged(final T newValue);
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    private final DefaultDefinition<T> typeDefinition;

    // optional
    private T       value;

    private boolean valueChanged;

    //------------------------
    // change listener

    private final Set<VALUE_CHANGED<T>> valueChangeListener = new ConcurrentSet<VALUE_CHANGED<T>>();

    //------------------------------------------------------------
    //------------------------------------------------------------
    public ValueParameter(final ByteBuffer _id, final DefaultDefinition<T> _typeDefinition) {

        super(_id, _typeDefinition);

        typeDefinition = _typeDefinition;
    }

    @Override
    public IValueParameter<T> cloneEmpty() {

        return (IValueParameter<T>)ParameterFactory.createParameter(id,
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
    public void write(final OutputStream _outputStream, final boolean all) throws IOException {

        // write mandatory id
        _outputStream.write(id.array().length);
        _outputStream.write(id.array());

        // write mandatory typeDefinition
        typeDefinition.write(_outputStream, all);

        // write all optionals
        if (value != null) {

            if (all || valueChanged) {

                _outputStream.write((int)ParameterOptions.VALUE.id());
                typeDefinition.writeValue(value, _outputStream);

                if (!all) {
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
        super.write(_outputStream, all);

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
    // listener

    /*
        VALUE_CHANGED listener
     */
    public void addValueChangeListener(final VALUE_CHANGED<T> _listener) {

        if (!valueChangeListener.contains(_listener)) {
            valueChangeListener.add(_listener);
        }
    }

    public void removeValueChangeListener(final VALUE_CHANGED<T> _listener) {

        if (valueChangeListener.contains(_listener)) {
            valueChangeListener.remove(_listener);
        }
    }

    public void clearValueChangeListener() {

        valueChangeListener.clear();
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

        for (final VALUE_CHANGED<T> value_changed : valueChangeListener) {
            value_changed.valueChanged(value);
        }
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

        // TODO: figure out if we need to check id/datatype before setting data
        //        if (id != _parameter.getId()) {
        //            System.err.println("don't updated unmatching id");
        //            return;
        //        }
        //
        //        // compare datatypes...
        //        if ((_parameter.getTypeDefinition() != null) &&
        //            (typeDefinition.getDatatype() != _parameter.getTypeDefinition().getDatatype
        // ())) {
        //            System.err.println("not updated unmatching types: " +
        //                               typeDefinition.getDatatype() +
        //                               " != " +
        //                               _parameter.getTypeDefinition().getDatatype());
        //            return;
        //        }

        // set fields directly, no change-flag ist set!

        if (_parameter instanceof ValueParameter) {

            final Object otherValue = ((ValueParameter)_parameter).getValue();

            if (otherValue != null) {

                try {
                    value = (T)value.getClass().cast(otherValue);
                }
                catch (final ClassCastException e) {

                    if ((value instanceof Number) && (otherValue instanceof Number)) {

                        if (value instanceof Integer) {
                            value = (T)new Integer(((Number)otherValue).intValue());
                        }
                        else if (value instanceof Short) {
                            value = (T)new Short(((Number)otherValue).shortValue());
                        }
                        else if (value instanceof Byte) {
                            value = (T)new Byte(((Number)otherValue).byteValue());
                        }
                        else if (value instanceof Long) {
                            value = (T)new Long(((Number)otherValue).longValue());
                        }
                        else if (value instanceof Float) {
                            value = (T)new Float(((Number)otherValue).floatValue());
                        }
                        else if (value instanceof Double) {
                            value = (T)new Double(((Number)otherValue).doubleValue());
                        }

                    }
                    else if (value instanceof Map) {

                        //TODO: do this better

                        if (otherValue instanceof Map) {

                            ((Map)value).clear();

                            for (Object key : ((Map)otherValue).keySet()) {
                                ((Map)value).put(key, ((Map)otherValue).get(key));
                            }

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

        super.update(_parameter);
    }
}
