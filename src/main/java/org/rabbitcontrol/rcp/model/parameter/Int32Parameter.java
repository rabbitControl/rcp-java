package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;

public class Int32Parameter extends NumberParameter<Integer> {

    public Int32Parameter(final short _id) {

        super(_id, Datatype.INT32);
    }

}
