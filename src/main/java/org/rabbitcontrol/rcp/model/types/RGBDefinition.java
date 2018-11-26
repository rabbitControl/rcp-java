package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.ColorOptions;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class RGBDefinition extends DefaultDefinition<Color> {



    public RGBDefinition() {
        super(Datatype.RGB);
    }



    @Override
    public void writeValue(final Color _value, final OutputStream _outputStream) throws
                                                                                 IOException {

        final Color value_to_write;
        if (_value != null) {
            value_to_write = _value;
        } else {
            value_to_write = defaultValue;
        }

        if (value_to_write != null) {

            // write rgb to stream
            int c = (value_to_write.getBlue() << 16) | (value_to_write.getGreen() << 8) | value_to_write.getRed();

            _outputStream.write(ByteBuffer.allocate(4).putInt(c).array());
        } else {
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());
        }
    }

    @Override
    public Color readValue(final KaitaiStream _io) {


        // read 4 bytes from stream
        int color = _io.readS4be();

        final Color c = new Color(color & 0xff, (color >> 8) & 0xff, (color >> 16) & 0xff);

        return c;
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final ColorOptions option = ColorOptions.byId(_propertyId);

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
    public void writeOptions(final OutputStream _outputStream, final boolean _all) throws
                                                                                 IOException {

        if (getDefault() != null) {

            if (_all || defaultValueChanged || initialWrite) {

                // use any of the default values id
                _outputStream.write((int)ColorOptions.DEFAULT.id());
                writeValue(getDefault(), _outputStream);

                if (!_all) {
                    defaultValueChanged = false;
                }
            }
        } else if (defaultValueChanged) {

            _outputStream.write((int)ColorOptions.DEFAULT.id());
            writeValue(null, _outputStream);

            defaultValueChanged = false;
        }

    }
}
