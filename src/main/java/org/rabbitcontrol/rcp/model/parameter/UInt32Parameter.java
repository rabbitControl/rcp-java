package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.types.UInt32Definition;

public class UInt32Parameter extends NumberParameter<Integer> {

    public UInt32Parameter(final short _id) {

        super(_id, Datatype.UINT32);
    }

    public long getValueUnsigned() {

        return UInt32Definition.getUnsigned(getValue());
    }
}
