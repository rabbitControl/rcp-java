package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.types.Vector3;

public class Vector3Float32Parameter extends NumberParameter<Vector3<Float>>{

    public Vector3Float32Parameter(final short _id) {

        super(_id, Datatype.VECTOR3F32);
    }

    @Override
    public void setStringValue(final String _value) {

        final String[] values = _value.split(",");
        if (values.length < 3) {
            return;
        }

        try {

            final Float x = Float.parseFloat(values[0]);
            final Float y = Float.parseFloat(values[1]);
            final Float z = Float.parseFloat(values[2]);

            setValue(new Vector3<Float>(x, y, z));
        }
        catch (final NumberFormatException _e) {
            System.err.println("could not parse string to vector");
        }
    }
}
