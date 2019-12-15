package org.rabbitcontrol.rcp;

import io.kaitai.struct.ByteBufferKaitaiStream;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RCPCommands.*;
import org.rabbitcontrol.rcp.model.RcpTypes.Command;
import org.rabbitcontrol.rcp.model.exceptions.*;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;
import org.rabbitcontrol.rcp.transport.ClientTransporter;
import org.rabbitcontrol.rcp.transport.ClientTransporterListener;

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

        addListener = null;
        removeListener = null;
        statusChangedListener = null;

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

    /**
     * update all dirty parameter
     */
    public void update() {

        if (transporter == null) {
            System.err.println("no transporter");
        }

        // update all dirty params
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

    }

    private void _info(final Packet _packet) {

        final InfoData version_data = _packet.getDataAsInfoData();

        if (version_data != null) {
            System.out.println("server version: " + version_data.getVersion());
            if (!version_data.getApplicationId().isEmpty()) {
                System.out.println("server: " + version_data.getApplicationId());
            }
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
            if (addListener != null) {
                addListener.parameterAdded(parameter);
            }
        }
        else {

            try {
                ((Parameter)cached_parameter).update(parameter);
            }
            catch (RCPException _e) {
                System.err.println(String.format("could not update parameter(%d): %s",
                                                 parameter.getId(),
                                                 _e.getMessage()));
            }

            // inform listener
            if (updateListener != null) {
                updateListener.parameterUpdated(cached_parameter);
            }
        }
    }

    private void removeParameter(final IParameter _parameter) {

        valueCache.remove(_parameter);

        // inform listener
        if (removeListener != null) {
            removeListener.parameterRemoved(_parameter);
        }

        if (_parameter instanceof GroupParameter) {
            for (final IParameter child : ((GroupParameter)_parameter).getChildren()) {
                removeParameter(child);
            }
        }

        _parameter.setManager(null);
        _parameter.clearUpdateListener();
        _parameter.clearValueUpdateListener();

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

    }

    @Override
    public void disconnected() {

    }
}
