package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Command;

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
    protected RCPTransporter transporter;

    private final Map<Integer, RCPParameter<?>> valueCache = new ConcurrentHashMap<Integer, RCPParameter<?>>();

    // callback objects
    protected RCPCommands.Update updateListener;

    private final ReentrantLock lock = new ReentrantLock();

    //------------------------------------------------------------
    //
    public RCPBase(final RCPTransporter _trans) {

        _setTransporter(_trans);
    }

    public void setTransporter(final RCPTransporter _transporter) {

        _setTransporter(_transporter);
    }

    private void _setTransporter(final RCPTransporter _transporter) {

        transporter = _transporter;

        if (transporter != null) {
            transporter.setListener(this);
        }
    }

    public Map<Integer, RCPParameter<?>> getValueCache() {
        return Collections.unmodifiableMap(valueCache);
    }

    //------------------------------------------------------------
    //
    public void setUpdateListener(final RCPCommands.Update _listener) {

        updateListener = _listener;
    }

    //------------------------------------------------------------
    //
    /**
     * send value updated using the transporter
     *
     * @param _value
     *      the value to updated
     */
    public void update(final RCPParameter<?> _value) {

        // updated valuecache
        if (valueCache.containsKey((int)_value.getId())) {
            // get cached value
            final RCPParameter<?> parameter = valueCache.get((int)_value.getId());

            parameter.update(_value);
        }
        else {
            System.out.println("value not in cache - ignore");
        }

        if (transporter != null) {

            // transport value
            final RCPPacket packet = new RCPPacket(Command.UPDATE, _value);
            transporter.send(packet);
        }
    }

    public void dumpCache() {

        for (final Map.Entry<Integer, RCPParameter<?>> entry : valueCache.entrySet()) {

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
