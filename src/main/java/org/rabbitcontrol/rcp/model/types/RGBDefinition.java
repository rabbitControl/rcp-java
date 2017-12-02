package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.ColorOptions;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;

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

        if (_value != null) {

            // write rgb to stream
            int c = (_value.getBlue() << 16) | (_value.getGreen() << 8) | _value.getRed();

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
    public void write(final OutputStream _outputStream, final boolean all) throws IOException {

        // write mandatory fields and defaultValue
        _outputStream.write((int)getDatatype().id());

        if (getDefault() != null) {

            if (all || defaultValueChanged) {

                // use any of the default values id
                _outputStream.write((int)ColorOptions.DEFAULT.id());
                writeValue(getDefault(), _outputStream);

                if (!all) {
                    defaultValueChanged = false;
                }
            }
        } else if (defaultValueChanged) {

            _outputStream.write((int)ColorOptions.DEFAULT.id());
            writeValue(null, _outputStream);

            defaultValueChanged = false;
        }

        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);

    }
}
