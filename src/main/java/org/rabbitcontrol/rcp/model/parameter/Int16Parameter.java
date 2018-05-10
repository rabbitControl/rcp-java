package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;

public class Int16Parameter extends NumberParameter<Short> {

    public Int16Parameter(final short _id) {

        super(_id, Datatype.INT16);
    }

}
