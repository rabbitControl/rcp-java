package org.rabbitcontrol.rcp.model.interfaces;

import org.rabbitcontrol.rcp.model.parameter.GroupParameter;

public interface IParameterManager {

    IParameter getParameter(final short _id);

    void setParameterDirty(final IParameter _parameter);

    GroupParameter getRootGroup();
}
