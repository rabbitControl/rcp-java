package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.RCPPacket;

public interface RCPTransporterListener {

    void received(final RCPPacket _packet);
}
