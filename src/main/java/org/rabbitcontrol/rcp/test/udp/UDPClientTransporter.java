package org.rabbitcontrol.rcp.test.udp;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.transport.RCPTransporter;
import org.rabbitcontrol.rcp.transport.RCPTransporterListener;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * Created by inx on 30/11/16.
 */
public class UDPClientTransporter extends Thread implements RCPTransporter {

    //------------------------------------------------------------
    //
    private InetAddress address;

    private int port;

    private final DatagramSocket clientSocket;

    private volatile boolean doit = true;

    private RCPTransporterListener listener;

    //------------------------------------------------------------
    //
    public UDPClientTransporter(final String _address, final int _port) throws
                                                                        SocketException,
                                                                        UnknownHostException {

        clientSocket = new DatagramSocket();

        address = InetAddress.getByName(_address);
        port = _port;

        if (port < 0) {
            throw new RuntimeException("no port < 0");
        }

        start();
    }

    @Override
    public void run() {

        final byte[] receiveData = new byte[1024];

        while (doit) {

            final DatagramPacket receivePacket = new DatagramPacket(receiveData,
                                                                    receiveData.length);

            try {
                clientSocket.receive(receivePacket);

                final byte[] data = Arrays.copyOf(receivePacket.getData(), receivePacket.getLength());

                // parse that
                try {
                    final RCPPacket toiPacket = RCPPacket.parse(new KaitaiStream(data));

                    received(toiPacket);
                }
                catch (RCPUnsupportedFeatureException | RCPDataErrorException _e) {
                    _e.printStackTrace();
                }

            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }
        }

        clientSocket.close();
        System.out.println("finishing Client Transporter");
    }


    @Override
    public void send(final RCPPacket _packet) {

        try {
            final byte[] data = RCPPacket.serialize(_packet);

            final DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, port);

            clientSocket.send(sendPacket);
        }
        catch (final IOException _e) {
            _e.printStackTrace();
        }

    }

    @Override
    public void setListener(final RCPTransporterListener _listener) {

        listener = _listener;
    }

//    @Override
//    public void received(final byte[] _data) {
//
//        if (listener != null) {
//            listener.received(_data);
//        }
//    }

    @Override
    public void received(final RCPPacket _packet) {
        if (listener != null) {
            listener.received(_packet);
        }
    }
}
