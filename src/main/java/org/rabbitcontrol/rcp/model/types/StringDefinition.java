package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.*;

import java.io.IOException;
import java.io.OutputStream;

public class StringDefinition extends DefaultDefinition<String> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    // options
    //----------------------------------------------------
    private String regex;
    private boolean regexChanged;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public StringDefinition() {

        super(Datatype.STRING);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final StringOptions option = StringOptions.byId(_propertyId);

        if (option == null) {
            return false;
        }

        switch (option) {
            case DEFAULT: {
                final LongString longString = new LongString(_io);
                setDefault(longString.data());
            }
                return true;

            case REGULAR_EXPRESSION:
            {
                final LongString longString = new LongString(_io);
                setRegex(longString.data());
            }
                return true;
        }

        return false;
    }

    @Override
    public String readValue(final KaitaiStream _io) {

        final LongString longString = new LongString(_io);
        return longString.data();
    }

    @Override
    public void writeValue(final String _value, final OutputStream _outputStream) throws
                                                                                  IOException {
        if (_value != null) {
            RCPParser.writeLongString(_value, _outputStream);
        } else if (defaultValue != null) {
            RCPParser.writeLongString(defaultValue, _outputStream);
        } else {
            RCPParser.writeLongString("", _outputStream);
        }
    }

    @Override
    public void writeOptions(final OutputStream _outputStream, final boolean _all) throws IOException {

        //
        // default
        //
        if (getDefault() != null) {

            if (_all || defaultValueChanged || initialWrite) {

                // use any of the default values id
                _outputStream.write((int)StringOptions.DEFAULT.id());
                writeValue(getDefault(), _outputStream);

                if (!_all) {
                    defaultValueChanged = false;
                }
            }
        } else if (defaultValueChanged) {

            _outputStream.write((int)StringOptions.DEFAULT.id());
            writeValue(null, _outputStream);

            defaultValueChanged = false;
        }


        //
        // regex
        //
        if (regex != null) {

            if (_all || regexChanged || initialWrite) {

                _outputStream.write((int)StringOptions.REGULAR_EXPRESSION.id());
                RCPParser.writeLongString(regex, _outputStream);

                if (!_all) {
                    regexChanged = false;
                }
            }
        } else if (regexChanged) {

            _outputStream.write((int)StringOptions.REGULAR_EXPRESSION.id());
            RCPParser.writeLongString("", _outputStream);

            regexChanged = false;
        }
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    public String getRegex() {
        return regex;
    }

    public void setRegex(final String _regex) {

        if ((regex == _regex) || ((regex != null) && regex.equals(_regex))) {
            return;
        }

        regex = _regex;
        regexChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }
}
