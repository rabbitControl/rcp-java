package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.types.Vector4;

public class Vector4Float32Parameter extends NumberParameter<Vector4<Float>>{

    public Vector4Float32Parameter(final short _id) {

        super(_id, Datatype.VECTOR4F32);
    }

    @Override
    public void setStringValue(final String _value) {

        final String[] values = _value.split(",");
        if (values.length < 4) {
            return;
        }

        try {

            final Float x = Float.parseFloat(values[0]);
            final Float y = Float.parseFloat(values[1]);
            final Float z = Float.parseFloat(values[2]);
            final Float t = Float.parseFloat(values[3]);

            setValue(new Vector4<Float>(x, y, z, t));
        }
        catch (final NumberFormatException _e) {
            System.err.println("could not parse string to vector");
        }
    }
}
