package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.EnumDefinition;

public class EnumParameter extends ValueParameter<String> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public EnumParameter(final short _id) {

        super(_id, new EnumDefinition());
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    public EnumDefinition getEnumTypeDefinition() {

        return (EnumDefinition)super.getTypeDefinition();
    }

    @Override
    public void setValue(final String _value) {

        if (!getEnumTypeDefinition().containsValue(_value)) {
            System.err.println("\"" + _value + "\" is not a valid option");
            return;
        }

        super.setValue(_value);
    }

    @Override
    public void setStringValue(final String _value) {
        setValue(_value);
    }
}
