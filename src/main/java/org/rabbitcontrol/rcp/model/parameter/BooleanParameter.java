package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.BooleanDefinition;

import java.nio.ByteBuffer;

public class BooleanParameter extends ValueParameter<Boolean> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public BooleanParameter(final ByteBuffer _id) {

        super(_id, new BooleanDefinition());
    }

}
