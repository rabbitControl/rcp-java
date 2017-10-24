package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RCPTypes.Command;

import java.util.Objects;

/**
 * Created by inx on 30/11/16.
 */
public class RCPServer extends RCPBase {

    //------------------------------------------------------------
    // callback objects
    private RCPCommands.Init initListener;

    //------------------------------------------------------------
    //
    public RCPServer(final RCPTransporter _trans) {

        super(_trans);
    }

    //------------------------------------------------------------
    //
    public void setInitListener(final RCPCommands.Init _listener) {

        initListener = _listener;
    }

    //------------------------------------------------------------
    //
    public void add(final RCPParameter<?> _value) {

        operateOnCache(valueCache -> {
            if (!valueCache.containsKey((int)_value.getId())) {
                // added
                valueCache.put((int)_value.getId(), _value);
            }
            else {

                if (!Objects.equals(valueCache.get((int)_value.getId()), _value)) {
                    System.err.println("different object with same ID!!!");
                } else {
                    System.out.println("already added value with this id - ignore");
                }
            }
        });

        if (transporter != null) {
            // TODO: send to all clients
            final RCPPacket packet = new RCPPacket(Command.ADD, _value);
            transporter.send(packet);
        }
    }

    public void remove(final RCPParameter<?> _value) {

        operateOnCache(valueCache -> {

            if (valueCache.containsKey((int)_value.getId())) {
                // removed
                valueCache.remove(_value.getId());
            }
            else {
                System.out.println("value not in cache - ignore");
            }
        });


        if (transporter != null) {
            final RCPPacket packet = new RCPPacket(Command.REMOVE, _value);
            transporter.send(packet);
        }
    }

    //------------------------------------------------------------
    //
    @Override
    public void received(final RCPPacket _packet) {

        if (_packet == null) {
            System.err.println("no packet... ");
            return;
        }

        if (_packet.getCmd() == Command.UPDATE) {

            final RCPParameter<?> val = (RCPParameter<?>)_packet.getData();

            if (updateListener != null) {
                updateListener.updated(val);
            }

            // update all clients...
            update(val);
        }
        else if (_packet.getCmd() == Command.VERSION) {

            // try to convert to version object
            System.out.println("version object yet to be specified");
        }
        else if (_packet.getCmd() == Command.INIT) {

            if (_packet.getData() != null) {
                // TODO: send full description of only one parameter
                final RCPParameter<?> val = (RCPParameter<?>)_packet.getData();
            }

            _init(_packet);
        }
        else {

            System.err.println("not implemented command: " + _packet.getCmd());
        }

    }

    private void _init(final RCPPacket _packet) {

        System.out.println("GOT INIT");

        operateOnCache(valueCache -> {

            // init with all values
            valueCache.forEach((_s, _parameter) -> {

                System.out.println("sending ::: " + _parameter.getDescription());

                final RCPPacket packet = new RCPPacket(Command.ADD, _parameter);
                transporter.send(packet);
            });

        });


        if (initListener != null) {
            initListener.init();
        }
    }
}
