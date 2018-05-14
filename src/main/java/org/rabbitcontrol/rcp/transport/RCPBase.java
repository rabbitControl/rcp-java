package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.RCPCacheOperator;
import org.rabbitcontrol.rcp.model.RCPCommands;
import org.rabbitcontrol.rcp.model.RCPCommands.Update;
import org.rabbitcontrol.rcp.model.RCPCommands.ValueUpdate;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.interfaces.IParameterManager;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by inx on 30/11/16.
 */
public abstract class RCPBase implements IParameterManager {

    //------------------------------------------------------------
    //
    protected final Map<Short, IParameter>
            valueCache
            = new ConcurrentHashMap<Short, IParameter>();

    protected final List<IParameter> dirtyParams = new ArrayList<IParameter>();

    // callback objects
    protected Update updateListener;

    protected ValueUpdate valueUpdateListener;

    protected RCPCommands.Error       errorListener;

    protected final GroupParameter rootGroup = new GroupParameter((short)0);

    private final ReentrantLock lock = new ReentrantLock();

    //------------------------------------------------------------
    //
    public Map<Short, IParameter> getValueCache() {

        return Collections.unmodifiableMap(valueCache);
    }

    //------------------------------------------------------------
    //
    public void setUpdateListener(final Update _listener) {

        updateListener = _listener;
    }

    public void setValueUpdateListener(final ValueUpdate _listener) {

        valueUpdateListener = _listener;
    }

    public void setErrorListener(final RCPCommands.Error _listener) {

        errorListener = _listener;
    }

    //------------------------------------------------------------
    //
    public void dumpCache() {

        for (final Entry<Short, IParameter> entry : valueCache.entrySet()) {

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
    }

    // IParameterManager
    @Override
    public IParameter getParameter(final short _id) {
        if (valueCache.containsKey(_id)) {
            return valueCache.get(_id);
        }

        return null;
    }

    @Override
    public void setParameterDirty(final IParameter _parameter) {

        if (!valueCache.containsKey(_parameter.getId())) {
            System.err.println("parameter not added - skip update...");
        }

        if (!dirtyParams.contains(_parameter)) {
            dirtyParams.add(_parameter);
        }
    }

}
