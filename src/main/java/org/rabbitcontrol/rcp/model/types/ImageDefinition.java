package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.BooleanOptions;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.ByteBuffer;

public class ImageDefinition extends DefaultDefinition<RenderedImage> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public enum ImageType {
        JPEG, PNG, GIF, BMP
    }

    ImageType imageType = ImageType.JPEG;


    public ImageDefinition() {

        super(Datatype.IMAGE);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final BooleanOptions option = BooleanOptions.byId(_propertyId);

        if (option == null) {
            return false;
        }

        switch (option) {
            case DEFAULT:
                setDefault(readValue(_io));
                return true;
        }

        return false;
    }

    @Override
    public RenderedImage readValue(final KaitaiStream _io) {

        final int    size = _io.readS4be();
        final byte[] data = _io.readBytes(size);

        try {
            final BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            return image;
        }
        catch (final IOException _e) {
            _e.printStackTrace();
        }

        return null;
    }

    private void writeImageData(final RenderedImage _value, final OutputStream _outputStream) throws
                                                                                               IOException {

        final byte[] imageInByte;

        // convert BufferedImage to byte array
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(_value, imageType.toString(), baos);

        baos.flush();
        imageInByte = baos.toByteArray();
        baos.close();


        _outputStream.write(ByteBuffer.allocate(4).putInt(imageInByte.length).array());
        _outputStream.write(imageInByte);
    }

    @Override
    public void writeValue(final RenderedImage _value, final OutputStream _outputStream) throws
                                                                                   IOException {

        if (_value != null) {
            writeImageData(_value, _outputStream);
        }
        else if (defaultValue != null) {
            writeImageData(defaultValue, _outputStream);
        }
        else {
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());
        }

    }

    @Override
    public void writeOptions(final OutputStream _outputStream, final boolean _all) throws
                                                                                   IOException {

        if (getDefault() != null) {

            if (_all || defaultValueChanged || initialWrite) {

                // use any of the default values id
                _outputStream.write((int)BooleanOptions.DEFAULT.id());
                writeValue(getDefault(), _outputStream);

                if (!_all) {
                    defaultValueChanged = false;
                }
            }
        }
        else if (defaultValueChanged) {

            _outputStream.write((int)BooleanOptions.DEFAULT.id());
            writeValue(null, _outputStream);

            defaultValueChanged = false;
        }

    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType _type) {
        imageType = _type;
    }
}
