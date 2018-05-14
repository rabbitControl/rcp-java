package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.Parameter.PARAMETER_VALUE_UPDATED;
import org.rabbitcontrol.rcp.model.RCPCommands.*;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.NumberParameter;
import org.rabbitcontrol.rcp.model.parameter.StringParameter;
import org.rabbitcontrol.rcp.test.websocket.client.WebsocketClientTransporter;
import org.rabbitcontrol.rcp.transport.RCPClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Created by inx on 29/11/16.
 */
public class RCPClientTest extends JFrame implements Add, Remove, Update {

    //------------------------------------------------------------
    //
    public static void main(final String[] args) {

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

        System.out.println("finish client");
    }

    //------------------------------------------------------------
    //
    private final RCPClient rcp;

    ArrayList<IParameter> allParams = new ArrayList<IParameter>();

    final GridLayout layout = new GridLayout();

    Map<Short, Component> componentMap = new HashMap<Short, Component>();

    int rowcount;

    //------------------------------------------------------------
    //
    public RCPClientTest() {

        setupFrame();

        // create serializer and transporter
//        final UDPClientTransporter transporter = new UDPClientTransporter("localhost", 8888);
        //        final TCPClientTransporter transporter = new TCPClientTransporter("localhost",
        // 8888);
        final WebsocketClientTransporter transporter = new WebsocketClientTransporter();


        // create toi
        rcp = new RCPClient(transporter);
        rcp.setUpdateListener(this);
        rcp.setAddListener(this);
        rcp.setRemoveListener(this);

        transporter.connect("localhost", 10000);

        // init
        rcp.init();
    }

    private void setupFrame() {

        setLayout(layout);

        pack();
        setSize(800, 48);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void addComponent(final Component _component) {

        add(_component);
        rowcount++;
        layout.setRows(rowcount);

        setSize(getWidth(), 48+(48*rowcount));

        getContentPane().repaint();
    }

    private void removeComponent(final Component _component) {
        remove(_component);
        if (--rowcount < 0) {
            rowcount = 0;
        }
        layout.setRows(rowcount);
    }


    int count;

    public void updateValue() {

        final Map<Short, IParameter> cache = rcp.getValueCache();

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
    public void parameterAdded(final IParameter _parameter) {

        //create ui
        //create adapter(parameter)
        // ui.setupdatelistern(adapter, client)

        System.out.println("adding parameter: " + _parameter.getLabel());

        if (!allParams.contains(_parameter)) {
            allParams.add(_parameter);
            _parameter.dump();

            final JPanel p = new JPanel(new BorderLayout());
            final JTextField field = new JTextField(_parameter.getStringValue());

            field.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    _parameter.setStringValue(field.getText());
                    rcp.update();
                }
            });


            p.add(new JLabel(_parameter.getLabel()), BorderLayout.WEST);
            p.add(field, BorderLayout.CENTER);

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    addComponent(p);
                }
            });

            componentMap.put(_parameter.getId(), p);

            _parameter.addValueUpdateListener(new PARAMETER_VALUE_UPDATED() {

                @Override
                public void valueUpdated(final IParameter _parameter) {
                    field.setText(_parameter.getStringValue());
                }
            });

        }
    }

    @Override
    public void parameterUpdated(final IParameter _value) {

        System.out.println("client: updated: " + _value.getId());
        //        toi.dumpCache();
        _value.dump();
    }

    @Override
    public void parameterRemoved(final IParameter _value) {

        System.out.println("client: removed: " + _value.getId());
        //        toi.dumpCache();
        _value.dump();
    }
}
