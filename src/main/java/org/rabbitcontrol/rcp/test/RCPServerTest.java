package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.ParameterFactory;
import org.rabbitcontrol.rcp.model.RCPCommands;
import org.rabbitcontrol.rcp.model.interfaces.INumberParameter;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.test.websocket.server.WebsocketServerTransporterNetty;
import org.rabbitcontrol.rcp.transport.RCPServer;

import java.awt.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Random;

public class RCPServerTest implements RCPCommands.Update, RCPCommands.Init {

    public static boolean doAutoUpdate = true;

    static Color[] colors = { Color.CYAN,
                              Color.BLUE,
                              Color.GREEN,
                              Color.MAGENTA,
                              Color.pink,
                              Color.RED };

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
                                Thread.sleep(5000);
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
    private final RCPServer rabbit;

    private final StringParameter theValueString;

    private INumberParameter<Double> theValueDouble;

    private INumberParameter<Float> theValueFloat;

    private final INumberParameter<Integer> theValueInt;

    private final BooleanParameter theValueBool;

    private final INumberParameter<Long> theValueLong;

    private final EnumParameter enumParameter;

    private final RGBParameter colorparam;

    private final GroupParameter groupParam;

    private final GroupParameter groupParam2;

    private final GroupParameter groupParam3;

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
        final WebsocketServerTransporterNetty transporter = new WebsocketServerTransporterNetty();

        // create rabbit
        rabbit = new RCPServer(transporter);
        //rabbit.addTransporter(transporter);

        rabbit.setUpdateListener(this);
        rabbit.setInitListener(this);

        transporter.bind(10000);


        enumParameter = new EnumParameter((short)111);
        enumParameter.getEnumTypeDefinition().addEntry("uno");
        enumParameter.getEnumTypeDefinition().addEntry("dos");
        enumParameter.getEnumTypeDefinition().addEntry("tres");
        enumParameter.getEnumTypeDefinition().addEntry("quattro");
        enumParameter.getEnumTypeDefinition().setDefault(3);
        enumParameter.setLabel("enum test");
        enumParameter.setValue(1);

        colorparam = new RGBParameter((short)112);
        colorparam.setValue(Color.CYAN);
        colorparam.setLabel("a color");

        UInt8Parameter u8p = new UInt8Parameter((short)122);
        u8p.getTypeDefinition().setMinimum((byte)1);
        u8p.getTypeDefinition().setMaximum((byte)200);
        u8p.setValue((byte)200);
        int us = u8p.getValueUnsigned();

        // create values
        theValueString = ParameterFactory.createStringParameter((short)1);
        theValueString.setValue("This is a text encoded in utf-8. let's test it: ¬”#£œæýýý‘");
        theValueString.setDescription("test description 2");
        theValueString.setLabel("text label");
        theValueString.setUserdata("some user data?".getBytes());

        theValueString.getTypeDefinition().setDefault("defaultstring");

        theValueDouble = ParameterFactory.createNumberParameter((short)2, Double.class);
        theValueDouble.getTypeDefinition().setMaximum(1000.D);
        theValueDouble.getTypeDefinition().setMinimum(0.D);
        theValueDouble.setLabel("a double");
        theValueDouble.setDescription("double description");
        theValueDouble.setValue(3.14);

        theValueFloat = ParameterFactory.createNumberParameter((short)44, Float.class);
        theValueFloat.setLabel("FLOAT");

        theValueInt = ParameterFactory.createNumberParameter((short)133, Integer.class);
        theValueInt.setLabel("INT LABEL");
        theValueInt.setValue(333);

        theValueBool = ParameterFactory.createBooleanParameter((short)4);
        theValueBool.setLabel("toggle button");
        theValueBool.setValue(true);

        theValueLong = ParameterFactory.createNumberParameter((short)5, Long.class);
        theValueLong.setValue((long)10);
        theValueLong.setLabel("a long number");

        groupParam = new GroupParameter((short)10);
        groupParam.setLabel("GROuP");

        groupParam2 = new GroupParameter((short)11);
        groupParam2.setLabel("group 2");

        groupParam3 = new GroupParameter((short)12);
        groupParam3.setLabel("group 3");

        groupParam.addChildren(colorparam, enumParameter, theValueDouble, theValueString);

        //        groupParam2.addChildren(theValueInt,
        //                                theValueBool,
        //                                theValueLong);

        groupParam2.addChildren(theValueInt, theValueBool);
        groupParam3.addChildren(theValueLong);

        groupParam.addChild(groupParam2);
        groupParam2.addChild(groupParam3);

        // addParameter the values to rabbit
        //        rabbit.addParameter(colorparam);
        //        rabbit.addParameter(enumParameter);
        //        rabbit.addParameter(theValueDouble);
        //        rabbit.addParameter(theValueString);
        //        rabbit.addParameter(theValueInt);
        //        rabbit.addParameter(theValueBool);
                //rabbit.addParameter(theValueLong);
        //        rabbit.addParameter(theValueFloat);

        rabbit.addParameter(groupParam);
        //        rabbit.addParameter(groupParam2);
    }

    public void updateVar1() {

    }

    public void updateVar2() {

    }

    //------------------------------------------------------------
    //
    @Override
    public void parameterUpdated(final IParameter _value) {

        System.out.println("server update:");
        _value.dump();
    }

    @Override
    public void init() {

    }
}
