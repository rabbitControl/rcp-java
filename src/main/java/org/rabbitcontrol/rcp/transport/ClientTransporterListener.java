package org.rabbitcontrol.rcp.transport;

public interface ClientTransporterListener {

    void connected();
    void disconnected();

    void received(byte[] _data);
}
