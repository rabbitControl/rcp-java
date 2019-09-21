package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.types.Vector4;

public class Vector4Int32Parameter extends NumberParameter<Vector4<Integer>>{

    public Vector4Int32Parameter(final short _id) {

        super(_id, Datatype.VECTOR4I32);
    }

    @Override
    public void setStringValue(final String _value) {

        final String[] values = _value.split(",");
        if (values.length < 4) {
            return;
        }

        try {

            final Integer x = Integer.parseInt(values[0]);
            final Integer y = Integer.parseInt(values[1]);
            final Integer z = Integer.parseInt(values[2]);
            final Integer t = Integer.parseInt(values[3]);

            setValue(new Vector4<Integer>(x, y, z, t));
        }
        catch (final NumberFormatException _e) {
            System.err.println("could not parse string to vector");
        }
    }
}
