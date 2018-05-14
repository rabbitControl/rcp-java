package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.types.UInt8Definition;

public class UInt8Parameter extends NumberParameter<Byte> {

    public UInt8Parameter(final short _id) {

        super(_id, Datatype.UINT8);
    }

    public int getValueUnsigned() {

        return UInt8Definition.getUnsigned(getValue());
    }

    @Override
    public void setStringValue(final String _value) {
        setValue(Byte.parseByte(_value));
    }
}
