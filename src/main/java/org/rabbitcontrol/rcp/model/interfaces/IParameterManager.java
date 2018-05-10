package org.rabbitcontrol.rcp.model.interfaces;

public interface IParameterManager {

    IParameter getParameter(final short _id);

    void setParameterDirty(final IParameter _parameter);
}
