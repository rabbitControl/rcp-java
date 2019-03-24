package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.types.Vector2;

public class Vector2Int32Parameter extends NumberParameter<Vector2<Integer>>{

    public Vector2Int32Parameter(final short _id) {

        super(_id, Datatype.VECTOR2I32);
    }

    @Override
    public void setStringValue(final String _value) {

        final String[] values = _value.split(",");
        if (values.length < 2) {
            return;
        }

        try {

            final Integer x = Integer.parseInt(values[0]);
            final Integer y = Integer.parseInt(values[1]);

            setValue(new Vector2<Integer>(x, y));
        }
        catch (final NumberFormatException _e) {
            System.err.println("could not parse string to vector");
        }
    }
}
