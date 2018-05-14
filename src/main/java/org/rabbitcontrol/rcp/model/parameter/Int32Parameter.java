package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;

public class Int32Parameter extends NumberParameter<Integer> {

    public Int32Parameter(final short _id) {

        super(_id, Datatype.INT32);
    }

    @Override
    public void setStringValue(final String _value) {
        setValue(Integer.parseInt(_value));
    }
}
