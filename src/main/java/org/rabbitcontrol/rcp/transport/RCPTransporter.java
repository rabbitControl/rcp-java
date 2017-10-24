package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.RCPPacket;

/**
 * Created by inx on 30/11/16.
 */
public interface RCPTransporter extends RCPTransporterListener {

    void send(RCPPacket _packet);

    void setListener(final RCPTransporterListener _listener);
}
