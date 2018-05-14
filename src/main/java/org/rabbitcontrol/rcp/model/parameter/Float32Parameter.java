package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;

public class Float32Parameter extends NumberParameter<Float> {

    public Float32Parameter(final short _id) {

        super(_id, Datatype.FLOAT32);
    }

    @Override
    public void setStringValue(final String _value) {
        setValue(Float.parseFloat(_value));
    }
}
