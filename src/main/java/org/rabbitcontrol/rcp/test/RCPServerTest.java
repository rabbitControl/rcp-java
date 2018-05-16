package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.RCPCommands;
import org.rabbitcontrol.rcp.model.RcpTypes;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.interfaces.INumberParameter;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.test.websocket.server.WebsocketServerTransporterNetty;
import org.rabbitcontrol.rcp.transport.RCPServer;

import java.awt.*;
import java.io.IOException;
import java.security.cert.CertificateException;

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
                                Thread.sleep(10000);
                                test.updateVar2();
                            }
                            catch (final InterruptedException _e) {
                                break;
                            }
                            catch (RCPParameterException _e) {
                                _e.printStackTrace();
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
        catch (RCPParameterException _e) {
            _e.printStackTrace();
        }
    }

    //------------------------------------------------------------
    //
    private final RCPServer rabbit;

    private StringParameter theValueString;

    private INumberParameter<Double> theValueDouble;

    private INumberParameter<Float> theValueFloat;

    private INumberParameter<Float> theValueFloat2;

    private INumberParameter<Integer> theValueInt;

    private BooleanParameter theValueBool;

    private INumberParameter<Long> theValueLong;

    private EnumParameter enumParameter;

    private RGBParameter colorparam;

    private GroupParameter groupParam1;

    private GroupParameter groupParam2;

    private GroupParameter groupParam3;

    private int counter;

    //------------------------------------------------------------
    //
    public RCPServerTest() throws
                           IOException,
                           CertificateException,
                           InterruptedException,
                           RCPParameterException {

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

        //------------------------------------------------------------
        //------------------------------------------------------------

        //        final Byte[][][] bla = ;

        final ArrayParameter<Byte[][][], Byte> arr = rabbit.createArrayParameter("testarray",
                                                                                 RcpTypes.Datatype.INT8,
                                                                                 new Byte[][][] {
                { { 1,
                                                                                                      2 },
                                                                                                    { 3,
                                                                                                      4 } },
                                                                                                  { { 11,
                                                                                                      12 },
                                                                                                    { 13,
                                                                                                      14 } },
                                                                                                  { { 21,
                                                                                                      22 },
                                                                                                    { 23,
                                                                                                      24 } } },
                                                                                 3,
                                                                                 2,
                                                                                 2);

        arr.getValue()[0][0][0] = 111;

        //        ArrayParameter<List<String>, String> bla = new ArrayParameter<List<String>,
        // String>(
        //                (short)1, null);
        //
        //        ArrayList<String> list = new ArrayList<String>();
        //        list.add("aa");
        //        bla.setValue(list);

        //
        //        ArrayParameter<List<List<Integer>>, Integer> arr = ArrayParameter.create(
        // (short)1, Integer
        //                .class, 3, 2);
        //
        //
        //        rabbit.addParameter(arr);
        //        rabbit.update();

        //        // group 1
        //        groupParam1 = rabbit.createGroupParameter("GROuP");
        //        //        groupParam1.setWidgetType((short)123);
        //
        //        // string
        //        theValueString = rabbit.createStringParameter("text label", groupParam1);
        //        theValueString.setValue("This is a text encoded in utf-8. let's test it:
        // ¬”#£œæýýý‘");
        //        theValueString.setDescription("test description 2");
        //        theValueString.setUserdata("some user data?".getBytes());
        //
        //        theValueString.setLanguageLabel("eng", "another label");
        //        theValueString.setLanguageLabel("deu", "deutsches label");
        //        theValueString.setLanguageLabel("fra", "une label francaise");
        //
        //        theValueString.setLanguageDescription("eng", "english description");
        //        theValueString.setLanguageDescription("deu", "deutsche beschreibung");
        //        theValueString.setLanguageDescription("fra", "escription francoise");

        //        // enum
        //        enumParameter = rabbit.createEnumParameter("enum test", groupParam1);
        //        enumParameter.getEnumTypeDefinition().addEntry("uno");
        //        enumParameter.getEnumTypeDefinition().addEntry("dos");
        //        enumParameter.getEnumTypeDefinition().addEntry("tres");
        //        enumParameter.getEnumTypeDefinition().addEntry("quattro");
        //        enumParameter.getEnumTypeDefinition().setDefault("tres");
        //        enumParameter.setValue("dos");
        //
        // RGB color
        //        colorparam = rabbit.createRGBParameter("a color", groupParam1);
        //        colorparam.setValue(Color.CYAN);
        //
        //
        //        theValueString.getTypeDefinition().setDefault("defaultstring");
        //
        //        // double
        //        theValueDouble = rabbit.createDoubleParameter("a double", groupParam1);
        //        theValueDouble.getTypeDefinition().setMaximum(1000.D);
        //        theValueDouble.getTypeDefinition().setMinimum(0.D);
        //        theValueDouble.setDescription("double description");
        //        theValueDouble.setValue(3.14);
        //
        //        // float
        //        theValueFloat = rabbit.createFloatParameter("FLOAT", groupParam1);
        //        theValueFloat.setValue(123.F);
        //
        //        // float
        //        theValueFloat2 = rabbit.createFloatParameter("FLOAT 2", groupParam1);
        //        theValueFloat2.setValue(123.F);
        //
        //        // int
        //        theValueInt = rabbit.createInt32Parameter("INt LABEL", groupParam1);
        //        theValueInt.setValue(333);
        //
        //        // boolean
        //        theValueBool = rabbit.createBooleanParameter("toggle button", groupParam1);
        //        theValueBool.setValue(true);

        // long
        //        theValueLong = rabbit.createInt64Parameter("a long number");
        //        theValueLong.setValue((long)10);

        //        // group 2
        //        groupParam2 = rabbit.createGroupParameter("foo");
        //
        //        // group 3
        //        groupParam3 = rabbit.createGroupParameter("group 3");
        //        //groupParam3.setWidgetType((short)123);

        //        groupParam2.addChildren(theValueInt,
        //                                theValueBool,
        //                                theValueLong);

        //        rabbit.addParameter(theValueFloat, groupParam2);
        //
        //        rabbit.addParameter(theValueLong, groupParam3);

        rabbit.update();
    }

    public void updateVar1() {

    }

    boolean init = false;

    public void updateVar2() throws RCPParameterException {

        if (!init) {

            //            System.out.println("adding group");
            theValueInt = rabbit.createInt32Parameter("INt LABEL");
            theValueInt.setValue(333);
            //
            //            groupParam2 = rabbit.createGroupParameter("foo");
            //
            //            rabbit.addParameter(theValueInt, groupParam2);
            //
            rabbit.update();

            init = true;
        }

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
