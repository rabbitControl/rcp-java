package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.types.Vector3;

public class Vector3Int32Parameter extends NumberParameter<Vector3<Integer>>{

    public Vector3Int32Parameter(final short _id) {

        super(_id, Datatype.VECTOR3I32);
    }

    @Override
    public void setStringValue(final String _value) {

        final String[] values = _value.split(",");
        if (values.length < 3) {
            return;
        }

        try {

            final Integer x = Integer.parseInt(values[0]);
            final Integer y = Integer.parseInt(values[1]);
            final Integer z = Integer.parseInt(values[2]);

            setValue(new Vector3<Integer>(x, y, z));
        }
        catch (final NumberFormatException _e) {
            System.err.println("could not parse string to vector");
        }
    }
}
