package org.rabbitcontrol.rcp.model.interfaces;

import java.nio.ByteBuffer;

public interface IRcpModel {

    IParameter getParameter(ByteBuffer _id);

    void setDirtyParameter(IParameter _parameter);
}
