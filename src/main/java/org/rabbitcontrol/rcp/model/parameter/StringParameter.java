package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.StringDefinition;

import java.nio.ByteBuffer;

public class StringParameter extends ValueParameter<String> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public StringParameter(final ByteBuffer _id) {

        super(_id, new StringDefinition());
    }
}
