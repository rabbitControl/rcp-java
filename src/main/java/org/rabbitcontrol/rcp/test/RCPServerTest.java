package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.types.*;
import org.rabbitcontrol.rcp.test.websocket.server.WebsocketServerTransporterNetty;
import org.rabbitcontrol.rcp.transport.RCPServer;

import java.io.IOException;
import java.security.cert.CertificateException;

public class RCPServerTest implements RCPCommands.Update, RCPCommands.Init {

    public static boolean doAutoUpdate = true;

    //------------------------------------------------------------
    //
    public static void main(final String[] args) {

        try {
            final RCPServerTest test = new RCPServerTest();

            if (doAutoUpdate) {

                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // some automatic value update...
                        while (!Thread.interrupted()) {

                            try {
                                Thread.sleep(500);
                                test.updateVar2();
                            }
                            catch (final InterruptedException _e) {
                                break;
                            }
                        }
                    }
                });
                t.start();
            }
        }
        catch (final InterruptedException _e) {
            _e.printStackTrace();
        }
        catch (final CertificateException _e) {
            _e.printStackTrace();
        }
        catch (final IOException _e) {
            _e.printStackTrace();
        }
    }

    //------------------------------------------------------------
    //
    private final RCPServer toi;

    private final RCPParameter<String> theValueString;

    private final RCPParameter<Double> theValueDouble;

    private final RCPParameter<Integer> theValueInt;

    private final RCPParameter<Boolean> theValueBool;

    private final RCPParameterNumber<Long> theValueLong;

    private int counter;

    //------------------------------------------------------------
    //
    public RCPServerTest() throws IOException, CertificateException, InterruptedException {

        // a udp transporter
        //        final UDPServerTransporter transporter = new UDPServerTransporter(8181);
        //        transporter.setTargetPort(61187);

        // a tcp transporter
        //        final TCPServerTransporter transporter = new TCPServerTransporter(8888);

        // a websocket transporter
        final WebsocketServerTransporterNetty transporter = new WebsocketServerTransporterNetty(
                10000);

        // create toi
        toi = new RCPServer(transporter);
        toi.setUpdateListener(this);
        toi.setInitListener(this);

        // create values
        theValueString = new RCPParameter<String>(1, new RCPTypeSTRING());
        theValueString.setValue("This is a text encoded in utf-8. let's test it: ¬”#£œæýýý‘");
        theValueString.setDescription("test description 2");
        theValueString.setLabel("text label");
        theValueString.setUserdata("some user data?".getBytes());

        theValueDouble = new RCPParameter<Double>(2, new RCPTypeFLOAT64(0.D, 10.D));
        theValueDouble.setLabel("a double");
        theValueDouble.setDescription("double description");
        theValueDouble.setValue(3.14);

        theValueInt = new RCPParameter<Integer>(3, new RCPTypeINT32());
        theValueInt.setLabel("INT LABEL");
        theValueInt.setValue(333);

        theValueBool = new RCPParameter<Boolean>(4, new RCPTypeBOOL());
        theValueBool.setLabel("toggle button");
        theValueBool.setValue(true);

        theValueLong = new RCPParameterNumber<Long>(5, new RCPTypeINT64());
        theValueLong.setValue((long)10);
        theValueLong.setLabel("a long number");

        // add the values to toi
        toi.add(theValueString);
        toi.add(theValueDouble);
        toi.add(theValueInt);
        toi.add(theValueBool);
    }

    public void updateVar1() {

        RCPParameter<Long> newVal = theValueLong.cloneEmpty();
        newVal.setValue(theValueLong.getValue() + 1);

        toi.update(newVal);
    }

    public void updateVar2() {

        RCPParameter<String> newVal = theValueString.cloneEmpty();

        newVal.setValue("content: " + counter++);
        toi.update(newVal);
    }

    //------------------------------------------------------------
    //
    @Override
    public void updated(final RCPParameter<?> _value) {

        // updated from client
        System.out.println("server: updated: " + _value.getId() + " : " + _value.getValue());

        toi.dumpCache();
    }

    @Override
    public void init() {

    }
}
