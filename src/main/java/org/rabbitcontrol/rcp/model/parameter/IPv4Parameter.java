package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.IPv4Definition;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class IPv4Parameter extends ValueParameter<Inet4Address> {

    public IPv4Parameter(short _id) {
        super(_id, new IPv4Definition());
    }

    @Override
    public void setStringValue(String _value) {
        try {
            setValue((Inet4Address) Inet4Address.getByName(_value));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
