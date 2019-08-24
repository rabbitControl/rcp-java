package org.rabbitcontrol.rcp.transport;

import java.io.IOException;

public interface RCPTransporter extends RCPTransporterListener {

    void send(byte[] _data) throws IOException;

    //void send(ByteBuffer _data);

    void setListener(final RCPTransporterListener _listener);
}
