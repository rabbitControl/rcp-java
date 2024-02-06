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

    protected final List<IParameter> dirtyParams =
            Collections.synchronizedList(new ArrayList<IParameter>());

    // callback objects
    protected final Set<Update> updateListener = new HashSet<Update>();

    protected final Set<ValueUpdate> valueUpdateListener = new HashSet<ValueUpdate>();

    protected final Set<RCPCommands.Error> errorListener = new HashSet<RCPCommands.Error>();

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

        // clear listeners
        updateListener.clear();
        valueUpdateListener.clear();
        errorListener.clear();
    }

    //------------------------------------------------------------
    //

    /**
     * Set Update listener
     *
     * @deprecated please use addUpdateListener() instead.
     * @param _listener the Update listener to set.
     */
    @Deprecated
    public void setUpdateListener(final Update _listener) {

        addUpdateListener(_listener);
    }

    /**
     * Add Update listener
     *
     * @param _listener the Update listener to add.
     */
    public void addUpdateListener(final Update _listener) {
        if (!updateListener.contains(_listener))
        {
            updateListener.add(_listener);
        }
    }

    /**
     * Remove Update listener
     *
     * @param _listener the Update listener to remove.
     */
    public void removeUpdateListener(final Update _listener) {
        if (updateListener.contains(_listener)) {
            updateListener.remove(_listener);
        }
    }

    /**
     * Set ValueUpdate listener
     *
     * @deprecated please use addValueUpdateListener() instead.
     * @param _listener the ValueUpdate listener to set.
     */
    @Deprecated
    public void setValueUpdateListener(final ValueUpdate _listener) {

        addValueUpdateListener(_listener);
    }

    /**
     * Add ValueUpdate listener
     *
     * @param _listener the ValueUpdate listener to add.
     */
    public void addValueUpdateListener(final ValueUpdate _listener) {
        if (!valueUpdateListener.contains(_listener)) {
            valueUpdateListener.add(_listener);
        }
    }

    /**
     * Remove ValueUpdate listener
     *
     * @param _listener the ValueUpdate listener to remove.
     */
    public void removeValueUpdateListener(final ValueUpdate _listener) {
        if (valueUpdateListener.contains(_listener)) {
            valueUpdateListener.remove(_listener);
        }
    }

    /**
     * Set Error listener
     *
     * @deprecated please use addErrorListener() instead.
     * @param _listener the Error listener to set.
     */
    @Deprecated
    public void setErrorListener(final RCPCommands.Error _listener) {

        addErrorListener(_listener);
    }

    /**
     * Add Error listener
     *
     * @param _listener the Error listener to add.
     */
    public void addErrorListener(final RCPCommands.Error _listener) {
        if (!errorListener.contains(_listener)) {
            errorListener.add(_listener);
        }
    }

    /**
     * Remove Error listener
     *
     * @param _listener the Error listener to remove.
     */
    public void removeErrorListener(final RCPCommands.Error _listener) {
        if (errorListener.contains(_listener)) {
            errorListener.remove(_listener);
        }
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

        synchronized (dirtyParams)
        {
            if (!dirtyParams.contains(_parameter)) {
                dirtyParams.add(_parameter);
            }
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
