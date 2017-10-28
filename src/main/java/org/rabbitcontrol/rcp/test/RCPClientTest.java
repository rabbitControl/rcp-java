package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.RCPCommands.*;
import org.rabbitcontrol.rcp.model.RCPParameter;
import org.rabbitcontrol.rcp.model.types.RCPTypeNumber;
import org.rabbitcontrol.rcp.model.types.RCPTypeSTRING;
import org.rabbitcontrol.rcp.test.websocket.client.WebsocketClientTransporter;
import org.rabbitcontrol.rcp.transport.RCPClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by inx on 29/11/16.
 */
public class RCPClientTest implements Add, Remove, Update {

    //------------------------------------------------------------
    //
    public static void main(final String[] args) {

        try {
            final RCPClientTest test = new RCPClientTest();

            while (true) {

                try {
                    Thread.sleep(1000);

                    //                    test.updateValue();
                }
                catch (final InterruptedException _e) {
                    break;
                }
            }
        }
        catch (final IOException _e) {
            _e.printStackTrace();
        }
        catch (final InterruptedException _e) {
            _e.printStackTrace();
        }
        catch (final URISyntaxException _e) {
            _e.printStackTrace();
        }

        System.out.println("finish client");
    }

    //------------------------------------------------------------
    //
    private final RCPClient toui;

    //------------------------------------------------------------
    //
    public RCPClientTest() throws IOException, URISyntaxException, InterruptedException {

        // create serializer and transporter
        //        final UDPClientTransporter transporter = new UDPClientTransporter("localhost",
        // 8888);
        //        final TCPClientTransporter transporter = new TCPClientTransporter("localhost",
        // 8888);
        final WebsocketClientTransporter transporter = new WebsocketClientTransporter("localhost",
                                                                                      8181);

        // create toi
        toui = new RCPClient(transporter);
        toui.setUpdateListener(this);
        toui.setAddListener(this);
        toui.setRemoveListener(this);

        // init
        toui.init();
    }

    int count;

    public void updateValue() {

        final Map<Integer, RCPParameter<?>> cache = toui.getValueCache();

        if (!cache.isEmpty()) {

            final Object[] objs = cache.keySet().toArray();

            final RCPParameter<?> parameter = cache.get(objs[0]);

            final RCPParameter<?> newParam = parameter.cloneEmpty();

            if (parameter.getType() instanceof RCPTypeSTRING) {

                final RCPParameter<String> stringParam = (RCPParameter<String>)newParam;

                if (stringParam.getValue() == null) {
                    stringParam.setValue("");
                }
                stringParam.setValue(stringParam.getValue() + "-");
            }
            else if (parameter.getType() instanceof RCPTypeNumber) {
                ((RCPParameter<Number>)newParam).setValue(count++);
            }

            toui.update(newParam);

        }
        else {
            System.err.println("no values");
        }

    }

    //------------------------------------------------------------
    //
    @Override
    public void added(final RCPParameter<?> _value) {

        System.out.println("client: added: " + _value.getId());
        //        toi.dumpCache();
        _value.dump();
    }

    @Override
    public void updated(final RCPParameter<?> _value) {

        System.out.println("client: updated: " + _value.getId());
        //        toi.dumpCache();
        _value.dump();
    }

    @Override
    public void removed(final RCPParameter<?> _value) {

        System.out.println("client: removed: " + _value.getId());
        //        toi.dumpCache();
        _value.dump();
    }
}
