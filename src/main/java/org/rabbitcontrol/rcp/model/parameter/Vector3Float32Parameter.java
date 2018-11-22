package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.types.Vector3;

public class Vector3Float32Parameter extends NumberParameter<Vector3<Float>>{

    public Vector3Float32Parameter(final short _id) {

        super(_id, Datatype.VECTOR3F32);
    }

    @Override
    public void setStringValue(final String _value) {

    }
}
