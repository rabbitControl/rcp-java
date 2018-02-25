package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.RGBDefinition;

import java.awt.*;
import java.nio.ByteBuffer;

public class RGBParameter extends ValueParameter<Color> {

    public RGBParameter(final ByteBuffer _id) {

        super(_id, new RGBDefinition());
    }
}
