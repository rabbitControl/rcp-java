package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.RGBDefinition;

import java.awt.*;

public class RGBParameter extends ValueParameter<Color> {

    public RGBParameter(final int _id) {

        super(_id, new RGBDefinition());
    }
}
