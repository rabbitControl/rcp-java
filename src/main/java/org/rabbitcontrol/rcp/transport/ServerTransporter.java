package org.rabbitcontrol.rcp.transport;

public interface ServerTransporter {

    void bind(int port);
    void unbind();

    void sendToOne(byte[] _data, Object _id);
    void sendToAll(byte[] _data, Object _excludeId);

    int getConnectionCount();

    void addListener(ServerTransporterListener _listener);
    void removeListener(ServerTransporterListener _listener);

}
