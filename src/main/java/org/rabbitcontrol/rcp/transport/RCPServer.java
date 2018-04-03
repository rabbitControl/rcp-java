package org.rabbitcontrol.rcp.transport;

import io.kaitai.struct.ByteBufferKaitaiStream;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RCPCommands.Init;
import org.rabbitcontrol.rcp.model.exceptions.*;
import org.rabbitcontrol.rcp.model.gen.RcpTypes;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Command;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.interfaces.INumberParameter;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by inx on 30/11/16.
 */
public class RCPServer extends RCPBase implements ServerTransporterListener {

    private final List<ServerTransporter> transporterList = new ArrayList<ServerTransporter>();

    ArrayList<Short> ids = new ArrayList<Short>();

    //------------------------------------------------------------
    // callback objects
    private Init initListener;

    //------------------------------------------------------------
    //
    public RCPServer(final ServerTransporter _transporter) {

        addTransporter(_transporter);
    }

    public void addTransporter(final ServerTransporter _transporter) {

        if (!transporterList.contains(_transporter)) {
            transporterList.add(_transporter);
            _transporter.addListener(this);
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

    //
    //
    //
    public IParameter createParameter(final Datatype _datatype) throws RCPParameterException {
        final short id = availableId();
        if (id != 0) {

            final IParameter p = ParameterFactory.createParameter(id, _datatype);
            addParameter(p);
            return p;
        }

        throw new RCPParameterException("could not get valid parameter id");
    }

    public <T extends Number> INumberParameter<T> createNumberParameter(final Datatype _datatype, final Class<T> _class) throws
                                                                                                             RCPParameterException,
                                                                                                             TypeMissmatch {

        final short id = availableId();
        if (id != 0) {

            final INumberParameter<T> p = ParameterFactory.createNumberParameter(id, _datatype, _class);
            addParameter(p);
            return p;
        }

        throw new RCPParameterException("could not get valid parameter id");
    }

    public <T extends Number> INumberParameter<T> createNumberParameter(final Class<T> _class) throws
                                                                                         RCPParameterException {

        final short id = availableId();
        if (id != 0) {

            final INumberParameter<T> p = ParameterFactory.createNumberParameter(id, _class);
            addParameter(p);
            return p;
        }

        throw new RCPParameterException("could not get valid parameter id");
    }

    public NumberParameter<Byte> createUInt8Parameter() throws RCPParameterException {

        final short id = availableId();
        if (id != 0) {

            final NumberParameter<Byte> p = new NumberParameter<Byte>(id, Datatype.UINT8);
            addParameter(p);
            return p;
        }

        throw new RCPParameterException("could not get valid parameter id");
    }

    public NumberParameter<Byte> createInt8Parameter() throws RCPParameterException {
        final short id = availableId();
        if (id != 0) {

            final NumberParameter<Byte> p = new NumberParameter<Byte>(id, Datatype.INT8);
            addParameter(p);
            return p;
        }

        throw new RCPParameterException("could not get valid parameter id");
    }


    public BooleanParameter createBooleanParameter() throws RCPParameterException {

        final short id = availableId();
        if (id != 0) {

            final BooleanParameter p = new BooleanParameter(id);
            addParameter(p);
            return p;
        }

        throw new RCPParameterException("could not get valid parameter id");
    }

    public StringParameter createStringParameter() throws RCPParameterException {

        final short id = availableId();
        if (id != 0) {

            final StringParameter p = new StringParameter(id);
            addParameter(p);
            return p;
        }

        throw new RCPParameterException("could not get valid parameter id");
    }


    private short availableId() {

        for (short i = 1; i <= Short.MAX_VALUE; i++) {
            if (ids.contains(i)) {
                continue;
            }

            return i;
        }

        for (short i= -1; i>=Short.MIN_VALUE; i--) {
            if (ids.contains(i)) {
                continue;
            }

            return i;
        }

        // invalid id
        return 0;
    }

    //------------------------------------------------------------
    //
    public void addParameter(final IParameter _value) {

        if (groupsAndParameters.contains(_value)) {
            System.err.println("already in cache...");
            return;
        }

        // add to hierarchical...
        groupsAndParameters.add(_value);

        // add everything to flat-map
        _addParameter(_value);

        // send it out
        _sendParameterFullAll(_value);
    }

    private void _sendParameterFullAll(final IParameter _parameter) {

        //------------------------------------------------
        // send add to all
        if (!transporterList.isEmpty()) {
            // TODO: send to all clients
            final Packet packet = new Packet(Command.UPDATE, _parameter);
            try {
                final byte[] data = Packet.serialize(packet, true);

                System.out.println("send parameter: " + _parameter.getLabel());

                for (final ServerTransporter transporter : transporterList) {
                    transporter.sendToAll(data, null);
                }
            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }
        }

        //------------------------------------------------
        // send all children
        if (_parameter instanceof GroupParameter) {

            for (final IParameter _child : ((GroupParameter)_parameter).getChildren()) {
                _sendParameterFullAll(_child);
            }
        }
    }

    private void _sendParameterFull(
            final IParameter _parameter, final ServerTransporter _transporter, final Object _id) {

        System.out.println("sending ::: " + _parameter.getLabel());

        final Packet packet = new Packet(Command.UPDATE, _parameter);
        try {
            final byte[] data = Packet.serialize(packet, true);
            _transporter.sendToOne(data, _id);
        }
        catch (final IOException _e) {
            _e.printStackTrace();
        }

        //------------------------------------------------
        // send all children
        if (_parameter instanceof GroupParameter) {

            for (final IParameter _child : ((GroupParameter)_parameter).getChildren()) {
                _sendParameterFull(_child, _transporter, _id);
            }
        }
    }

    private void _addParameter(final IParameter _value) {

        if (valueCache.containsKey(_value.getId())) {

            if (!valueCache.get(_value.getId()).equals(_value)) {
                System.err.println("different object with same ID!!!");
            }
            else {
                System.out.println("already added value with this id - ignore");
            }

            return;
        }

        //------------------------------------------------
        // add to valueChache
        // added to flat-map...
        valueCache.put(_value.getId(), _value);

        if (ids.contains(_value.getId())) {
            System.err.println("!!!! id already in ids!!!!");
        }
        ids.add(_value.getId());

        //------------------------------------------------
        // add all children into flat-map
        if (_value instanceof GroupParameter) {

            for (final IParameter _child : ((GroupParameter)_value).getChildren()) {
                _addParameter(_child);
            }
        }
    }

    public void update() {

        // TODO: multithreading??

        // update dirty params
        for (final IParameter parameter : dirtyParams) {
            // send to all
            // INFO: don't call update(_parameter!!) -> co-modification of list
            update(parameter, null);
        }

        dirtyParams.clear();
    }

    public void updateParameters(final IParameter... _values) {

        for (final IParameter parameter : _values) {
            update(parameter, null);
        }

        // TODO
        // we probably want to send all the parameters at once...
        // if transport packet size allows it

    }

    /**
     * send value updated using the transporterList
     *
     * @param _parameter
     *         the value to updated
     */
    public void update(final IParameter _parameter) {

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

        update(_parameter, null);

        dirtyParams.remove(_parameter);
    }

    private void update(final IParameter _value, final Object _id) {

        if (!transporterList.isEmpty()) {

            // transport value
            final Packet packet = new Packet(Command.UPDATE, _value);
            try {
                final byte[] data = Packet.serialize(packet, false);

                for (final ServerTransporter transporter : transporterList) {
                    System.out.println("update : " + _value.getId());
                    transporter.sendToAll(data, _id);
                }
            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }
        }
    }



    public void remove(final IParameter _parameter) {

        if (valueCache.containsKey(_parameter.getId())) {
            // removed
            valueCache.remove(_parameter.getId());
        }
        else {
            System.out.println("value not in cache - ignore");
        }

        if (groupsAndParameters.contains(_parameter)) {
            groupsAndParameters.remove(_parameter);
        }

        if (dirtyParams.contains(_parameter)) {
            dirtyParams.remove(_parameter);
        }

        // free id
        ids.remove(_parameter.getId());

        if (!transporterList.isEmpty()) {
            final Packet packet = new Packet(Command.REMOVE, _parameter);
            try {
                final byte[] data = Packet.serialize(packet, false);

                for (final ServerTransporter transporter : transporterList) {
                    transporter.sendToAll(data, null);
                }
            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }

        }

        //------------------------------------------------
        // remove all children
        if (_parameter instanceof GroupParameter) {

            for (final IParameter _child : ((GroupParameter)_parameter).getChildren()) {
                remove(_child);
            }
        }
    }

    //------------------------------------------------------------
    //
    @Override
    public void received(
            final byte[] _data, final ServerTransporter _transporter, final Object _id) {

        try {
            final Packet _packet = Packet.parse(new ByteBufferKaitaiStream(_data));

            if (_packet == null) {
                System.err.println("no packet... ");
                return;
            }

            if (_packet.getCmd() == Command.UPDATE) {

                _update(_packet, _transporter, _id);
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

                _init(_transporter, _id);
            }
            else {

                System.err.println("not implemented command: " + _packet.getCmd());
            }
        }
        catch (final RCPUnsupportedFeatureException _e) {
            _e.printStackTrace();
        }
        catch (final RCPDataErrorException _e) {
            _e.printStackTrace();
        }
    }

    private void _update(
            final Packet _packet, final ServerTransporter _transporter, final Object _id) {

        final IParameter val = (IParameter)_packet.getData();

        operateOnCache(new RCPCacheOperator() {

            @Override
            public void operate(final Map<Short, IParameter> valueCache) {

                //updated value cache?
                final IParameter cached = valueCache.get(val.getId());
                if (cached != null) {

                    // update cached
                    ((Parameter)cached).update(val);

                    // call listeners with cached...
                    if (updateListener != null) {
                        updateListener.parameterUpdated(cached);
                    }
                }
                else {
                    System.err.println("client: updated: no value in value cache - " + "ignoring");
                }
            }
        });

        // update all clients...
        update(val, _id);

    }

    private void _init(final ServerTransporter _transporter, final Object _id) {

        System.out.println("GOT INIT");

        for (final IParameter parameter : groupsAndParameters) {
            _sendParameterFull(parameter, _transporter, _id);
        }

        if (initListener != null) {
            initListener.init();
        }
    }
}
