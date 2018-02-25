package org.rabbitcontrol.rcp.model;

import org.rabbitcontrol.rcp.model.interfaces.IParameter;

import java.nio.ByteBuffer;
import java.util.Map;

public interface RCPCacheOperator {

    void operate(final Map<ByteBuffer, IParameter> valueCache);
}
