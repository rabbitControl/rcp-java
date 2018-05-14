package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;

public class Int8Parameter extends NumberParameter<Byte> {

    public Int8Parameter(final short _id) {

        super(_id, Datatype.INT8);
    }

    @Override
    public void setStringValue(final String _value) {
        setValue(Byte.parseByte(_value));
    }
}
