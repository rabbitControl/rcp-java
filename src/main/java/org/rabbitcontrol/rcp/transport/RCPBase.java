package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.RCPCacheOperator;
import org.rabbitcontrol.rcp.model.RCPCommands;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.interfaces.IRcpModel;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by inx on 30/11/16.
 */
public abstract class RCPBase implements IRcpModel {

    //------------------------------------------------------------
    //
    protected final Map<Short, IParameter>
            valueCache
            = new ConcurrentHashMap<Short, IParameter>();

    protected final List<IParameter> groupsAndParameters = new ArrayList<IParameter>();

    protected final List<IParameter> dirtyParams = new ArrayList<IParameter>();

    // callback objects
    protected RCPCommands.Update updateListener;

    protected RCPCommands.ValueUpdate valueUpdateListener;

    protected RCPCommands.Error       errorListener;

    private final ReentrantLock lock = new ReentrantLock();

    //------------------------------------------------------------
    //
    public Map<Short, IParameter> getValueCache() {

        return Collections.unmodifiableMap(valueCache);
    }

    //------------------------------------------------------------
    //
    public void setUpdateListener(final RCPCommands.Update _listener) {

        updateListener = _listener;
    }

    public void setValueUpdateListener(final RCPCommands.ValueUpdate _listener) {

        valueUpdateListener = _listener;
    }

    public void setErrorListener(final RCPCommands.Error _listener) {

        errorListener = _listener;
    }

    //------------------------------------------------------------
    //
    public void dumpCache() {

        for (final Map.Entry<Short, IParameter> entry : valueCache.entrySet()) {

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

    // IRcpModel
    @Override
    public IParameter getParameter(final ByteBuffer _id) {
        if (valueCache.containsKey(_id)) {
            return valueCache.get(_id);
        }

        return null;
    }

    @Override
    public void setDirtyParameter(final IParameter _parameter) {
        if (!dirtyParams.contains(_parameter)) {
            dirtyParams.add(_parameter);
        }
    }

}
