package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.EnumDefinition;

public class EnumParameter extends ValueParameter<Integer> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public EnumParameter(final int _id) {

        super(_id, new EnumDefinition());
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    public EnumDefinition getEnumTypeDefinition() {

        return (EnumDefinition)super.getTypeDefinition();
    }

    @Override
    public void setValue(final Integer _value) {
        super.setValue(getEnumTypeDefinition().conformValue(_value));
    }
}
