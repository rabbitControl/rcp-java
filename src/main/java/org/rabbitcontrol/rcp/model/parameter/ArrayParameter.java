package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.ArrayDefinition;

import java.nio.ByteBuffer;
import java.util.List;

public class ArrayParameter<T> extends ValueParameter<List<T>> {

    public ArrayDefinition<T> arrayDefinition;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public ArrayParameter(final ByteBuffer _id, final ArrayDefinition<T> _definition) {

        super(_id, _definition);

        arrayDefinition = _definition;

    }
}
