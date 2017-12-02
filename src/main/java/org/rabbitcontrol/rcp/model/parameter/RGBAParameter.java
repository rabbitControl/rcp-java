package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.RGBADefinition;

import java.awt.*;

public class RGBAParameter extends ValueParameter<Color> {

    public RGBAParameter(final int _id) {

        super(_id, new RGBADefinition());
    }
}
