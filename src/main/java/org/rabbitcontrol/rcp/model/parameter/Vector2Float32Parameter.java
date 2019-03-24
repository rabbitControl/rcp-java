package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.types.Vector2;

public class Vector2Float32Parameter extends NumberParameter<Vector2<Float>>{

    public Vector2Float32Parameter(final short _id) {

        super(_id, Datatype.VECTOR2F32);
    }

    @Override
    public void setStringValue(final String _value) {

        final String[] values = _value.split(",");
        if (values.length < 2) {
            return;
        }

        try {

            final Float x = Float.parseFloat(values[0]);
            final Float y = Float.parseFloat(values[1]);

            setValue(new Vector2<Float>(x, y));
        }
        catch (final NumberFormatException _e) {
            System.err.println("could not parse string to vector");
        }
    }
}
