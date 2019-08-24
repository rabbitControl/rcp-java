package org.rabbitcontrol.rcp;

import io.kaitai.struct.ByteBufferKaitaiStream;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RCPCommands.Init;
import org.rabbitcontrol.rcp.model.RcpTypes.Command;
import org.rabbitcontrol.rcp.model.exceptions.*;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.transport.ServerTransporter;
import org.rabbitcontrol.rcp.transport.ServerTransporterListener;

import java.io.IOException;
import java.util.*;
import java.util.List;

// TODO
/*
- create parameter, create parameter with group
- update() only sends the updates to the clients
- remove group only sends to remove the group (not all the children...)
 */

/**
 * Created by inx on 30/11/16.
 */
public class RCPServer extends RCPBase implements ServerTransporterListener {

    private final List<ServerTransporter> transporterList = new ArrayList<ServerTransporter>();

    private Set<Short> ids = new HashSet<Short>();

    private final List<IParameter> parameterToRemove = new ArrayList<IParameter>();

    //------------------------------------------------------------
    // callback objects
    private Init initListener;

    //------------------------------------------------------------
    //
    public RCPServer(final ServerTransporter... _transporter) {

        addTransporter(_transporter);
    }

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

    //------------------------------------------------------------
    //
    public void setInitListener(final Init _listener) {

        initListener = _listener;
    }

    //
    //
    //
    private void setupParameter(
            final Parameter _parameter, final String _label, final GroupParameter _group) {

        _parameter.setLabel(_label);
        _parameter.setManager(this);

        // addParameter adds _parameter to dirtyParams
        addParameter(_parameter, _group);
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
    public Int32Parameter createInt16Parameter(final String _label) throws RCPParameterException {

        return createInt16Parameter(_label, rootGroup);
    }

    public Int32Parameter createInt16Parameter(
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

        return createFloatParameter(_label, rootGroup);
    }

    public Float32Parameter createFloatParameter(
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

        return createDoubleParameter(_label, rootGroup);
    }

    public Float64Parameter createDoubleParameter(
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

    public <T, E> ArrayParameter<T, E> createArrayParameter(
            final String _label,
            final RcpTypes.Datatype _datatype,
            final int... _sizes) throws RCPParameterException {

        return createArrayParameter(_label, rootGroup, _datatype, _sizes);
    }

    public <T, E> ArrayParameter<T, E> createArrayParameter(
            final String _label,
            final GroupParameter _group,
            final RcpTypes.Datatype _datatype,
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
            _e.printStackTrace();
        }
        catch (final IllegalAccessException _e) {
            _e.printStackTrace();
        }

        throw new RCPParameterException("could not create array parameter!");
    }

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

    //------------------------------------------------------------
    //
    public void addParameters(final IParameter... _parameter) {

        addParameters(rootGroup, _parameter);
    }

    public void addParameters(
            final GroupParameter _group, final IParameter... _parameter) {

        for (final IParameter parameter : _parameter) {
            addParameter(parameter, _group);
        }
    }

    public void addParameter(final IParameter _parameter) {

        addParameter(_parameter, rootGroup);
    }

    public void addParameter(final IParameter _parameter, final GroupParameter _group) {

        if (_group != null) {

            // this add parameter to dirtyParams
            _group.addChild(_parameter);

            // make sure we send parameter _after_ group

            // this can happen if parameter was added earlier before
            // ParameterGroup _group was added and then got added to _group
            if (dirtyParams.contains(_parameter)) {
                dirtyParams.remove(_parameter);
                dirtyParams.add(_parameter);
            }
        }
        else {
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
            else {
                System.out.println("already added value with this id - ignore");
            }

            return;
        }

        //------------------------------------------------
        // add parameter to valueCache (flat map)
        valueCache.put(_parameter.getId(), _parameter);

        if (!dirtyParams.contains(_parameter)) {
            dirtyParams.add(_parameter);
        }

        if (!ids.contains(_parameter.getId())) {
            ids.add(_parameter.getId());
        }
        else {
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

    private void _sendParameterFullAll(final IParameter _parameter) {

        //------------------------------------------------
        // send add to all
        if (!transporterList.isEmpty()) {

            try {
                final byte[] data = new Packet(Command.UPDATE, _parameter).serialize(true);

                System.out.println("send parameter: " + _parameter.getLabel());

                for (final ServerTransporter transporter : transporterList) {
                    transporter.sendToAll(data, null);
                }
            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }
            catch (RCPException _e) {
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

        try {
            final byte[] data = new Packet(Command.UPDATE, _parameter).serialize(true);
            _transporter.sendToOne(data, _id);
        }
        catch (final IOException _e) {
            _e.printStackTrace();
        }
        catch (RCPException _e) {
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

    /**
     * public interface: update
     * updates all dirty parameters and removes parameters to remove
     */
    public void update() {

        // update dirty params
        for (final IParameter parameter : dirtyParams) {
            // send to all
            // INFO: don't call update(_parameter!!) -> co-modification of list
            update(parameter, null);
        }
        dirtyParams.clear();

        // remove
        for (final IParameter parameter : parameterToRemove) {

            // free id
            ids.remove(parameter.getId());

            if (!transporterList.isEmpty()) {
                try {
                    final byte[] data = new Packet(Command.REMOVE, parameter).serialize(false);

                    for (final ServerTransporter transporter : transporterList) {
                        transporter.sendToAll(data, null);
                    }
                }
                catch (final IOException _e) {
                    _e.printStackTrace();
                }
                catch (RCPException _e) {
                    _e.printStackTrace();
                }
            }
        }
        parameterToRemove.clear();
    }

    private void update(final IParameter _value, final Object _id) {

        if (!transporterList.isEmpty()) {

            //TODO possibly check for connected clients

            try {
                final Packet packet = new Packet(Command.UPDATE, _value);
                final byte[] data   = Packet.serialize(packet, false);



                for (final ServerTransporter transporter : transporterList) {
                    System.out.println("update : " + _value.getId());
                    transporter.sendToAll(data, _id);
                }
            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }
            catch (RCPException _e) {
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

        ((Parameter)_parameter).setParent(null);

        if (dirtyParams.contains(_parameter)) {
            dirtyParams.remove(_parameter);
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
            final byte[] _data, final ServerTransporter _transporter, final Object _id) {

        System.out.println(_data);

        try {
            final Packet _packet = Packet.parse(new ByteBufferKaitaiStream(_data));

            if (_packet == null) {
                System.err.println("no packet... ");
                return;
            }

            final Command cmd = _packet.getCmd();

            switch (cmd) {
                case UPDATE:
                case UPDATEVALUE:
                {
                    if (_update(_packet, _transporter, _id)) {
                        // update all clients, bypass deserialize and serialize...
                        for (final ServerTransporter transporter : transporterList) {
                            transporter.sendToAll(_data, _id);
                        }
                    }
                    break;
                }


                case VERSION:
                {
                    // TODO:
                    // try to convert to version object

                    // answer with version
                    final Packet versionPacket = new Packet(Command.VERSION);
                    versionPacket.setData(new VersionData("0.0.0"));

                    try {
                        _transporter.sendToOne(versionPacket.serialize(true), _id);
                    }
                    catch (final IOException _e) {
                        _e.printStackTrace();
                    }
                    catch (final RCPException _e) {
                        _e.printStackTrace();
                    }
                    break;
                }

                case INITIALIZE:
                {
                    if (_packet.getData() != null) {
                        // TODO: send full description of only one parameter
                        final IParameter val = (IParameter)_packet.getData();

                        // get value from cache, and send it...
                    }

                    _init(_transporter, _id);
                    break;
                }

                case REMOVE:
                    // invalid on server
                    System.err.println("cannot remove parameter at server");
                    break;

                case INVALID:
                case DISCOVER:
                default:
                    System.err.println("not implemented command: " + cmd);

            }

        }
        catch (final RCPUnsupportedFeatureException _e) {
            _e.printStackTrace();
        }
        catch (final RCPDataErrorException _e) {
            _e.printStackTrace();
        }
    }

    private boolean _update(
            final Packet _packet, final ServerTransporter _transporter, final Object _id) {

        try {

            final IParameter parameter = _packet.getDataAsParameter();

            //updated value cache?
            final IParameter cached_parameter = valueCache.get(parameter.getId());
            if (cached_parameter != null) {

                // update cached
                try {
                    ((Parameter)cached_parameter).update(parameter);

                    // call listeners with cached...
                    if (updateListener != null) {
                        updateListener.parameterUpdated(cached_parameter);
                    }

                    return true;
                }
                catch (RCPException _e) {
                    _e.printStackTrace();
                }
            }
            else {
                System.err.println("server: update: parameter not found in valuecache - " +
                                   "ignoring");
            }

        } catch (final ClassCastException _e) {
            // nop
        }

        return false;
    }

    private void _init(final ServerTransporter _transporter, final Object _id) {

        System.out.println("GOT INIT");

        for (final IParameter parameter : rootGroup.getChildren()) {
            _sendParameterFull(parameter, _transporter, _id);
        }

        if (initListener != null) {
            initListener.init();
        }
    }

    @Override
    public void setParameterDirty(final IParameter _parameter) {

        if (parameterToRemove.contains(_parameter)) {
            System.out.println("parameter marked for deletion... " + _parameter);
            return;
        }

        super.setParameterDirty(_parameter);
    }
}