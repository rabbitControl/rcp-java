package org.rabbitcontrol.rcp.transport;

import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;

public interface ServerTransporterListener {

    void received(byte[] _data, ServerTransporter _transporter, Object _id) throws
                                                                            RCPException,
                                                                            RCPDataErrorException;
}
