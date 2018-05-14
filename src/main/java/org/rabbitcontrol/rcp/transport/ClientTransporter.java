package org.rabbitcontrol.rcp.transport;

public interface ClientTransporter {

    void connect(String host, int port);

    void disconnect();

    boolean isConnected();

    void send(byte[] _data);

    void addListener(ClientTransporterListener _listener);

    void removeListener(ClientTransporterListener _listener);
}
