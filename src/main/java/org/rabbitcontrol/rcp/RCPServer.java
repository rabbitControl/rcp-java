package org.rabbitcontrol.rcp;

import io.kaitai.struct.ByteBufferKaitaiStream;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RCPCommands.Init;
import org.rabbitcontrol.rcp.model.RCPCommands.Update;
import org.rabbitcontrol.rcp.model.RcpTypes.Command;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.exceptions.*;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.transport.ServerTransporter;
import org.rabbitcontrol.rcp.transport.ServerTransporterListener;

import java.util.*;

// TODO
/*
- update() only sends the updates to the clients
- remove group only sends to remove the group (not all the children...)
 */

/**
 * Created by inx on 30/11/16.
 */
public class RCPServer extends RCPBase implements ServerTransporterListener {

    //------------------------------------------------------------
    //
    private final List<ServerTransporter> transporterList = new ArrayList<ServerTransporter>();

    private Set<Short> ids = new HashSet<Short>();

    protected final List<IParameter> addedParams =
            Collections.synchronizedList(new ArrayList<IParameter>());
    private final List<IParameter> parameterToRemove = Collections.synchronizedList(new ArrayList<IParameter>());

    // callback objects
    private final Set<Init> initListener = new HashSet<Init>();

    //------------------------------------------------------------
    // constructor
    public RCPServer(final ServerTransporter... _transporter) {

        addTransporter(_transporter);
    }

    //------------------------------------------------------------
    // dispose
    public void dispose() {

        super.dispose();

        // close all connections
        for (final ServerTransporter serverTransporter : transporterList) {
            serverTransporter.unbind();
        }

        transporterList.clear();
        ids.clear();
        addedParams.clear();
        parameterToRemove.clear();

        initListener.clear();
    }


    //------------------------------------------------------------
    // transporter
    public void addTransporter(final ServerTransporter... _transporter) {

        for (final ServerTransporter serverTransporter : _transporter) {
            if (!transporterList.contains(serverTransporter)) {
                transporterList.add(serverTransporter);
                serverTransporter.addListener(this);
            }
        }
    }

    public void removeTransporter(final ServerTransporter... _transporter) {

        for (final ServerTransporter serverTransporter : _transporter) {
            if (transporterList.contains(serverTransporter)) {
                transporterList.remove(serverTransporter);
                serverTransporter.removeListener(this);
            }
        }
    }

    public List<ServerTransporter> getTransporter() {
        return transporterList;
    }

    //------------------------------------------------------------
    // connection count
    public int getConnectionCount() {

        int count = 0;
        for (final ServerTransporter serverTransporter : transporterList) {
            count += serverTransporter.getConnectionCount();
        }

        return count;
    }

    //------------------------------------------------------------
    //

    /**
     * Set Init listener
     *
     * @deprecated please use addInitListener() instead.
     * @param _listener the Init listener to set.
     */
    @Deprecated
    public void setInitListener(final Init _listener) {

        addInitListener(_listener);
    }

    /**
     * Add Init listener
     *
     * @param _listener the Init listener to add.
     */
    public void addInitListener(final Init _listener) {
        if (!initListener.contains(_listener)) {
            initListener.add(_listener);
        }
    }

    /**
     * Remove Init listener
     *
     * @param _listener the Init listener to remove.
     */
    public void removeInitListener(final Init _listener) {
        if (initListener.contains(_listener)) {
            initListener.remove(_listener);
        }
    }


    //----------------------------------------------------
    //----------------------------------------------------
    // boolean
    //----------------------------------------------------
    //----------------------------------------------------
    public BooleanParameter createBooleanParameter(
            final String _label) throws RCPParameterException {

        return createBooleanParameter(_label, rootGroup);
    }

    public BooleanParameter createBooleanParameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();
        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final BooleanParameter p = new BooleanParameter(id);
        setupParameter(p, _label, _group);
        return p;
    }


    //----------------------------------------------------
    //----------------------------------------------------
    // bang
    //----------------------------------------------------
    //----------------------------------------------------
    public BangParameter createBangParameter(
            final String _label) throws RCPParameterException {

        return createBangParameter(_label, rootGroup);
    }

    public BangParameter createBangParameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();
        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final BangParameter p = new BangParameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // int8
    //----------------------------------------------------
    //----------------------------------------------------
    public Int8Parameter createInt8Parameter(final String _label) throws RCPParameterException {

        return createInt8Parameter(_label, rootGroup);
    }

    public Int8Parameter createInt8Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final Int8Parameter p = new Int8Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // int16
    //----------------------------------------------------
    //----------------------------------------------------
    public Int16Parameter createInt16Parameter(final String _label) throws RCPParameterException {

        return createInt16Parameter(_label, rootGroup);
    }

    public Int16Parameter createInt16Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final Int16Parameter p = new Int16Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // int32
    //----------------------------------------------------
    //----------------------------------------------------
    public Int32Parameter createInt32Parameter(final String _label) throws RCPParameterException {

        return createInt32Parameter(_label, rootGroup);
    }

    public Int32Parameter createInt32Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final Int32Parameter p = new Int32Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // int64
    //----------------------------------------------------
    //----------------------------------------------------
    public Int64Parameter createInt64Parameter(final String _label) throws RCPParameterException {

        return createInt64Parameter(_label, rootGroup);
    }

    public Int64Parameter createInt64Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final Int64Parameter p = new Int64Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // float
    //----------------------------------------------------
    //----------------------------------------------------
    public Float32Parameter createFloatParameter(final String _label) throws RCPParameterException {

        return createFloat32Parameter(_label, rootGroup);
    }
    public Float32Parameter createFloatParameter(final String _label, final GroupParameter _group) throws RCPParameterException {
        return createFloat32Parameter(_label, _group);
    }
    public Float32Parameter createFloat32Parameter(final String _label) throws RCPParameterException {

        return createFloat32Parameter(_label, rootGroup);
    }
    public Float32Parameter createFloat32Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final Float32Parameter p = new Float32Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // double
    //----------------------------------------------------
    //----------------------------------------------------
    public Float64Parameter createDoubleParameter(final String _label) throws
                                                                       RCPParameterException {
        return createFloat64Parameter(_label, rootGroup);
    }
    public Float64Parameter createDoubleParameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        return createFloat64Parameter(_label, _group);
    }
    public Float64Parameter createFloat64Parameter(final String _label) throws
                                                                       RCPParameterException {
        return createFloat64Parameter(_label, rootGroup);
    }
    public Float64Parameter createFloat64Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final Float64Parameter p = new Float64Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // vectors
    //----------------------------------------------------
    //----------------------------------------------------
    // vector2
    public Vector2Float32Parameter createVector2Float32Parameter(final String _label) throws
                                                                                      RCPParameterException {

        return createVector2Float32Parameter(_label, rootGroup);
    }

    public Vector2Float32Parameter createVector2Float32Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final Vector2Float32Parameter p = new Vector2Float32Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    public Vector2Int32Parameter createVector2Int32Parameter(final String _label) throws
                                                                                  RCPParameterException {

        return createVector2Int32Parameter(_label, rootGroup);
    }

    public Vector2Int32Parameter createVector2Int32Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final Vector2Int32Parameter p = new Vector2Int32Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    // vector3
    public Vector3Float32Parameter createVector3Float32Parameter(final String _label) throws
                                                                       RCPParameterException {

        return createVector3Float32Parameter(_label, rootGroup);
    }

    public Vector3Float32Parameter createVector3Float32Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final Vector3Float32Parameter p = new Vector3Float32Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    public Vector3Int32Parameter createVector3Int32Parameter(final String _label) throws
                                                                                      RCPParameterException {

        return createVector3Int32Parameter(_label, rootGroup);
    }

    public Vector3Int32Parameter createVector3Int32Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final Vector3Int32Parameter p = new Vector3Int32Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    // vector4
    public Vector4Float32Parameter createVector4Float32Parameter(final String _label) throws
                                                                                      RCPParameterException {

        return createVector4Float32Parameter(_label, rootGroup);
    }

    public Vector4Float32Parameter createVector4Float32Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final Vector4Float32Parameter p = new Vector4Float32Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    public Vector4Int32Parameter createVector4Int32Parameter(final String _label) throws
                                                                                  RCPParameterException {

        return createVector4Int32Parameter(_label, rootGroup);
    }

    public Vector4Int32Parameter createVector4Int32Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final Vector4Int32Parameter p = new Vector4Int32Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // range
    //----------------------------------------------------
    //----------------------------------------------------
    public <T extends Number> RangeParameter<T> createRangeParameter(final String _label, final
    Class<T>
            _class) throws
                                                                       RCPParameterException {

        return createRangeParameter(_label, rootGroup, _class);
    }

    public <T extends Number> RangeParameter<T> createRangeParameter(
            final String _label, final GroupParameter _group, final Class<T> _class) throws RCPParameterException {

        final short id = availableId();

        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final RangeParameter<T> p = new RangeParameter<T>(id, _class);
        setupParameter(p, _label, _group);
        return p;
    }


    //----------------------------------------------------
    //----------------------------------------------------
    // string
    //----------------------------------------------------
    //----------------------------------------------------
    public StringParameter createStringParameter(final String _label) throws RCPParameterException {

        return createStringParameter(_label, rootGroup);
    }

    public StringParameter createStringParameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();
        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final StringParameter p = new StringParameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // enum
    //----------------------------------------------------
    //----------------------------------------------------
    public EnumParameter createEnumParameter(final String _label) throws RCPParameterException {

        return createEnumParameter(_label, rootGroup);
    }

    public EnumParameter createEnumParameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();
        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final EnumParameter p = new EnumParameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // RGB
    //----------------------------------------------------
    //----------------------------------------------------
    public RGBParameter createRGBParameter(final String _label) throws RCPParameterException {

        return createRGBParameter(_label, rootGroup);
    }

    public RGBParameter createRGBParameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();
        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final RGBParameter p = new RGBParameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // RGBA
    //----------------------------------------------------
    //----------------------------------------------------
    public RGBAParameter createRGBAParameter(final String _label) throws RCPParameterException {

        return createRGBAParameter(_label, rootGroup);
    }

    public RGBAParameter createRGBAParameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();
        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final RGBAParameter p = new RGBAParameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // image
    //----------------------------------------------------
    //----------------------------------------------------
    public ImageParameter createImageParameter(
            final String _label) throws RCPParameterException {

        return createImageParameter(_label, rootGroup);
    }

    public ImageParameter createImageParameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();
        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final ImageParameter p = new ImageParameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // group
    //----------------------------------------------------
    //----------------------------------------------------
    public GroupParameter createGroupParameter(final String _label) throws RCPParameterException {

        return createGroupParameter(_label, rootGroup);
    }

    public GroupParameter createGroupParameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();
        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final GroupParameter p = new GroupParameter(id);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // array
    //----------------------------------------------------
    //----------------------------------------------------
    public <T, E> ArrayParameter<T, E> createArrayParameter(
            final String _label,
            final Datatype _datatype,
            final int... _sizes) throws RCPParameterException {

        return createArrayParameter(_label, rootGroup, _datatype, _sizes);
    }

    public <T, E> ArrayParameter<T, E> createArrayParameter(
            final String _label,
            final GroupParameter _group,
            final Datatype _datatype,
            final int... _sizes) throws RCPParameterException {

        final short id = availableId();
        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        try {
            final ArrayParameter<T, E> p = ArrayParameter.create(id,
                                                                 (Class<E>)RCPFactory.getClass(
                                                                         _datatype),
                                                                 _sizes);

            setupParameter(p, _label, _group);
            return p;
        }
        catch (final InstantiationException _e) {
            throw new RCPParameterException(_e);
        }
        catch (final IllegalAccessException _e) {
            throw new RCPParameterException(_e);
        }
    }

    //----------------------------------------------------
    //----------------------------------------------------
    // IPV4
    //----------------------------------------------------
    //----------------------------------------------------
    public IPv4Parameter createIPv4Parameter(final String _label) throws RCPParameterException {

        return createIPv4Parameter(_label, rootGroup);
    }

    public IPv4Parameter createIPv4Parameter(
            final String _label, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();
        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final IPv4Parameter p = new IPv4Parameter(id);
        setupParameter(p, _label, _group);
        return p;
    }


    //----------------------------------------------------
    //----------------------------------------------------
    // Custom
    //----------------------------------------------------
    //----------------------------------------------------
    public CustomParameter createCustomParameter(final String _label, final long size) throws RCPParameterException {

        return createCustomParameter(_label, size, rootGroup);
    }

    public CustomParameter createCustomParameter(
            final String _label, final long size, final GroupParameter _group) throws RCPParameterException {

        final short id = availableId();
        if (id == 0) {
            throw new RCPParameterException("could not get valid parameter id");
        }

        final CustomParameter p = new CustomParameter(id, size);
        setupParameter(p, _label, _group);
        return p;
    }

    //----------------------------------------------------
    // parameter end
    //----------------------------------------------------
    /**
     * get next available id
     *
     * @return next available id
     */
    private short availableId() {

        for (short i = 1; i > 0; i++) {
            if (ids.contains(i)) {
                continue;
            }

            return i;
        }

        for (short i = -1; i < 0; i--) {
            if (ids.contains(i)) {
                continue;
            }

            return i;
        }

        // invalid id
        return 0;
    }

    //
    //
    //
    private void setupParameter(
            final Parameter _parameter, final String _label, final GroupParameter _group) {

        _parameter.setLabel(_label);

        // addParameter adds _parameter to dirtyParams
        addParameter(_group, _parameter);
    }

    //------------------------------------------------------------
    //
    public void addParameters(final IParameter... _parameter) {

        addParameters(rootGroup, _parameter);
    }

    public void addParameters(
            final GroupParameter _group, final IParameter... _parameter) {

        for (final IParameter parameter : _parameter) {
            addParameter(_group, parameter);
        }
    }

    public void addParameter(final IParameter _parameter) {

        addParameter(rootGroup, _parameter);
    }

    public void addParameter(
            final GroupParameter _group, final IParameter _parameter)
    {
        // check for id 0
        if (_parameter.getId() == 0) {
            System.err.println("addParameter: invalid id 0");
            return;
        }

        // check if id is already registered
        if (valueCache.containsKey(_parameter.getId())) {

            System.err.println("addParameter: reject already existing id");

            if (!valueCache.get(_parameter.getId()).equals(_parameter)) {
                System.err.println("different object with same ID!!!");
            }

            return;
        }

        _parameter.setManager(this);

        if (_group != null)
        {
            // this adds the parameter to dirtyParams
            _group.addChild(_parameter);

            // make sure we send parameter _after_ group
            // add parameter to the end of dirtyParams list

            // this can happen if parameter was added earlier before
            // ParameterGroup _group was added and then got added to _group
            synchronized (dirtyParams)
            {
                if (dirtyParams.contains(_parameter)) {
                    dirtyParams.remove(_parameter);
                    dirtyParams.add(_parameter);
                }
            }
        }
        else
        {
            // add to root group
            // this adds _parameter to dirtyParams
            rootGroup.addChild(_parameter);
        }

        // add everything to flat-map
        _addParameterFlat(_parameter);
    }

    private void _addParameterFlat(final IParameter _parameter) {

        if (valueCache.containsKey(_parameter.getId())) {

            if (!valueCache.get(_parameter.getId()).equals(_parameter)) {
                System.err.println("different object with same ID!!!");
            }

            return;
        }

        //------------------------------------------------
        // add parameter to valueCache (flat map)
        valueCache.put(_parameter.getId(), _parameter);

        // remove from deleted params
        synchronized (parameterToRemove)
        {
            if (parameterToRemove.contains(_parameter)) {
                parameterToRemove.remove(_parameter);
            }
        }

        // remove from dirty params
        synchronized (dirtyParams)
        {
            if (dirtyParams.contains(_parameter)) {
                dirtyParams.remove(_parameter);
            }
        }

        // add to addedParameters list
        synchronized (addedParams)
        {
            if (!addedParams.contains(_parameter)) {
                addedParams.add(_parameter);
            }
        }

        if (!ids.contains(_parameter.getId())) {
            ids.add(_parameter.getId());
        } else {
            // ? ignore
        }

        //------------------------------------------------
        // if group: add all children into flat-map
        if (_parameter instanceof GroupParameter) {

            for (final IParameter _child : ((GroupParameter)_parameter).getChildren()) {
                _addParameterFlat(_child);
            }
        }
    }


    private void _sendParameterFullAll(final IParameter _parameter) throws
                                                                    RCPException {

        //------------------------------------------------
        // send add to all
        if (!transporterList.isEmpty()) {

            final byte[] data = new Packet(Command.UPDATE, _parameter).serialize(true);

            for (final ServerTransporter transporter : transporterList) {
                transporter.sendToAll(data, null);
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
            final IParameter _parameter, final ServerTransporter _transporter, final Object _id) throws
                                                                                                 RCPException {

        final byte[] data = new Packet(Command.UPDATE, _parameter).serialize(true);
        _transporter.sendToOne(data, _id);

        //------------------------------------------------
        // send all children
        if (_parameter instanceof GroupParameter) {

            for (final IParameter _child : ((GroupParameter)_parameter).getChildren()) {
                _sendParameterFull(_child, _transporter, _id);
            }
        }
    }

    /**
     * public interface: update
     * updates all dirty parameters and removes parameters to remove
     *
     * @throws RCPException
     *      in case Packet.serialize fails
     */
    public void update() throws RCPException {

        // send added parameters first
        synchronized (addedParams)
        {
            for (final IParameter parameter : addedParams)
            {
                // send parameter fully and all its children
                _sendParameterFullAll(parameter);
            }

            addedParams.clear();
        }

        // update dirty params
        synchronized (dirtyParams)
        {
            for (final IParameter parameter : dirtyParams) {
                // send to all
                // INFO: don't call update(_parameter!!) -> co-modification of list
                update(parameter, null);
            }
            dirtyParams.clear();
        }

        // remove
        synchronized (parameterToRemove)
        {
            for (final IParameter parameter : parameterToRemove) {

                // free id
                ids.remove(parameter.getId());

                if (!transporterList.isEmpty()) {
                    final byte[] data =
                            new Packet(Command.REMOVE, new IdData(parameter.getId())).serialize(false);

                    for (final ServerTransporter transporter : transporterList) {
                        transporter.sendToAll(data, null);
                    }
                }
            }
            parameterToRemove.clear();
        }
    }

    private void update(final IParameter _parameter, final Object _id) throws
                                                                   RCPException {

        if (_parameter == null) {
            return;
        }

        if (!transporterList.isEmpty()) {

            //TODO possibly check for connected clients

            final Command cmd;
            if (_parameter.onlyValueChanged())
            {
                cmd = Command.UPDATEVALUE;
            }
            else
            {
                cmd = Command.UPDATE;
            }

            final Packet packet = new Packet(cmd, _parameter);
            final byte[] data   = Packet.serialize(packet, false);

            for (final ServerTransporter transporter : transporterList) {
                transporter.sendToAll(data, _id);
            }
        }
    }

    public void remove(final IParameter _parameter) {

        if (_parameter == null) {
            return;
        }

        if (valueCache.containsKey(_parameter.getId())) {
            // removed
            valueCache.remove(_parameter.getId());
        }
        else if (RCP.doDebugLogging) {
            System.out.println("value not in cache - ignore");
        }

        _parameter.setParent(null);

        // remove from dirty params
        synchronized (dirtyParams)
        {
            if (dirtyParams.contains(_parameter)) {
                dirtyParams.remove(_parameter);
            }
        }

        // remove from added params
        synchronized (addedParams)
        {
            if (addedParams.contains(_parameter)) {
                addedParams.remove(_parameter);
            }
        }

        parameterToRemove.add(_parameter);

        //- remove group: only remove group.id is sent
        // client needs to remove all children
        //------------------------------------------------
        // remove all children
        //        if (_parameter instanceof GroupParameter) {
        //
        //            for (final IParameter _child : ((GroupParameter)_parameter).getChildren()) {
        //                remove(_child);
        //            }
        //        }
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public void received(
            final byte[] _data, final ServerTransporter _transporter, final Object _id) throws
                                                                                        RCPException,
                                                                                        RCPDataErrorException {

        final Packet packet;
        try {
            packet = Packet.parse(new ByteBufferKaitaiStream(_data));
        }
        catch (RCPUnsupportedFeatureException _e) {
            System.err.println("could not parse: " + _e.getMessage());
            return;
        }

        final Command cmd = packet.getCmd();

        switch (cmd) {
            case UPDATE:
            case UPDATEVALUE:
            {
                if (_update(packet, _transporter, _id))
                {
                    // update all clients, bypass deserialize and serialize...
                    for (final ServerTransporter transporter : transporterList) {
                        transporter.sendToAll(_data, _id);
                    }
                }
                break;
            }


            case INFO:
            {
                final InfoData version_data = packet.getDataAsInfoData();

                if (version_data != null) {
                    System.out.println("client version: " + version_data.getVersion());
                    if (!version_data.getApplicationId().isEmpty()) {
                        System.out.println("client application: " + version_data.getApplicationId());
                    }
                }
                else
                {
                    // no data, answer with own version
                    final Packet version_packet = new Packet(Command.INFO);
                    version_packet.setData(new InfoData(RCP.getRcpVersion(),
                                                       applicationId +
                                                       " (" +
                                                       RCP.getLibraryVersion() +
                                                       ")"));

                    _transporter.sendToOne(version_packet.serialize(true), _id);

                    // request infodata from client
                    _transporter.sendToOne(new Packet(Command.INFO).serialize(true), _id);
                }

                break;
            }

            case INITIALIZE:
            {
                final IdData id_data = packet.getDataAsIdData();
                if (id_data != null) {
                    _init(valueCache.get(id_data.getId()), _transporter, _id);
                }

                _init(_transporter, _id);
                break;
            }

            case REMOVE:
                // invalid on server
                System.err.println("can not remove parameter at server");
                break;

            case DISCOVER:
                System.out.println("DISCOVER not implemented");
                break;

            case INVALID:
                if (RCP.doDebugLogging) System.err.println("invalid command");

        }
    }

    private boolean _update(
            final Packet _packet, final ServerTransporter _transporter, final Object _id) throws
                                                                                          RCPException {

        final IParameter parameter = _packet.getDataAsParameter();

        if (parameter == null) {
            return false;
        }

        //updated value cache?
        final IParameter cached_parameter = valueCache.get(parameter.getId());
        if (cached_parameter != null) {

            // update cached
            ((Parameter)cached_parameter).update(parameter);

            // call listeners with cached...
            for (Update listener : updateListener) {
                listener.parameterUpdated(cached_parameter);
            }

            return true;
        }
        else {
            System.err.println("server: update: parameter not found in valuecache - " +
                               "ignoring");
        }

        return false;
    }

    private void _init(final IParameter _parameter, final ServerTransporter _transporter,
                       final Object _id) throws RCPException {

        if (_parameter == null) {
            return;
        }

        _sendParameterFull(_parameter, _transporter, _id);

        for (final Init listener : initListener) {
            listener.init();
        }
    }

    private void _init(final ServerTransporter _transporter, final Object _id) throws RCPException
    {
        synchronized (rootGroup)
        {
            for (final IParameter parameter : rootGroup.getChildren())
            {
                _sendParameterFull(parameter, _transporter, _id);
            }
        }

        if (RCP.rcp100FeaturesEnabled)
        {
            // send INITIALIZE to mark end of parameter list
            final byte[] data = new Packet(Command.INITIALIZE).serialize(true);
            _transporter.sendToOne(data, _id);
        }

        for (final Init listener : initListener) {
            listener.init();
        }
    }

    @Override
    public void setParameterDirty(final IParameter _parameter) {

        if (parameterToRemove.contains(_parameter)) {
            // parameter marked for deletion
            return;
        }

        if (addedParams.contains(_parameter)) {
            // parameter is added - no need to set it dirty
            return;
        }

        super.setParameterDirty(_parameter);
    }

}
