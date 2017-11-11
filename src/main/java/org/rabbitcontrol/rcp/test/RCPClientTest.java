package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.RCPCommands.*;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.NumberParameter;
import org.rabbitcontrol.rcp.model.parameter.StringParameter;
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
    private final RCPClient rcp;

    //------------------------------------------------------------
    //
    public RCPClientTest() throws IOException, URISyntaxException, InterruptedException {

        // create serializer and transporter
//        final UDPClientTransporter transporter = new UDPClientTransporter("localhost", 8888);
        //        final TCPClientTransporter transporter = new TCPClientTransporter("localhost",
        // 8888);
        final WebsocketClientTransporter transporter = new WebsocketClientTransporter("localhost", 10000);

        // create toi
        rcp = new RCPClient(transporter);
        rcp.setUpdateListener(this);
        rcp.setAddListener(this);
        rcp.setRemoveListener(this);

        transporter.connect();

        // init
        rcp.init();
    }

    int count;

    public void updateValue() {

        final Map<Integer, IParameter> cache = rcp.getValueCache();

        if (!cache.isEmpty()) {

            final Object[] objs = cache.keySet().toArray();

            final IParameter parameter = cache.get(objs[0]);

            if (parameter instanceof StringParameter) {

                final StringParameter stringParam = (StringParameter)parameter;

                if (stringParam.getValue() == null) {
                    stringParam.setValue("-");
                } else {
                    stringParam.setValue(stringParam.getValue() + "-");
                }
            }
            else if (parameter instanceof NumberParameter) {
                ((NumberParameter)parameter).setValue(count++);
            }
        }
        else {
            System.err.println("no values");
        }
    }

    //------------------------------------------------------------
    //
    @Override
    public void added(final IParameter _value) {

        System.out.println("client: added: " + _value.getId());
        //        toi.dumpCache();
        _value.dump();
    }

    @Override
    public void updated(final IParameter _value) {

        System.out.println("client: updated: " + _value.getId());
        //        toi.dumpCache();
        _value.dump();
    }

    @Override
    public void removed(final IParameter _value) {

        System.out.println("client: removed: " + _value.getId());
        //        toi.dumpCache();
        _value.dump();
    }
}
