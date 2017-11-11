package org.rabbitcontrol.rcp.transport;

public interface RCPTransporter extends RCPTransporterListener {

    void send(byte[] _data);

    //void send(ByteBuffer _data);

    void setListener(final RCPTransporterListener _listener);
}
