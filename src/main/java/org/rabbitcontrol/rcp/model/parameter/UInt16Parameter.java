package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.types.UInt16Definition;

public class UInt16Parameter extends NumberParameter<Short> {

    public UInt16Parameter(final short _id) {

        super(_id, Datatype.UINT16);
    }

    public int getValueUnsigned() {

        return UInt16Definition.getUnsigned(getValue());
    }
}
