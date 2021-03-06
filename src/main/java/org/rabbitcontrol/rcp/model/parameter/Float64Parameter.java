package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;

public class Float64Parameter extends NumberParameter<Double> {

    public Float64Parameter(final short _id) {

        super(_id, Datatype.FLOAT64);
    }

    @Override
    public void setStringValue(final String _value) {
        setValue(Double.parseDouble(_value));
    }
}
