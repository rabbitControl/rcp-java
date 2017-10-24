package org.rabbitcontrol.rcp.model;

import java.util.Map;

@FunctionalInterface
public interface RCPCacheOperator {

    void operate(final Map<Integer, RCPParameter<?>> valueCache);

}
