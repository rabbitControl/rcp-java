package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.Packet;

/**
 * Created by inx on 30/11/16.
 */
public interface RCPTransporter extends RCPTransporterListener {

    void send(Packet _packet);

    void setListener(final RCPTransporterListener _listener);
}
