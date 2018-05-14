package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;

public class Int64Parameter extends NumberParameter<Long> {

    public Int64Parameter(final short _id) {

        super(_id, Datatype.INT64);
    }

    @Override
    public void setStringValue(final String _value) {
        setValue(Long.parseLong(_value));
    }
}
