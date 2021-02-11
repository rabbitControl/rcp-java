package org.rabbitcontrol.rcp;

import org.rabbitcontrol.rcp.model.RCPCommands;
import org.rabbitcontrol.rcp.model.RCPCommands.Update;
import org.rabbitcontrol.rcp.model.RCPCommands.ValueUpdate;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.interfaces.IParameterManager;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

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

    protected RCPCommands.Error errorListener;

    protected final GroupParameter rootGroup = new GroupParameter((short)0);

    protected String applicationId = "";

    //------------------------------------------------------------
    //
    public Map<Short, IParameter> getValueCache() {

        return Collections.unmodifiableMap(valueCache);
    }

    public void dispose() {

        // remove children before clearing dirtyParams
        rootGroup.removeAllChildren();

        // remove all parameter
        valueCache.clear();
        dirtyParams.clear();

        // remove listeners
        updateListener = null;
        valueUpdateListener = null;
        errorListener = null;
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

    // IParameterManager
    @Override
    public IParameter getParameter(final short _id) {
        if (valueCache.containsKey(_id)) {
            return valueCache.get(_id);
        }

        return null;
    }

    public IParameter getParameter(final String _label) {

        for (Short key : valueCache.keySet()){
            final IParameter p = valueCache.get(key);
            if (p.getLabel().equals(_label)) {
                return p;
            }
        }

        return null;
    }

    public IParameter getParameterByUserid(final String _label) {

        for (Short key : valueCache.keySet()){
            final IParameter p = valueCache.get(key);
            if ((p.getUserid() != null) && p.getUserid().equals(_label)) {
                return p;
            }
        }

        return null;
    }

    @Override
    public void setParameterDirty(final IParameter _parameter) {

        if (!valueCache.containsKey(_parameter.getId())) {
            return;
        }

        if (!dirtyParams.contains(_parameter)) {
            dirtyParams.add(_parameter);
        }
    }

    public String getApplicationId() {

        return applicationId;
    }

    public void setApplicationId(final String _applicationId) {

        applicationId = _applicationId;
    }

    @Override
    public GroupParameter getRootGroup()
    {
        return rootGroup;
    }

}
