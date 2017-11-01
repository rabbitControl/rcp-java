package org.rabbitcontrol.rcp.test.udp;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.transport.RCPTransporter;
import org.rabbitcontrol.rcp.transport.RCPTransporterListener;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * Created by inx on 30/11/16.
 */
public class UDPServerTransporter extends Thread implements RCPTransporter {

    private class UDPClient {

        public InetAddress address;

        public int port;

        private UDPClient(final InetAddress _address, final int _port) {

            address = _address;
            port = _port;
        }
    }

    //------------------------------------------------------------
    //
    private int port;

    private final DatagramSocket socket;

    private volatile boolean doit = true;

    private RCPTransporterListener listener;

    private final Collection<InetAddress> clients = new ArrayList<InetAddress>();

    private int targetPort = 8182;

    //------------------------------------------------------------
    //
    public UDPServerTransporter(final int _port) throws SocketException {

        port = _port;

        if (port < 0) {
            throw new RuntimeException("no port < 0");
        }

        socket = new DatagramSocket(port);

        start();
    }

    @Override
    public void run() {

        final byte[] receiveData = new byte[1024];

        while (doit) {
            final DatagramPacket receivePacket = new DatagramPacket(receiveData,
                                                                    receiveData.length);

            try {
                socket.receive(receivePacket);

                if (!clients.contains(receivePacket.getAddress())) {
                    // FIXME: bad idea to store those "clients" - we never know when they go away
                    // addParameter client anyway - ignore advice and go on
                    clients.add(receivePacket.getAddress());
                }

                byte[] data = Arrays.copyOf(receivePacket.getData(), receivePacket.getLength());

                // parse that
                try {
                    final Packet toiPacket = Packet.parse(new KaitaiStream(data));

                    received(toiPacket, this);
                }
                catch (RCPUnsupportedFeatureException _e) {
                    _e.printStackTrace();
                }
                catch (RCPDataErrorException _e) {
                    _e.printStackTrace();
                }
            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }
        }

        socket.close();
        System.out.println("finishing Client Transporter");
    }


    @Override
    public void send(final Packet _packet) {

        try {
            final byte[] data = Packet.serialize(_packet);

            for (final InetAddress _inetAddress : clients) {
//                System.out.println("ip: " + _inetAddress.getHostAddress() + ":" + targetPort + " :: " +
//                                   "" + new String
//                                           (data));

                final DatagramPacket sendPacket = new DatagramPacket(data,
                                                                     data.length,
                                                                     _inetAddress,
                                                                     targetPort);

                try {
                    socket.send(sendPacket);
                }
                catch (final IOException _e) {
                    _e.printStackTrace();
                }
            }
        }
        catch (final IOException _e) {
            _e.printStackTrace();
        }

    }

    @Override
    public void setListener(final RCPTransporterListener _listener) {

        listener = _listener;
    }

    @Override
    public void received(final Packet _packet, final RCPTransporter _transporter) {
        if (listener != null) {
            listener.received(_packet, this);
        }
    }

    public void setTargetPort(final int _targetPort) {

        targetPort = _targetPort;
    }

}
