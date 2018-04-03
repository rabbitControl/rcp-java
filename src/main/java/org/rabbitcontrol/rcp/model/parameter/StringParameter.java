package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.StringDefinition;

public class StringParameter extends ValueParameter<String> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public StringParameter(final short _id) {

        super(_id, new StringDefinition());
    }
}
