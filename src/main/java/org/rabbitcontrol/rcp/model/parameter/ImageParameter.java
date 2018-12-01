package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.types.ImageDefinition;
import org.rabbitcontrol.rcp.model.types.ImageDefinition.ImageType;

import java.awt.image.RenderedImage;

public class ImageParameter extends ValueParameter<RenderedImage> {

    private ImageDefinition imageDefinition;
    //------------------------------------------------------------
    //------------------------------------------------------------
    public ImageParameter(final short _id) {

        super(_id, new ImageDefinition());
        imageDefinition = (ImageDefinition) getTypeDefinition();
    }

    @Override
    public void setStringValue(final String _value) {
    }

    public void setImageType(ImageType _type) {
        imageDefinition.setImageType(_type);
    }

    public ImageType getImageType() {
        return imageDefinition.getImageType();
    }
}
