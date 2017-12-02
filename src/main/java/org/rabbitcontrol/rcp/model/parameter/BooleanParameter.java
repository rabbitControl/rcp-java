package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.BooleanDefinition;

public class BooleanParameter extends ValueParameter<Boolean> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public BooleanParameter(final int _id) {

        super(_id, new BooleanDefinition());
    }

}
