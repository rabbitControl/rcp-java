package org.rabbitcontrol.rcp.transport.udp;

import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.transport.ServerTransporter;
import org.rabbitcontrol.rcp.transport.ServerTransporterListener;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * Created by inx on 30/11/16.
 */
public class UDPServerTransporter extends Thread implements ServerTransporter {

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

    private DatagramSocket socket;

    private volatile boolean doit = true;

    private ServerTransporterListener listener;

    private final Collection<InetAddress> clients = new ArrayList<InetAddress>();

    private int targetPort = 8182;

    //------------------------------------------------------------
    //

    @Override
    public void run() {

        final byte[] receiveData = new byte[1024];

        while (doit && !interrupted())
        {
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
                if ((data != null) && (listener != null))
                {
                    listener.received(data, this, receivePacket.getAddress());
                }
            }
            catch (final IOException _e) {
                // nop
            }
            catch (RCPDataErrorException _e) {
                // nop
            }
            catch (RCPException _e) {
                // nop
            }
        }

        socket.close();
    }


    public void send(final byte[] _data) throws IOException {

        for (final InetAddress _inetAddress : clients)
        {
            final DatagramPacket sendPacket = new DatagramPacket(_data,
                                                                 _data.length,
                                                                 _inetAddress,
                                                                 targetPort);

            socket.send(sendPacket);
        }
    }

    public void setTargetPort(final int _targetPort) {

        targetPort = _targetPort;
    }


    @Override
    public void bind(final int port) throws RCPException
    {
        this.port = port;

        if (this.port < 0) {
            throw new RuntimeException("no port < 0");
        }

        try {
            socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(port));
            start();
        }
        catch (SocketException _e) {
            throw new RCPException(_e);
        }

    }

    @Override
    public void unbind() {

        if (socket != null)
        {
            doit = false;
            interrupt();

            // should we wait?
        }

    }

    @Override
    public void sendToOne(final byte[] _data, final Object _id) {

    }

    @Override
    public void sendToAll(final byte[] _data, final Object _excludeId) {

    }

    @Override
    public int getConnectionCount() {

        return 0;
    }

    @Override
    public void addListener(final ServerTransporterListener _listener) {
        listener = _listener;
    }

    @Override
    public void removeListener(final ServerTransporterListener _listener) {
        if ((listener != null) && listener.equals(_listener)) {
            listener = null;
        }
    }

}
