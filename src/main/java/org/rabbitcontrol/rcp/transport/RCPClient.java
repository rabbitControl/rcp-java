package org.rabbitcontrol.rcp.transport;

import io.kaitai.struct.ByteBufferKaitaiStream;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RCPCommands.Add;
import org.rabbitcontrol.rcp.model.RCPCommands.Remove;
import org.rabbitcontrol.rcp.model.RCPCommands.StatusChange;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Command;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.ValueParameter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

public class RCPClient extends RCPBase implements ClientTransporterListener {

    private ClientTransporter transporter;

    /* callback objects */
    private Add addListener;

    private Remove removeListener;

    private StatusChange statusChangedListener;

    //------------------------------------------------------------
    //
    public RCPClient(final ClientTransporter _trans) {

        setTransporter(_trans);
    }

    public void setTransporter(final ClientTransporter _transporter) {

        if (transporter != null) {
            transporter.removeListener(this);
        }

        transporter = _transporter;

        if (transporter != null) {
            transporter.addListener(this);
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

    public void setStatusChangedListener(final StatusChange _listener) {

        statusChangedListener = _listener;
    }

    //------------------------------------------------------------
    //
    public void update() {
        // update all dirty params
        for (IParameter parameter : dirtyParams) {
            update(parameter);
        }

        dirtyParams.clear();
    }


    /**
     * send value updated using the transporter
     *
     * @param _value
     *         the value to updated
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
            public void operate(final Map<Short, IParameter> valueCache) {
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
    public void received(final byte[] _data) {

        try {
            final Packet _packet = Packet.parse(new ByteBufferKaitaiStream(_data));

            if (_packet == null) {
                System.err.println("no packet...");
                return;
            }

            if ((_packet.getCmd() == Command.UPDATE) ||
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
        catch (RCPDataErrorException _e) {
            _e.printStackTrace();
        }
        catch (RCPUnsupportedFeatureException _e) {
            _e.printStackTrace();
        }

    }

    private void _update(final Packet _packet) {

        // try to convert data to TypeDefinition
        try {

            final IParameter val = (IParameter)_packet.getData();

            switch (_packet.getCmd()) {

                case UPDATE:

                    operateOnCache(new RCPCacheOperator() {

                        @Override
                        public void operate(final Map<Short, IParameter> valueCache) {
                            //updated value cache?
                            final IParameter cached = valueCache.get(val.getId());
                            if (cached == null) {
                                valueCache.put(val.getId(), val);

                                // inform listener
                                if (addListener != null) {
                                    addListener.parameterAdded(val);
                                }
                            }
                            else {

                                ((ValueParameter)cached).update(val);

                                // inform listener
                                if (updateListener != null) {
                                    updateListener.parameterUpdated(cached);
                                }

                            }
                        }
                    });

                    break;

                case REMOVE:

                operateOnCache(new RCPCacheOperator() {

                        @Override
                        public void operate(final Map<Short, IParameter> valueCache) {

                            if (valueCache.containsKey(val.getId())) {
                                final IParameter
                                        removed
                                        = valueCache.remove(val.getId());

                                // inform listener
                                if (removeListener != null) {
                                    removeListener.parameterRemoved(removed);
                                }
                            }
                            else {
                                System.err.println("client: removed: does not know value with id:" +
                                                   " " +
                                                   val.getId());
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

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {

    }
}
