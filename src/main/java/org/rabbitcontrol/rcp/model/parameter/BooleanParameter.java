package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.BooleanDefinition;

public class BooleanParameter extends ValueParameter<Boolean> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public BooleanParameter(final short _id) {

        super(_id, new BooleanDefinition());
    }

    @Override
    public void setStringValue(final String _value) {
        setValue(Boolean.parseBoolean(_value));
    }
}
