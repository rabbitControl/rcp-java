package org.rabbitcontrol.rcp.model.parameter;

import io.netty.util.internal.ConcurrentSet;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.ParameterOptions;
import org.rabbitcontrol.rcp.model.interfaces.IDefaultDefinition;
import org.rabbitcontrol.rcp.model.interfaces.IValueParameter;
import org.rabbitcontrol.rcp.model.types.DefaultDefinition;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
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
    private T value;
    private boolean valueChanged;

    //------------------------
    // change listener

    private final Set<VALUE_CHANGED<T>> valueChangeListener = new ConcurrentSet<VALUE_CHANGED<T>>();


    //------------------------------------------------------------
    //------------------------------------------------------------
    public ValueParameter(final int _id, final DefaultDefinition<T> _typeDefinition) {

        super(_id, _typeDefinition);

        typeDefinition = _typeDefinition;
    }

    @Override
    public IValueParameter<T> cloneEmpty() {
        return (IValueParameter<T>)ParameterFactory.createParameter((int)id, typeDefinition.getDatatype());
    }


    @Override
    public void write(final OutputStream _outputStream, final boolean all) throws IOException {

        // write mandatory id
        _outputStream.write(ByteBuffer.allocate(4).putInt((int)id).array());

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
        } else if (valueChanged) {

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
        } catch (ClassCastException _e) {
            _e.printStackTrace();
        }

    }
}
