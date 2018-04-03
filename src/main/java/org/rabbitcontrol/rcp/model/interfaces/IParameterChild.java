package org.rabbitcontrol.rcp.model.interfaces;

import org.rabbitcontrol.rcp.model.parameter.GroupParameter;

public interface IParameterChild {

    void setParent(GroupParameter _parent);

    void setRcpModel(IRcpModel _model);
}
