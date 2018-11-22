package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.RCPCommands.Init;
import org.rabbitcontrol.rcp.model.RCPCommands.Update;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.Widget;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.interfaces.INumberParameter;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.model.types.Range;
import org.rabbitcontrol.rcp.model.types.Vector3;
import org.rabbitcontrol.rcp.model.widgets.*;
import org.rabbitcontrol.rcp.test.websocket.server.WebsocketServerTransporterNetty;
import org.rabbitcontrol.rcp.transport.RCPServer;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * start with
 * ./gradlew run -Dexec.args="-c exposeSingleFLoat"
 */
public class RCPServerTest implements Update, Init {

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

        String config_string = "exposeSingleFLoat";
        int port = 10000;

        for (int i = 0; i < args.length; i++) {

            final String arg = args[i];

            if ("--config".equals(arg) || "-c".equals(arg)) {
                i++;
                if (i <args.length) {
                    System.out.println("setting config string: " + args[i]);
                    config_string = args[i];
                }
            } else if ("-m".equals(arg)) {

                System.out.println("callable method-names:");
                final Method[] methods = RCPServerTest.class.getDeclaredMethods();

                for (final Method method : methods) {
                    final Class<?>[] ex_types = method.getExceptionTypes();
                    if (!method.getName().startsWith("_") &&
                        Arrays.toString(ex_types).contains
                            ("RCPParameterException"))
                    {
                        System.out.println(method.getName());
                    }
                }

                System.out.println("\nuse -c method-name to call the method on start");

                return;
            } else if ("-h".equals(arg)) {
                System.out.println("help");
                return;
            } else if ("-p".equals(arg)) {
                i++;

                if (i <args.length) {
                    port = Integer.parseInt(args[i]);
                    System.out.println("setting port: " + port);
                }
            }
        }

        try {
            final RCPServerTest test = new RCPServerTest(config_string, port);

//            if (doAutoUpdate) {
//
//                final Thread t = new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // some automatic value update...
//                        while (!Thread.interrupted()) {
//
//                            try {
//                                Thread.sleep(10000);
//                                test.updateVar2();
//                            }
//                            catch (final InterruptedException _e) {
//                                break;
//                            }
//                        }
//                    }
//                });
//                t.start();
//            }
        }
        catch (final RCPParameterException _e) {
            _e.printStackTrace();
        }
    }

    //------------------------------------------------------------
    //
    private final RCPServer rabbit;

    private StringParameter theValueString;

    private INumberParameter<Double> theValueDouble;

    private INumberParameter<Float> theValueFloat1;

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
    public RCPServerTest(final String config, int port) throws RCPParameterException {

        // a udp transporter
        //        final UDPServerTransporter transporter = new UDPServerTransporter(8181);
        //        transporter.setTargetPort(61187);

        // a tcp transporter
        //        final TCPServerTransporter transporter = new TCPServerTransporter(8888);

        // a websocket transporter
        final WebsocketServerTransporterNetty transporter = new WebsocketServerTransporterNetty();
        ///final WebsocketServerTransporterNetty transporter2 = new
        // WebsocketServerTransporterNetty();

        // create rabbit
        rabbit = new RCPServer(transporter);
        //rabbit.addTransporter(transporter2);

        rabbit.setUpdateListener(this);
        rabbit.setInitListener(this);

        transporter.bind(port);

        //------------------------------------------------------------
        //------------------------------------------------------------
        // expose parameters

        try {
            final Method config_method = RCPServerTest.class.getDeclaredMethod(config);

            System.out.println("calling method: " + config_method.getName());
            config_method.invoke(this);
        }
        catch (final NoSuchMethodException _e) {
            _e.printStackTrace();
        }
        catch (final IllegalAccessException _e) {
            _e.printStackTrace();
        }
        catch (final InvocationTargetException _e) {
            _e.printStackTrace();
        }

        //        exposeRange();
//        exposeArray();
//        exposeList();
//        exposeGroupWithCustomWidget();
//        exposeLong();
//        exposeParameterInGroups();
//        exposeWithWidget();

        rabbit.update();
    }


    private void exposeRange() throws RCPParameterException {

        final RangeParameter<Byte> rangeParameter = rabbit.createRangeParameter("range",
                                                                                Byte.class);

        rangeParameter.setValue(new Range<Byte>((byte)2, (byte)4));
        rangeParameter.getTypeDefinition().setDefault(new Range<Byte>((byte)1, (byte)10));
        rangeParameter.getRangeDefinition().getElementType().setMinimum((byte)0);
        rangeParameter.getRangeDefinition().getElementType().setMaximum((byte)20);
    }

    private void exposeArray() throws RCPParameterException {

        //--------------------------------------------
        // expose 1 dimensions array of strings with 3 elements
        final ArrayParameter<String[], String> arr3 = rabbit.createArrayParameter("String " +

                                                                                  "Array",
                                                                                  Datatype.STRING,
                                                                                  3);
        arr3.setValue(new String[]{ "test", "string", "array" });


        //--------------------------------------------
        // expose one-dimensional array of doubles with 6 elements
        final ArrayParameter<Double[], Double> arr1 = rabbit.createArrayParameter("double array",
                                                                                  Datatype.FLOAT64,
                                                                                  6);

        arr1.getArrayDefinition().setDefault(new Double[] { 1.1, 2.2, 3.3, 4.4, 5.5, 6.6 });
        arr1.setValue(new Double[]{ 10.1, 20.2, 30.3, 40.4, 50.5, 60.6 });


        //--------------------------------------------
        // expose 2-dimensional array of integer: 3, 2
        final ArrayParameter<Integer[][], Integer> arr2 = rabbit.createArrayParameter("int 2D",
                                                                                      Datatype.INT32,
                                                                                      3,
                                                                                      2);

        arr2.setValue(new Integer[][]{ {1024, 2048},
                                       {3333, 6666},
                                       {10000, 10001}});


        //--------------------------------------------
        // expose array with 3 dimensions: 4, 2, 2
        final ArrayParameter<Byte[][][], Byte> arr = rabbit.createArrayParameter("byte-array " +
                                                                                 "3-2-2",
                                                                                 Datatype.INT8,
                                                                                 4,
                                                                                 2,
                                                                                 2);

        // set default value of element type
        arr.getElementType().setDefault((byte)123);

        // set value
        arr.setValue(new Byte[][][] { { { 1, 2 }, { 3, 4 } },
                                      { { 11, 12 }, { 13, 14 } },
                                      { { 21, 22 }, { 23, 24 } },
                                      { { 31, 32 }, { 33, 34 } } });

        // change one element in the array
        arr.getValue()[0][0][0] = 111;
    }

    private void exposeList() {

//        final ArrayParameter<List<List<List<Byte>>>, Byte> list = rabbit.createArrayParameter(
//                "byte-list",
//
//                Datatype.INT8,
//                3);
//
//        list.setMinDimSizes(3, 0, 2) list.setMaxDimSizes(10, -1, 2)

    }

    private void exposeWithWidget() throws RCPParameterException {

        // string
        theValueString = rabbit.createStringParameter("string with widget");
        theValueString.setValue("This is a text encoded in utf-8. let's test it:");

        theValueString.setDescription("test description 2");
        theValueString.setUserdata("some user data?".getBytes());

        theValueString.setLanguageLabel("eng", "another label");
        theValueString.setLanguageLabel("deu", "deutsches label");
        theValueString.setLanguageLabel("fra", "une label francaise");

        theValueString.setLanguageDescription("eng", "english description");
        theValueString.setLanguageDescription("deu", "deutsche beschreibung");
        theValueString.setLanguageDescription("fra", "escription francoise");

        theValueString.getTypeDefinition().setDefault("defaultstring");

        final Widget str_widget = new TextboxWidget();
        theValueString.setWidget(str_widget);
    }

    private void exposeGroupWithCustomWidget() throws RCPParameterException {

        // setup custom widget
        final CustomWidget custom_group_widget = new CustomWidget();
        custom_group_widget.setUuid(UUID.fromString("69babd06-72d9-11e8-adc0-fa7ae01bbebc"));
        custom_group_widget.setConfig("testconfig".getBytes());

        // group 1
        groupParam1 = rabbit.createGroupParameter("GROuP");
        groupParam1.setWidget(custom_group_widget);

        // add parameter to group
        rabbit.createInt8Parameter("int 1", groupParam1);
        rabbit.createInt8Parameter("int 2", groupParam1);
        rabbit.createInt8Parameter("int 3", groupParam1);
    }

    private void exposeLong() throws RCPParameterException {

        theValueLong = rabbit.createInt64Parameter("a long number");
        theValueLong.setValue(10L);
    }

    private void exposeSingleFLoat() throws RCPParameterException {



        for (int i = 0; i <1; i++) {
            final INumberParameter<Float> p = rabbit.createFloatParameter("FLOAT", groupParam1);
            p.setValue(123.F);
            p.getTypeDefinition().setMinimum(0.F);
            p.getTypeDefinition().setMaximum(200.F);

            rabbit.addParameter(p);
        }


    }

    private void exposeParameterInGroups() throws RCPParameterException {

        // group 1
        groupParam1 = rabbit.createGroupParameter("GROUP 1");

        // enumeration
        enumParameter = rabbit.createEnumParameter("enum test", groupParam1);
        enumParameter.getEnumTypeDefinition().addEntry("uno");
        enumParameter.getEnumTypeDefinition().addEntry("dos");
        enumParameter.getEnumTypeDefinition().addEntry("tres");
        enumParameter.getEnumTypeDefinition().addEntry("quattro");
        enumParameter.getEnumTypeDefinition().setDefault("tres");
        enumParameter.setValue("dos");

        //RGB color
        colorparam = rabbit.createRGBParameter("a color", groupParam1);
        colorparam.setValue(Color.CYAN);

        // string
        theValueString = rabbit.createStringParameter("a string");
        theValueString.setValue("This is a text encoded in utf-8. let's test it:");
        theValueString.setDescription("description for string");

        // double
        theValueDouble = rabbit.createDoubleParameter("a double", groupParam1);
        theValueDouble.getTypeDefinition().setMaximum(1000.D);
        theValueDouble.getTypeDefinition().setMinimum(0.D);
        theValueDouble.setDescription("double description");
        theValueDouble.setValue(3.14);

        // float
        theValueFloat1 = rabbit.createFloatParameter("FLOAT", groupParam1);
        theValueFloat1.setValue(123.F);

        // float
        theValueFloat2 = rabbit.createFloatParameter("FLOAT 2", groupParam1);
        theValueFloat2.setValue(33.F);

        // int
        theValueInt = rabbit.createInt32Parameter("INT32 LABEL", groupParam1);
        theValueInt.setValue(333);

        // boolean
        theValueBool = rabbit.createBooleanParameter("toggle button", groupParam1);
        theValueBool.setValue(true);

        // group 2 - as subgroup of group1
        groupParam2 = rabbit.createGroupParameter("foo", groupParam1);

        // group 3 - as subgroup of group1
        groupParam3 = rabbit.createGroupParameter("bar", groupParam1);

        // steal move values to other groups
        rabbit.addParameter(theValueFloat1, groupParam2);
        rabbit.addParameter(theValueFloat2, groupParam3);
    }

    public void updateVar1() {

    }

    boolean init;

    public void updateVar2() {

        if (!init) {

            //            System.out.println("adding group");
            //            theValueInt = rabbit.createInt32Parameter("INt LABEL");
            //            theValueInt.setValue(333);
            //            //
            //            //            groupParam2 = rabbit.createGroupParameter("foo");
            //            //
            //            //            rabbit.addParameter(theValueInt, groupParam2);
            //            //
            //            rabbit.update();

            init = true;
        } else {
            for (final INumberParameter<Float> p : floatParameter) {
                p.setValue((float)Math.random());
            }
            rabbit.update();
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
