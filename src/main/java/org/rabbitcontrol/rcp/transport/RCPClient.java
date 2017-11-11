package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RCPCommands.Add;
import org.rabbitcontrol.rcp.model.RCPCommands.Remove;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Command;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;

import java.io.IOException;
import java.util.Map;

public class RCPClient extends RCPBase {

    private RCPTransporter  transporter;

    // callback objects
    private Add addListener;

    private Remove removeListener;

    //------------------------------------------------------------
    //
    public RCPClient(final RCPTransporter _trans) {

        setTransporter(_trans);
    }

    public void setTransporter(final RCPTransporter _transporter) {

        transporter = _transporter;

        if (transporter != null) {
            transporter.setListener(this);
        }
    }

    //------------------------------------------------------------
    //
    public void setAddListener(final Add _listener) {

        addListener = _listener;
    }

    public void setRemoveListener(final Remove _listener) {

        removeListener = _listener;
    }

    //------------------------------------------------------------
    //
    /**
     * send value updated using the transporter
     *
     * @param _value
     *      the value to updated
     */
    public void update(final IParameter _value) {

//        // updated valuecache
//        if (getValueCache().containsKey((int)_value.getId())) {
//            // get cached value
//            final IParameter parameter = getValueCache().get((int)_value.getId());
//
//            parameter.update(_value);
//        }
//        else {
//            System.out.println("value not in cache - ignore");
//        }

        if (transporter != null) {

            // transport value
            final Packet packet = new Packet(Command.UPDATE, _value);

            System.out.println("client update: " + _value.getId());
            try {
                transporter.send(Packet.serialize(packet, false));
            }
            catch (IOException _e) {
                _e.printStackTrace();
            }
        }
    }

    /**
     * send init to server
     */
    public void init() {

        operateOnCache(new RCPCacheOperator() {

            @Override
            public void operate(final Map<Integer, IParameter> valueCache) {
                // clear cache?
                valueCache.clear();
            }
        });

        if (transporter != null) {
            // send to all clients
            final Packet packet = new Packet(Command.INITIALIZE);
            try {
                transporter.send(Packet.serialize(packet, false));
            }
            catch (IOException _e) {
                _e.printStackTrace();
            }
        }
    }

    //------------------------------------------------------------
    //
    @Override
    public void received(final Packet _packet, final RCPTransporter _transporter) {

        if (_packet == null) {
            System.err.println("no packet...");
            return;
        }

        if ((_packet.getCmd() == Command.ADD) ||
            (_packet.getCmd() == Command.UPDATE) ||
            (_packet.getCmd() == Command.REMOVE)) {

            _update(_packet);
        }
        else if (_packet.getCmd() == Command.VERSION) {

            // try to convert to version object
            System.out.println("version object yet to be specified");
        }
        else {

            System.err.println("not implemented command: " + _packet.getCmd());
        }

    }

    private void _update(final Packet _packet) {

        // try to convert data to TypeDefinition
        try {

            final IParameter val = (IParameter)_packet.getData();

            switch (_packet.getCmd()) {
                case ADD:

                    operateOnCache(new RCPCacheOperator() {

                        @Override
                        public void operate(final Map<Integer, IParameter> valueCache) {
                            // added to value cache?
                            if (!valueCache.containsKey(val.getId())) {

                                valueCache.put(val.getId(), val);

                                // inform listener
                                if (addListener != null) {
                                    addListener.added(val);
                                }
                            }
                            else {
                                System.err.println("client: added: already has value with id: " +
                                                   val.getId());
                            }
                        }
                    });


                    // TODO
                    // add update listener!!

                    break;

                case REMOVE:

                    operateOnCache(new RCPCacheOperator() {

                        @Override
                        public void operate(final Map<Integer, IParameter> valueCache) {
                            if (valueCache.containsKey(val.getId())) {
                                final IParameter removed = valueCache.remove(val.getId());

                                // inform listener
                                if (removeListener != null) {
                                    removeListener.removed(removed);
                                }
                            }
                            else {
                                System.err.println("client: removed: does not know value with id: " +
                                                   val.getId());
                            }
                        }
                    });

                    break;

                case UPDATE:

                    operateOnCache(new RCPCacheOperator() {

                        @Override
                        public void operate(final Map<Integer, IParameter> valueCache) {
                            //updated value cache?
                            final IParameter cached = valueCache.get(val.getId());
                            if (cached != null) {

                                // TODO!!
                                //cached.update(val);

                                // inform listener
                                if (updateListener != null) {
                                    updateListener.updated(cached);
                                }

                            }
                            else {
                                System.err.println("client: updated: no value in value cache - " +
                                                   "ignoring");
                            }
                        }
                    });

                    break;

                default:
                    System.err.println("no such command implemented in client: " +
                                       _packet.getCmd());
            }

        }
        catch (final IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
