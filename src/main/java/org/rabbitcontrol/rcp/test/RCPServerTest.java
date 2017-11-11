package org.rabbitcontrol.rcp.test;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.rabbitcontrol.rcp.model.ParameterFactory;
import org.rabbitcontrol.rcp.model.RCPCommands;
import org.rabbitcontrol.rcp.model.interfaces.INumberParameter;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.model.types.*;
import org.rabbitcontrol.rcp.test.websocket.server.WebsocketServerTransporterNetty;
import org.rabbitcontrol.rcp.transport.RabbitServer;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;

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
    private final RabbitServer rabbit;

    private final StringParameter theValueString;

    private INumberParameter<Double> theValueDouble;

    private final INumberParameter<Integer> theValueInt;

    private final BooleanParameter theValueBool;

    private final INumberParameter<Long> theValueLong;

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

        // create rabbit
        rabbit = new RabbitServer(transporter);

        rabbit.setUpdateListener(this);
        rabbit.setInitListener(this);

        // create values
        theValueString = ParameterFactory.createStringParameter(1);
        theValueString.setValue("This is a text encoded in utf-8. let's test it: ¬”#£œæýýý‘");
        theValueString.setDescription("test description 2");
        theValueString.setLabel("text label");
        theValueString.setUserdata("some user data?".getBytes());

        theValueString.getTypeDefinition().setDefault("defaultstring");

//        theValueString.addLabelChangeListener(new RCPParameter.LABEL_CHANGED() {
//
//            @Override
//            public void labelChanged(final String newValue, final String oldValue) {
//
//                System.out.println("label changed!! : " + oldValue + " -> " + newValue);
//            }
//        });
//
//        theValueString.addValueChangeListener(new RCPParameter.VALUE_CHANGED<String>() {
//
//            @Override
//            public void valueChanged(final String newValue, final String oldValue) {
//
//                System.out.println("label changed!! : " + oldValue + " -> " + newValue);
//            }
//        });
//
//
//        theValueString.setLabel("new label");

        DefaultDefinition<Boolean> bl = new BooleanDefinition();
        DefaultDefinition<List<Boolean>> d          = new ArrayDefinition<Boolean>(bl, 4);
        ArrayDefinition<Boolean>         def        = new ArrayDefinition((DefaultDefinition<List<Boolean>>)d, 4);
        final ArrayParameter<Boolean>    arrayParam = ParameterFactory.createArrayParameter(12, def);





        theValueDouble = ParameterFactory.createNumberParameter(2, Double.class);

        theValueDouble.getTypeDefinition().setMaximum(1.D);


        theValueDouble.setLabel("a double");
        theValueDouble.setDescription("double description");
        theValueDouble.setValue(3.14);

//        theValueDouble.addValueChangeListener(new RCPParameter.VALUE_CHANGED<Double>() {
//
//            @Override
//            public void valueChanged(final Double newValue, final Double oldValue) {
//                System.out.println("double value changed!! : " + oldValue + " -> " + newValue);
//            }
//        });


        theValueInt = ParameterFactory.createNumberParameter(3, Integer.class);
        theValueInt.setLabel("INT LABEL");
        theValueInt.setValue(333);

        theValueBool = ParameterFactory.createBooleanParameter(4);
        theValueBool.setLabel("toggle button");
        theValueBool.setValue(true);

        theValueLong = ParameterFactory.createNumberParameter(5, Long.class);
        theValueLong.setValue((long)10);
        theValueLong.setLabel("a long number");

        // addParameter the values to rabbit
        rabbit.addParameter(theValueDouble);
        rabbit.addParameter(theValueString);
        rabbit.addParameter(theValueInt);
        rabbit.addParameter(theValueBool);
    }


    public void updateVar1() {

        //rcp.beginParamterUpdate();
        //rcp.endParamterUpdate();
        //theValueLong.setValueMore().setLabelMore().setOrder();

//        RCPParameterNumber<Long> newVal = theValueLong.cloneEmpty();
//        newVal.setValue(theValueLong.getValue() + 1);
//
//        rabbit.update(newVal);
    }

    public void updateVar2() {
        theValueString.setValue("content: " + counter++);
        theValueDouble.setValue((double)counter);

        rabbit.updateParameters(theValueString, theValueDouble);
    }

    //------------------------------------------------------------
    //
    @Override
    public void updated(final IParameter _value) {

        // updated from client
//        System.out.println("server: updated: " + _value.getId() + " : " + _value.getValue());
//
//        rabbit.dumpCache();
        System.out.println("server update:");
        _value.dump();
    }

    @Override
    public void init() {

    }
}
