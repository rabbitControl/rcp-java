package org.rabbitcontrol.rcp.model;

import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.interfaces.IDefaultDefinition;

import java.io.IOException;
import java.io.OutputStream;

public abstract class DefaultDefinition<T> extends TypeDefinition implements IDefaultDefinition<T> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    // optional
    private T defaultValue;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public DefaultDefinition(final Datatype _datatype) {

        super(_datatype);
    }

    public abstract void writeValue(final T _value, final OutputStream _outputStream) throws
                                                                                      IOException;

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public T getDefault() {

        return defaultValue;
    }

    @Override
    public void setDefault(final T _default) {
        defaultValue = _default;
    }
}
