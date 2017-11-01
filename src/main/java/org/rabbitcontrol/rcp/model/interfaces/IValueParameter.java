package org.rabbitcontrol.rcp.model.interfaces;

public interface IValueParameter<T> extends IParameter {

    // mandatory
    @Override
    IDefaultDefinition<T> getTypeDefinition();

    // optional
    T getValue();

    void setValue(T _value);

    void setObjectValue(Object _value);
}
