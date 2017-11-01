package org.rabbitcontrol.rcp.model.interfaces;

public interface IDefaultDefinition<T> extends ITypeDefinition {

    T getDefault();

    void setDefault(T _default);
}
