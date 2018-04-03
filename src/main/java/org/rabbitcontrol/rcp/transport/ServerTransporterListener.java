package org.rabbitcontrol.rcp.transport;

public interface ServerTransporterListener {

    void received(byte[] _data, ServerTransporter _transporter, Object _id);
}
