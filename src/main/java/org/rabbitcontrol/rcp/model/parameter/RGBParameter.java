package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.RGBDefinition;

import java.awt.*;

public class RGBParameter extends ValueParameter<Color> {

    public RGBParameter(final short _id) {

        super(_id, new RGBDefinition());
    }

    @Override
    public void setStringValue(final String _value) {
        setValue(Color.decode(_value));
    }
}
