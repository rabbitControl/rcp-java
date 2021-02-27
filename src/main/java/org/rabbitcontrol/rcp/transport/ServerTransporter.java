package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.exceptions.RCPException;

public interface ServerTransporter {

    void bind(int port) throws RCPException;
    void unbind();

    void sendToOne(byte[] _data, Object _id);
    void sendToAll(byte[] _data, Object _excludeId);

    int getConnectionCount();

    void addListener(ServerTransporterListener _listener);
    void removeListener(ServerTransporterListener _listener);

    void dispose();
}
