package org.rabbitcontrol.rcp.model.interfaces;

public interface IRcpModel {

    IParameter getParameter(short _id);

    void setDirtyParameter(IParameter _parameter);
}
