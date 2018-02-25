package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.RGBADefinition;

import java.awt.*;
import java.nio.ByteBuffer;

public class RGBAParameter extends ValueParameter<Color> {

    public RGBAParameter(final ByteBuffer _id) {

        super(_id, new RGBADefinition());
    }
}
