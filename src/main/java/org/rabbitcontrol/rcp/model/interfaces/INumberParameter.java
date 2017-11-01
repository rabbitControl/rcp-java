package org.rabbitcontrol.rcp.model.interfaces;

public interface INumberParameter<T extends Number> extends IValueParameter<T> {

    @Override
    INumberDefinition<T> getTypeDefinition();
}
