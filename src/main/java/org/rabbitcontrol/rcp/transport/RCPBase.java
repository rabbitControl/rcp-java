package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by inx on 30/11/16.
 */
public abstract class RCPBase implements RCPTransporterListener {

    //------------------------------------------------------------
    //
    private final Map<Integer, IParameter>
            valueCache
            = new ConcurrentHashMap<Integer, IParameter>();

    // callback objects
    protected RCPCommands.Update updateListener;

    private final ReentrantLock lock = new ReentrantLock();

    //------------------------------------------------------------
    //
    public Map<Integer, IParameter> getValueCache() {
        return Collections.unmodifiableMap(valueCache);
    }

    //------------------------------------------------------------
    //
    public void setUpdateListener(final RCPCommands.Update _listener) {

        updateListener = _listener;
    }

    //------------------------------------------------------------
    //
    public void dumpCache() {

        for (final Map.Entry<Integer, IParameter> entry : valueCache.entrySet()) {

            System.out.println("------");
            entry.getValue().dump();
            System.out.println();
        }
    }

    public void operateOnCache(final RCPCacheOperator _operator) {

        // single point of entry
        // locking??

        // FIXME: right??
        _operator.operate(valueCache);
//        lock.lock();
//        try {
//        } finally {
//            lock.unlock();
//        }
    }
}
