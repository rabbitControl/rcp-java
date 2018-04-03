package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;

public class UInt64Parameter extends NumberParameter<Long> {

    public UInt64Parameter(final short _id) {

        super(_id, Datatype.UINT64);
    }
}
