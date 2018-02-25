package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RCPCommands.Init;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Command;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by inx on 30/11/16.
 */
public class RCPServer extends RCPBase {

    private final List<RCPTransporter> transporterList = new ArrayList<RCPTransporter>();

    //------------------------------------------------------------
    // callback objects
    private Init initListener;

    //------------------------------------------------------------
    //
    public RCPServer(final RCPTransporter _transporter) {

        addTransporter(_transporter);
    }

    public void addTransporter(final RCPTransporter _transporter) {

        if (!transporterList.contains(_transporter)) {
            transporterList.add(_transporter);
            _transporter.setListener(this);
        }
    }

    public void removeTransporter(final RCPTransporter _transporter) {

        if (transporterList.contains(_transporter)) {
            transporterList.remove(_transporter);

            // TODO: shutdown transporter
        }
    }

    //------------------------------------------------------------
    //
    public void setInitListener(final Init _listener) {

        initListener = _listener;
    }

    //------------------------------------------------------------
    //
    public void addParameter(final IParameter _value) {

        if (valueCache.containsKey(_value.getId())) {

            if (valueCache.get(_value.getId()) != _value) {
                System.err.println("different object with same ID!!!");
            }
            else {
                System.out.println("already added value with this id - ignore");
            }

            return;
        }

        operateOnCache(new RCPCacheOperator() {

            @Override
            public void operate(final Map<ByteBuffer, IParameter> valueCache) {

                if (!valueCache.containsKey(_value.getId())) {
                    // added
                    valueCache.put(_value.getId(), _value);
                }
//                else {
//
//                    if (valueCache.get(_value.getId()) != _value) {
//                        System.err.println("different object with same ID!!!");
//                    }
//                    else {
//                        System.out.println("already added value with this id - ignore");
//                    }
//                }
            }
        });


        // register callbacks
//        final Parameter parameter = (Parameter)_value;
//
//        parameter.addLabelChangeListener(new LABEL_CHANGED() {
//
//            @Override
//            public void labelChanged(final String newValue) {
//
//                final IParameter emptyParam = parameter.cloneEmpty();
//                emptyParam.setLabel(newValue);
//
//                // send update
//                update(emptyParam);
//            }
//        });
//
//        parameter.addDescriptionChangedListener(new DESCRIPTION_CHANGED() {
//
//            @Override
//            public void descriptionChanged(final String newValue) {
//
//                final IParameter emptyParam = parameter.cloneEmpty();
//                emptyParam.setDescription(newValue);
//
//                // send update
//                update(emptyParam);
//            }
//        });
//
//
//        if (parameter instanceof ValueParameter) {
//
//            ((ValueParameter)parameter).addValueChangeListener(new VALUE_CHANGED() {
//
//                @Override
//                public void valueChanged(final Object newValue) {
//
//                    final IValueParameter<?> emptyParam = ((ValueParameter)parameter).cloneEmpty();
//                    emptyParam.setObjectValue(newValue);
//
//                    update(emptyParam);
//                }
//            });
//
//        }


        // send add to all
        if (!transporterList.isEmpty()) {
            // TODO: send to all clients
            final Packet packet = new Packet(Command.ADD, _value);
            try {
                final byte[] data = Packet.serialize(packet, true);

                for (final RCPTransporter transporter : transporterList) {
                    transporter.send(data);
                }
            }
            catch (IOException _e) {
                _e.printStackTrace();
            }
        }
    }

    public void updateParameters(final IParameter ... _values) {

        List<Packet> packets = new ArrayList<Packet>();

        for (IParameter parameter : _values) {

            final Packet packet = new Packet(Command.UPDATE, parameter);
            packets.add(packet);

            try {
                final byte[] data = Packet.serialize(packet, false);

                for (final RCPTransporter transporter : transporterList) {
                    System.out.println("update : " + parameter.getId());
                    transporter.send(data);
                }
            }
            catch (IOException _e) {
                _e.printStackTrace();
            }

        }

        // TODO
        // we probably want to send all the parameters at once...
        // if transport packet size allows it


    }

    /**
     * send value updated using the transporterList
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

        if (!transporterList.isEmpty()) {

            // transport value
            final Packet packet = new Packet(Command.UPDATE, _value);
            try {
                final byte[] data = Packet.serialize(packet, false);

                for (final RCPTransporter transporter : transporterList) {
                    System.out.println("update : " + _value.getId());
                    transporter.send(data);
                }
            }
            catch (IOException _e) {
                _e.printStackTrace();
            }
        }
    }


    public void remove(final IParameter _value) {

        operateOnCache(new RCPCacheOperator() {

            @Override
            public void operate(final Map<ByteBuffer, IParameter> valueCache) {

                if (valueCache.containsKey(_value.getId())) {
                    // removed
                    valueCache.remove(_value.getId());
                }
                else {
                    System.out.println("value not in cache - ignore");
                }
            }
        });

        if (!transporterList.isEmpty()) {
            final Packet packet = new Packet(Command.REMOVE, _value);
            try {
                byte[] data = Packet.serialize(packet, false);

                for (final RCPTransporter transporter : transporterList) {
                    transporter.send(data);
                }
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
            System.err.println("no packet... ");
            return;
        }

        if (_packet.getCmd() == Command.UPDATE) {

            _update(_packet);
        }
        else if (_packet.getCmd() == Command.VERSION) {

            // TODO:
            // try to convert to version object
            System.out.println("version object yet to be specified");
        }
        else if (_packet.getCmd() == Command.INITIALIZE) {

            if (_packet.getData() != null) {
                // TODO: send full description of only one parameter
                final IParameter val = (IParameter)_packet.getData();

                // get value from cache, and send it...
            }

            _init(_packet, _transporter);
        }
        else {

            System.err.println("not implemented command: " + _packet.getCmd());
        }
    }


    private void _update(final Packet _packet) {

        final IParameter val = (IParameter)_packet.getData();

        operateOnCache(new RCPCacheOperator() {

            @Override
            public void operate(final Map<ByteBuffer, IParameter> valueCache) {

                //updated value cache?
                final IParameter cached = valueCache.get(val.getId());
                if (cached != null) {

                    // update cached
                    ((Parameter)cached).update(val);

                    // call listeners with cached...
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

        // update all clients...
        // TODO: filter out the client who sent the update!
        update(val);

    }


    private void _init(final Packet _packet, final RCPTransporter _transporter) {

        System.out.println("GOT INIT");

        operateOnCache(new RCPCacheOperator() {

            @Override
            public void operate(final Map<ByteBuffer, IParameter> valueCache) {

                // init with all values
                for (final Entry<ByteBuffer, IParameter> entry : valueCache.entrySet()) {

                    System.out.println("sending ::: " + entry.getValue().getDescription());

                    final Packet packet = new Packet(Command.ADD, entry.getValue());
                    try {
                        final byte[] data = Packet.serialize(packet, true);
                        _transporter.send(data);
                    }
                    catch (IOException _e) {
                        _e.printStackTrace();
                    }

                }

            }
        });

        if (initListener != null) {
            initListener.init();
        }
    }
}
