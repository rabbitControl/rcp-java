package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.RCPCommands.Init;
import org.rabbitcontrol.rcp.model.RCPCommands.Update;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.Widget;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.interfaces.INumberParameter;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.model.types.*;
import org.rabbitcontrol.rcp.model.types.ImageDefinition.ImageType;
import org.rabbitcontrol.rcp.model.widgets.*;
import org.rabbitcontrol.rcp.test.websocket.server.WebsocketServerTransporterNetty;
import org.rabbitcontrol.rcp.transport.RCPServer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * start with
 * ./gradlew run -Dexec.args="-c exposeSingleFLoat"
 */
public class RCPServerTest implements Update, Init {

    public static boolean doAutoUpdate = false;

    static Color[] colors = { Color.CYAN,
                              Color.BLUE,
                              Color.GREEN,
                              Color.MAGENTA,
                              Color.pink,
                              Color.RED };

    //------------------------------------------------------------
    //
    public static void main(final String[] args) {

        String config_string = "exposeParameterInGroups";
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

            if (doAutoUpdate) {

                final Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // some automatic value update...
                        while (!Thread.interrupted()) {

                            try {
                                Thread.sleep(1000);
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

    private ArrayList<INumberParameter<Float>> floatParameter = new ArrayList<INumberParameter<Float>>();
    private ArrayList<BooleanParameter> boolParameter = new ArrayList<BooleanParameter>();

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



    private void exposeFaultyParameter() throws RCPParameterException {

        Float32Parameter imageParameter = rabbit.createFloatParameter("test");
        imageParameter.setMinimum(0.F);
        imageParameter.setMaximum(0.F);
    }


    private void exposeImageParameter() throws RCPParameterException, IOException {

        ImageParameter imageParameter = rabbit.createImageParameter("test");
        imageParameter.setReadonly(true);
        imageParameter.setImageType(ImageType.BMP);

        BufferedImage image = ImageIO.read(new File("image.jpg"));
        imageParameter.setValue(image);
    }


    private void exposeBooleanReadonlyToggle() throws RCPParameterException {

        BooleanParameter bp = rabbit.createBooleanParameter("test");
        bp.setReadonly(true);

        boolParameter.add(bp);
    }

    private void exposeConUIController() throws RCPParameterException {

        StringParameter p = rabbit.createStringParameter("server");
        p.setUserid("/server");
        p.setValue("testserver");

    }


    private void exposeConUI() throws RCPParameterException {


        // admin
        INumberParameter<Integer> adminnum = rabbit.createInt32Parameter("Speed");
        adminnum.setUserid("/cpu/speed");
        adminnum.getTypeDefinition().setUnit("Mhz");
        adminnum.setValue(13);

        adminnum = rabbit.createInt32Parameter("Temp");
        adminnum.setUserid("/cpu/temp");
        adminnum.getTypeDefinition().setUnit("C");
        adminnum.setValue(14);

        adminnum = rabbit.createInt32Parameter("Usage");
        adminnum.setUserid("/cpu/usage");
        adminnum.getTypeDefinition().setUnit("%");
        adminnum.setValue(14);

        adminnum = rabbit.createInt32Parameter("Power");
        adminnum.setUserid("/cpu/watt");
        adminnum.getTypeDefinition().setUnit("W");
        adminnum.setValue(15);

        adminnum = rabbit.createInt32Parameter("RAM");
        adminnum.setUserid("/cpu/ram");
        adminnum.getTypeDefinition().setUnit("%");
        adminnum.setValue(16);

        final INumberParameter<Float> audiolevel = rabbit.createFloatParameter("Global audo");
        audiolevel.setUserid("/level");
        audiolevel.getTypeDefinition().setMinimum(-15F);
        audiolevel.getTypeDefinition().setMaximum(15F);
        audiolevel.setValue(1F);

        BangParameter adminBang = rabbit.createBangParameter("RESET ALL");
        adminBang.setUserid("/restart");
        adminBang.setFunction(new Runnable() {
            @Override
            public void run() {
                System.out.println("RESET ALL");
            }
        });
        Widget widget = new DefaultWidget();
        widget.setNeedsConfirmation(true);
        adminBang.setWidget(widget);


        adminBang = rabbit.createBangParameter("REBOOT ALL");
        adminBang.setUserid("/reboot");
        adminBang.setFunction(new Runnable() {
            @Override
            public void run() {
                System.out.println("REBOOT ALL");
            }
        });
        adminBang.setWidget(widget);


        final BooleanParameter admindebug = rabbit.createBooleanParameter("Debug view");
        admindebug.setUserid("/debug");
        widget = new DefaultWidget();
        widget.setNeedsConfirmation(true);
        admindebug.setWidget(widget);

        _exposeMarshmallowGroup("A");
        _exposeMarshmallowGroup("B");
    }
    
    private void _exposeMarshmallowGroup(final String groupname) throws 
                                                                       RCPParameterException {

        final GroupParameter group = rabbit.createGroupParameter(groupname);


        // timeline value
        final INumberParameter<Float> timeline = rabbit.createFloatParameter("Timeline", group);
        timeline.setUserid("/" + groupname + "/time");
        timeline.getTypeDefinition().setMinimum(0.F);
        timeline.getTypeDefinition().setMaximum(22.123F);
        timeline.setValue(5F);

        // time sections
        StringParameter timestring = rabbit.createStringParameter("Intro", group);
        timestring.setUserid("/" + groupname + "/time/intro");
        timestring.setValue("00:00");

        timestring = rabbit.createStringParameter("Experience", group);
        timestring.setUserid("/" + groupname + "/time/experience");
        timestring.setValue("00:00");

        timestring = rabbit.createStringParameter("Outro", group);
        timestring.setUserid("/" + groupname + "/time/outro");
        timestring.setValue("00:00");


        // on/off toggle
        BooleanParameter button = rabbit.createBooleanParameter("Play", group);
        button.setUserid("/" + groupname + "/on");

        // next button
        final BangParameter next = rabbit.createBangParameter("Next", group);
        next.setUserid("/" + groupname + "/next");
        next.setFunction(new Runnable() {

            @Override
            public void run() {
                System.out.println("NEXT");
            }
        });
        Widget widget = new DefaultWidget();
        widget.setNeedsConfirmation(true);
        next.setWidget(widget);

        StringParameter grouplabel = rabbit.createStringParameter("label", group);
        grouplabel.setUserid("/" + groupname + "/label");
        grouplabel.setValue(groupname);


        for (int i = 1; i < 10; i++) {

            final String pack_prefix = "/" + groupname + "/pack" + i;

            final GroupParameter pack_group = rabbit.createGroupParameter("Pack " + i, group);

            // on
            BooleanParameter bp = rabbit.createBooleanParameter("on", pack_group);
            bp.setUserid(pack_prefix + "/on");
            widget = new DefaultWidget();
            widget.setNeedsConfirmation(true);
            bp.setWidget(widget);

            // name
            final StringParameter packname = rabbit.createStringParameter("name", pack_group);
            packname.setUserid(pack_prefix + "/label");
            packname.setValue("Pack " + i);

            // warning
            bp = rabbit.createBooleanParameter("warning", pack_group);
            bp.setUserid(pack_prefix + "/warning");


            // position
            // vec3 float32 of position
            Vector3Float32Parameter posparam = rabbit.createVector3Float32Parameter("Position",
                                                                                    pack_group);
            posparam.setDescription("Pack "+i);
            posparam.setUserid(pack_prefix + "/pos");
            posparam.setValue(new Vector3<Float>(
                    (float)Math.random()*2-1,
                    (float)Math.random()*2-1,
                    (float)Math.random()*2-1));



            //
            INumberParameter<Float> p = rabbit.createFloatParameter("Ping", pack_group);
            p.setUserid(pack_prefix + "/roundtrip");
            p.getTypeDefinition().setUnit("ms");
            p.setValue(2.5F);

            bp = rabbit.createBooleanParameter("Debug View", pack_group);
            bp.setUserid(pack_prefix + "/debug");
            widget = new DefaultWidget();
            widget.setNeedsConfirmation(true);
            bp.setWidget(widget);

            // system commands
            BangParameter bang = rabbit.createBangParameter("RESET", pack_group);
            bang.setUserid(pack_prefix + "/restart");
            bang.setFunction(new Runnable() {

                @Override
                public void run() {
                    System.out.println("RESET BANG");
                }
            });
            widget = new DefaultWidget();
            widget.setNeedsConfirmation(true);
            bang.setWidget(widget);


            bang = rabbit.createBangParameter("REBOOT", pack_group);
            bang.setUserid(pack_prefix + "/reboot");
            bang.setFunction(new Runnable() {

                @Override
                public void run() {
                    System.out.println("REBOOT BANG");
                }
            });
            widget = new DefaultWidget();
            widget.setNeedsConfirmation(true);
            bang.setWidget(widget);

            bang = rabbit.createBangParameter("VNC", pack_group);
            bang.setUserid(pack_prefix + "/vnc");
            bang.setFunction(new Runnable() {

                @Override
                public void run() {
                    System.out.println("VNC BANG");
                }
            });
            widget = new DefaultWidget();
            widget.setNeedsConfirmation(true);
            bang.setWidget(widget);


            // vive
            button = rabbit.createBooleanParameter("on", pack_group);
            button.setUserid(pack_prefix + "/vive/on");

            button = rabbit.createBooleanParameter("HMD", pack_group);
            button.setUserid(pack_prefix + "/head");

            button = rabbit.createBooleanParameter("LH", pack_group);
            button.setUserid(pack_prefix + "/tracker1");

            button = rabbit.createBooleanParameter("RH", pack_group);
            button.setUserid(pack_prefix + "/tracker2");


            // leap
            button = rabbit.createBooleanParameter("on", pack_group);
            button.setUserid(pack_prefix + "/leap/on");

            button = rabbit.createBooleanParameter("LH", pack_group);
            button.setUserid(pack_prefix + "/leap/left");

            button = rabbit.createBooleanParameter("RH", pack_group);
            button.setUserid(pack_prefix + "/leap/right");

            // body
            button = rabbit.createBooleanParameter("on", pack_group);
            button.setUserid(pack_prefix + "/bio/on");

            p = rabbit.createFloatParameter("Breath", pack_group);
            p.setUserid(pack_prefix + "/bio/breath");
            p.getTypeDefinition().setMinimum(0.F);
            p.getTypeDefinition().setMaximum(1.F);
            p.setValue(0.5F);

            INumberParameter<Integer> num = rabbit.createInt32Parameter("Heart", pack_group);
            num.setUserid(pack_prefix + "/bio/heart");
            num.getTypeDefinition().setUnit("bpm");
            num.getTypeDefinition().setMinimum(20);
            num.getTypeDefinition().setMaximum(200);
            num.setValue(10);


            // CPU
            num = rabbit.createInt32Parameter("Speed", pack_group);
            num.setUserid(pack_prefix + "/cpu/speed");
            num.getTypeDefinition().setUnit("Mhz");
            num.setValue(10);

            num = rabbit.createInt32Parameter("Temp", pack_group);
            num.setUserid(pack_prefix + "/cpu/temp");
            num.getTypeDefinition().setUnit("C");
            num.setValue(10);

            num = rabbit.createInt32Parameter("Usage", pack_group);
            num.setUserid(pack_prefix + "/cpu/usage");
            num.getTypeDefinition().setUnit("%");
            num.setValue(10);

            num = rabbit.createInt32Parameter("Power", pack_group);
            num.setUserid(pack_prefix + "/cpu/watt");
            num.getTypeDefinition().setUnit("W");
            num.setValue(10);

            num = rabbit.createInt32Parameter("RAM", pack_group);
            num.setUserid(pack_prefix + "/cpu/ram");
            num.getTypeDefinition().setUnit("%");
            num.setValue(10);


            // GPU
            num = rabbit.createInt32Parameter("Speed", pack_group);
            num.setUserid(pack_prefix + "/gpu/speed");
            num.getTypeDefinition().setUnit("Mhz");
            num.setValue(10);

            num = rabbit.createInt32Parameter("Temp", pack_group);
            num.setUserid(pack_prefix + "/gpu/temp");
            num.getTypeDefinition().setUnit("C");
            num.setValue(10);

            num = rabbit.createInt32Parameter("Usage", pack_group);
            num.setUserid(pack_prefix + "/gpu/usage");
            num.getTypeDefinition().setUnit("%");
            num.setValue(10);

            num = rabbit.createInt32Parameter("FPS", pack_group);
            num.setUserid(pack_prefix + "/gpu/fps");
            num.getTypeDefinition().setUnit("fps");
            num.setValue(10);


            // floats


            p = rabbit.createFloatParameter("Ext 1", pack_group);
            p.setUserid(pack_prefix + "/Puk1");
            p.getTypeDefinition().setMinimum(0.F);
            p.getTypeDefinition().setMaximum(1.F);
            p.setValue(0.5F);
            floatParameter.add(p);

            p = rabbit.createFloatParameter("Ext 2", pack_group);
            p.setUserid(pack_prefix + "/Puk2");
            p.getTypeDefinition().setMinimum(0.F);
            p.getTypeDefinition().setMaximum(1.F);
            p.setValue(0.5F);
            floatParameter.add(p);

            p = rabbit.createFloatParameter("INT", pack_group);
            p.setUserid(pack_prefix + "/PC");
            p.getTypeDefinition().setMinimum(0.F);
            p.getTypeDefinition().setMaximum(1.F);
            p.setValue(0.5F);
            floatParameter.add(p);

            p = rabbit.createFloatParameter("TRK 1", pack_group);
            p.setUserid(pack_prefix + "/tracker1/battery");
            p.getTypeDefinition().setMinimum(0.F);
            p.getTypeDefinition().setMaximum(1.F);
            p.setValue(0.5F);
            floatParameter.add(p);

            p = rabbit.createFloatParameter("TRK 2", pack_group);
            p.setUserid(pack_prefix + "/tracker2/battery");
            p.getTypeDefinition().setMinimum(0.F);
            p.getTypeDefinition().setMaximum(1.F);
            p.setValue(0.5F);
            floatParameter.add(p);
        }
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

        rabbit.update();
    }


    private void exposeFloatTest() throws RCPParameterException {

        Float32Parameter p = rabbit.createFloatParameter("simple-float");
        p.setValue(3.1415F);

        p = rabbit.createFloatParameter("float min/max");
        p.setMinimum(0.F);
        p.setMaximum(100.F);
        p.setValue(20.F);

        p = rabbit.createFloatParameter("float min/max/multipleof/unit");
        p.setMinimum(0.F);
        p.setMaximum(100.F);
        p.setValue(20.F);
        p.setMultipleof(5.F);
        p.setUnit("MM");

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

        final BangParameter bangParameter = rabbit.createBangParameter("Bang!");
        bangParameter.setFunction(new Runnable() {

            @Override
            public void run() {

                System.out.println("BANG!");
            }
        });

        ImageParameter imageParameter = rabbit.createImageParameter("image");
        imageParameter.setReadonly(true);
        imageParameter.setImageType(ImageType.BMP);

        try {
            BufferedImage image = ImageIO.read(new File("/Users/inx/Pictures/ice.jpg"));
            imageParameter.setValue(image);
        }
        catch (IOException _e) {
            _e.printStackTrace();
        }

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
