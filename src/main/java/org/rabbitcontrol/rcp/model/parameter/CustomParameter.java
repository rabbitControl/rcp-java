package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.CustomDefinition;

import static org.rabbitcontrol.rcp.RCP.bytesToHex;

public class CustomParameter extends ValueParameter<byte[]> {

    public CustomParameter(final short _id, final long size) {

        super(_id, new CustomDefinition(size));
    }

    @Override
    public void setStringValue(final String _value) {
        // nop
    }

    @Override
    public String getStringValue() {
        return bytesToHex(getValue());
    }

    public CustomDefinition getCustomDefinition() {
        return (CustomDefinition) getTypeDefinition();
    }
}
