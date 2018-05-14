package org.rabbitcontrol.rcp.model.interfaces;

import org.rabbitcontrol.rcp.model.parameter.GroupParameter;

public interface IParameterChild {

    void setParent(final GroupParameter _parent);

    void setManager(final IParameterManager _model);
}
