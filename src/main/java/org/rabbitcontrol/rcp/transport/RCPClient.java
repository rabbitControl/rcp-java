package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Command;

import java.util.Map;

public class RCPClient extends RCPBase {

    // callback objects
    private RCPCommands.Add addListener;

    private RCPCommands.Remove removeListener;

    //------------------------------------------------------------
    //
    public RCPClient(final RCPTransporter _trans) {

        super(_trans);
    }

    //------------------------------------------------------------
    //
    public void setAddListener(final RCPCommands.Add _listener) {

        addListener = _listener;
    }

    public void setRemoveListener(final RCPCommands.Remove _listener) {

        removeListener = _listener;
    }

    //------------------------------------------------------------
    //

    /**
     * send init to server
     */
    public void init() {

        operateOnCache(new RCPCacheOperator() {

            @Override
            public void operate(final Map<Integer, RCPParameter<?>> valueCache) {
                // clear cache?
                valueCache.clear();
            }
        });

        if (transporter != null) {
            // send to all clients
            final RCPPacket packet = new RCPPacket(Command.INIT);
            transporter.send(packet);
        }
    }

    //------------------------------------------------------------
    //
    @Override
    public void received(final RCPPacket _packet) {

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

    private void _update(final RCPPacket _packet) {

        // try to convert data to TypeDefinition
        try {

            final RCPParameter<?> val = (RCPParameter<?>)_packet.getData();

            switch (_packet.getCmd()) {
                case ADD:

                    operateOnCache(new RCPCacheOperator() {

                        @Override
                        public void operate(final Map<Integer, RCPParameter<?>> valueCache) {
                            // added to value cache?
                            if (!valueCache.containsKey((int)val.getId())) {

                                valueCache.put((int)val.getId(), val);

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

                    break;

                case REMOVE:

                    operateOnCache(new RCPCacheOperator() {

                        @Override
                        public void operate(final Map<Integer, RCPParameter<?>> valueCache) {
                            if (valueCache.containsKey((int)val.getId())) {
                                final RCPParameter<?> removed = valueCache.remove(val.getId());

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
                        public void operate(final Map<Integer, RCPParameter<?>> valueCache) {
                            //updated value cache?
                            final RCPParameter<?> cached = valueCache.get((int)val.getId());
                            if (cached != null) {
                                cached.update(val);

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
