package org.rabbitcontrol.rcp.model;

import java.util.Map;

public interface RCPCacheOperator {

    void operate(final Map<Integer, RCPParameter<?>> valueCache);
}
