package org.rabbitcontrol.rcp.transport;

import io.kaitai.struct.ByteBufferKaitaiStream;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RCPCommands.*;
import org.rabbitcontrol.rcp.model.RcpTypes.Command;
import org.rabbitcontrol.rcp.model.exceptions.*;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;

import java.io.IOException;

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
        for (final IParameter parameter : dirtyParams) {
            update(parameter);
        }

        dirtyParams.clear();
    }

    /**
     * send value updated using the transporter
     *
     * @param _parameter
     *         the value to updated
     */
    private void update(final IParameter _parameter) {

         if (transporter != null) {

            // transport value
            try {
                transporter.send(Packet.serialize(new Packet(Command.UPDATE, _parameter), false));
            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }
        }
    }

    /**
     * send init to server
     */
    public void init() {

        valueCache.clear();

        if (transporter != null) {

            // send to all clients
            try {
                transporter.send(Packet.serialize(new Packet(Command.INITIALIZE), false));
            }
            catch (final IOException _e) {
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


            switch (_packet.getCmd()) {
                case INVALID:
                    // nop
                    break;
                case VERSION:
                    // try to convert to version object
                    System.out.println("version object yet to be specified");
                    break;
                case INITIALIZE:
                    // ignore
                    break;
                case DISCOVER:
                    // ignore
                    break;
                case UPDATE:
                    _update(_packet);
                    break;
                case REMOVE:
                    _remove(_packet);
                    break;
                case UPDATEVALUE:
                    break;
            }

        }
        catch (final RCPDataErrorException _e) {
            _e.printStackTrace();
        }
        catch (final RCPUnsupportedFeatureException _e) {
            _e.printStackTrace();
        }

    }

    private void _update(final Packet _packet) {

        // try to convert data to TypeDefinition
        try {

            final IParameter parameter = _packet.getDataAsParameter();

            //updated value cache?
            final IParameter cached_parameter = valueCache.get(parameter.getId());
            if (cached_parameter == null) {

                // add parameter

                valueCache.put(parameter.getId(), parameter);

                ((Parameter)parameter).setManager(this);

                // inform listener
                if (addListener != null) {
                    addListener.parameterAdded(parameter);
                }
            }
            else {

                try {
                    ((Parameter)cached_parameter).update(parameter);

                    // inform listener
                    if (updateListener != null) {
                        updateListener.parameterUpdated(cached_parameter);
                    }
                }
                catch (RCPException _e) {
                    _e.printStackTrace();
                }

            }

        } catch (final ClassCastException _e) {
            // nop
        }

    }

    private void removeParameter(IParameter _parameter) {

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
    }

    private void _remove(final Packet _packet) {

        try {
            final IParameter parameter = _packet.getDataAsParameter();

            if (valueCache.containsKey(parameter.getId())) {
                removeParameter(valueCache.get(parameter.getId()));
            }
            else {
                System.err.println("client: removed: does not know value with id:" + " " + parameter
                        .getId());
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
