package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.Packet;

public interface RCPTransporterListener {

    void received(final Packet _packet, final RCPTransporter _transporter);
}
