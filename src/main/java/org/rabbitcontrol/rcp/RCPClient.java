package org.rabbitcontrol.rcp;

import io.kaitai.struct.ByteBufferKaitaiStream;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.Parameter.PARAMETER_UPDATED;
import org.rabbitcontrol.rcp.model.Parameter.PARAMETER_VALUE_UPDATED;
import org.rabbitcontrol.rcp.model.RCPCommands.*;
import org.rabbitcontrol.rcp.model.RcpTypes.Command;
import org.rabbitcontrol.rcp.model.exceptions.*;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;
import org.rabbitcontrol.rcp.transport.ClientTransporter;
import org.rabbitcontrol.rcp.transport.ClientTransporterListener;

import java.util.HashSet;
import java.util.Set;

public class RCPClient extends RCPBase implements ClientTransporterListener {

    private ClientTransporter transporter;

    /* callback objects */
    final private Set<Add> addListener = new HashSet<Add>();

    final private Set<Remove> removeListener = new HashSet<Remove>();

    final private Set<StatusChange> statusChangedListener = new HashSet<StatusChange>();

    //------------------------------------------------------------
    //
    public RCPClient(final ClientTransporter _trans) {

        setTransporter(_trans);
    }

    //------------------------------------------------------------
    // client transporter
    public void setTransporter(final ClientTransporter _transporter) {

        if (transporter != null) {
            transporter.removeListener(this);
        }

        transporter = _transporter;

        if (transporter != null) {
            transporter.addListener(this);
        }
    }

    public ClientTransporter getTransporter() {
        return transporter;
    }

    //------------------------------------------------------------
    // api
    @Override
    public void dispose() {

        super.dispose();

        addListener.clear();
        removeListener.clear();
        statusChangedListener.clear();

        if (transporter != null) {
            transporter.disconnect();
            transporter = null;
        }
    }

    public void connect(final String _host, final int _port) {
        if (transporter != null) {
            transporter.connect(_host, _port);
        }
    }

    public void disconnect() {
        if (transporter != null) {
            transporter.disconnect();
        }
    }

    //------------------------------------------------------------
    //

    /**
     * Set Add listener
     *
     * @deprecated please use addAddListener instead
     * @param _listener the Add listener to set.
     */
    @Deprecated
    public void setAddListener(final Add _listener) {

        addAddListener(_listener);
    }

    /**
     * Add Add listener
     *
     * @param _listener the Add listener to add.
     */
    public void addAddListener(final Add _listener) {
        if (!addListener.contains(_listener)) {
            addListener.add(_listener);
        }
    }

    /**
     * Remove Add listener
     *
     * @param _listener the Add listener to remove.
     */
    public void removeAddListener(final Add _listener) {
        if (addListener.contains(_listener)) {
            addListener.remove(_listener);
        }
    }

    /**
     * Set Remove listener
     *
     * @deprecated please use addRemoveListener instead
     * @param _listener the Remove listener to set.
     */
    @Deprecated
    public void setRemoveListener(final Remove _listener) {

        addRemoveListener(_listener);
    }

    /**
     * Add Remove listener
     *
     * @param _listener the Remove listener to add.
     */
    public void addRemoveListener(final Remove _listener) {
        if (!removeListener.contains(_listener)) {
            removeListener.add(_listener);
        }
    }

    /**
     * Remove Remove listener
     *
     * @param _listener the Remove listener to remove.
     */
    public void removeRemoveListener(final Remove _listener) {
        if (removeListener.contains(_listener)) {
            removeListener.remove(_listener);
        }
    }

    /**
     * Set StatusChange listener
     *
     * @deprecated please use addStatusChangeListener instead
     * @param _listener the StatusChange listener to set.
     */
    @Deprecated
    public void setStatusChangedListener(final StatusChange _listener) {

        addStatusChangeListener(_listener);
    }

    /**
     * Add StatusChange listener
     *
     * @param _listener the StatusChange listener to add.
     */
    public void addStatusChangeListener(final StatusChange _listener) {
        if (!statusChangedListener.contains(_listener)) {
            statusChangedListener.add(_listener);
        }
    }

    /**
     * Remove StatusChange listener
     *
     * @param _listener the StatusChange listener to remove.
     */
    public void removeStatusChangeListener(final StatusChange _listener) {
        if (statusChangedListener.contains(_listener)) {
            statusChangedListener.remove(_listener);
        }
    }

    //------------------------------------------------------------
    //

    /**
     * update all dirty parameter
     */
    public void update() {

        if (transporter == null) {
            if (RCP.doDebugLogging) System.err.println("no transporter");
            return;
        }

        // update all dirty params
        synchronized (dirtyParams)
        {
            for (final IParameter parameter : dirtyParams) {
                try {

                    final Command cmd;
                    if (parameter.onlyValueChanged())
                    {
                        cmd = Command.UPDATEVALUE;
                    }
                    else
                    {
                        cmd = Command.UPDATE;
                    }

                    transporter.send(Packet.serialize(new Packet(cmd, parameter), false));
                }
                catch (final RCPException _e) {
                    System.err.println("could not update parameter: " + parameter.getId());
                }
            }
            dirtyParams.clear();
        }

    }

    /**
     * send value updated using the transporter
     *
     * @param _parameter
     *         the value to updated
     */
    private void update(final IParameter _parameter) throws RCPException {

        if (transporter == null) {
            System.err.println("no transporter");
            return;
        }

        // transport value
        transporter.send(Packet.serialize(new Packet(Command.UPDATE, _parameter), false));
    }

    /**
     * send init to server
     *
     * @throws RCPException
     *      in case Packet.serialize fails
     */
    public void initialize() throws RCPException {

        rootGroup.removeAllChildren();
        valueCache.clear();
        dirtyParams.clear();

        if (transporter == null) {
            System.err.println("no transporter");
            return;
        }

        // send to all clients
        transporter.send(Packet.serialize(new Packet(Command.INITIALIZE), false));
    }

    //------------------------------------------------------------
    //

    /**
     * called from transporter when data arrived
     *
     * @param _data
     *          received data
     */
    @Override
    public void received(final byte[] _data) {

        try {

            final Packet packet = Packet.parse(new ByteBufferKaitaiStream(_data), this);

            switch (packet.getCmd()) {
                case INVALID:
                    // nop
                    break;

                case INFO:
                    _info(packet);
                    break;

                case INITIALIZE:
                    // ignore
                    break;

                case DISCOVER:
                    // ignore
                    break;

                case UPDATEVALUE:
                case UPDATE:
                    _update(packet);
                    break;

                case REMOVE:
                    _remove(packet);
                    break;
            }
        }
        catch (final RCPUnsupportedFeatureException _e) {
            // nop
        }
        catch (final RCPDataErrorException _e) {
            // nop
        }
        catch (final RCPException _e) {
            _e.printStackTrace();
        }

    }

    private boolean checkVersion(final String version) {

        if ((version == null) || version.isEmpty()) return false;

        final String[] parts = version.split("\\.");

        if (parts.length == 3) {

            final int major = Integer.parseInt(parts[0]);
            final int minor = Integer.parseInt(parts[1]);
            final int patch = Integer.parseInt(parts[2]);

            // TODO: do a real check here
            if ((major >= 0) && (minor >= 0) && (patch >= 0)) {
                return true;
            }
        }

        if (RCP.doDebugLogging) System.out.println("version missmatch");

        return false;
    }


    private void _info(final Packet _packet) throws RCPException {

        final InfoData version_data = _packet.getDataAsInfoData();

        if (version_data != null) {
            System.out.println("server version: " + version_data.getVersion());
            System.out.println("server: " + version_data.getApplicationId());

            // check version
            if (checkVersion(version_data.getVersion()))
            {
                // ok
                initialize();
            }

        } else {
            // send back information

            final Packet version_packet = new Packet(Command.INFO);
            version_packet.setData(new InfoData(RCP.getRcpVersion(),
                                                 applicationId +
                                                " (" +
                                                RCP.getLibraryVersion() +
                                                ")"));

            transporter.send(Packet.serialize(version_packet, false));
        }
    }

    private void _update(final Packet _packet) {

        // try to convert data to TypeDefinition
        final IParameter parameter = _packet.getDataAsParameter();

        if (parameter == null) {
            return;
        }

        //updated value cache?
        final IParameter cached_parameter = valueCache.get(parameter.getId());
        if (cached_parameter == null) {

            // add parameter

            valueCache.put(parameter.getId(), parameter);

            parameter.setManager(this);

            // inform listener
            for (Add listener : addListener) {
                listener.parameterAdded(parameter);
            }

            parameter.addValueUpdateListener(new PARAMETER_VALUE_UPDATED() {

                @Override
                public void valueUpdated(final IParameter _parameter) {
                    for (ValueUpdate listener : valueUpdateListener)
                    {
                        listener.parameterValueUpdated(parameter);
                    }
                }
            });

            parameter.addUpdateListener(new PARAMETER_UPDATED() {

                @Override
                public void updated(final IParameter _parameter) {
                    // inform listener
                    for (Update listener : updateListener) {
                        listener.parameterUpdated(parameter);
                    }
                }
            });
        }
        else {

            // update chached parameter

            try {
                ((Parameter)cached_parameter).update(parameter);
            }
            catch (final RCPException _e) {
                System.err.println(String.format("could not update parameter(%d): %s",
                                                 parameter.getId(),
                                                 _e.getMessage()));
            }
        }
    }

    private void removeParameter(final IParameter _parameter) {

        _parameter.setManager(null);
        _parameter.clearUpdateListener();
        _parameter.clearValueUpdateListener();

        valueCache.remove(_parameter.getId());

        // inform listener
        for (Remove listener : removeListener) {
            listener.parameterRemoved(_parameter);
        }

        if (_parameter instanceof GroupParameter) {
            for (final IParameter child : ((GroupParameter)_parameter).getChildren()) {
                removeParameter(child);
            }
        }

        // remove from parent
        _parameter.setParent(null);

        // remove from dirty
        dirtyParams.remove(_parameter);
    }

    private void _remove(final Packet _packet) {

        try {
            final IdData id_data = _packet.getDataAsIdData();

            if (valueCache.containsKey(id_data.getId())) {
                removeParameter(valueCache.get(id_data.getId()));
            }
            else {
                System.err.printf("client: remove: no parameter with id: %s", id_data.getId());
            }
        } catch (final ClassCastException _e) {
            // nop
        }

    }

    @Override
    public void connected() {

        // request version from server
        try {
            transporter.send(Packet.serialize(new Packet(Command.INFO), false));
        }
        catch (final RCPException _e) {
            _e.printStackTrace();
        }
    }

    @Override
    public void disconnected() {

        // TODO: remove all parameter

    }
}
