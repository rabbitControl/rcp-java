package org.rabbitcontrol.rcp.model.widgets;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.TextboxOptions;
import org.rabbitcontrol.rcp.model.RcpTypes.Widgettype;

import java.io.IOException;
import java.io.OutputStream;

public class TextboxWidget extends WidgetImpl {


    // optional
    private boolean multiline;
    private boolean multilineChanged;

    private boolean wordwrap;
    private boolean wordwrapChanged;

    private boolean password;
    private boolean passwordChanged;

    public TextboxWidget() {
        super(Widgettype.TEXTBOX);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final TextboxOptions option = TextboxOptions.byId(_propertyId);

        switch (option) {

            case MULTILINE:
                setMultiline(_io.readU1() > 0);
                return true;

            case WORDWRAP:
                setWordwrap(_io.readU1() > 0);
                return true;

            case PASSWORD:
                setPassword(_io.readU1() > 0);
                return true;
        }

        return false;
    }


    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        // write basic stuff first
        super.write(_outputStream, _all);

        // write own options

        //
        // multiline
        //
        if (_all || multilineChanged || initialWrite) {

            _outputStream.write((int)TextboxOptions.MULTILINE.id());
            _outputStream.write(multiline ? 1 : 0);

            // clear flag
            if (!_all) {
                multilineChanged = false;
            }
        }

        //
        // wordwrap
        //
        if (_all || wordwrapChanged || initialWrite) {

            _outputStream.write((int)TextboxOptions.WORDWRAP.id());
            _outputStream.write(wordwrap ? 1 : 0);

            // clear flag
            if (!_all) {
                wordwrapChanged = false;
            }
        }

        //
        // password
        //
        if (_all || passwordChanged || initialWrite) {

            _outputStream.write((int)TextboxOptions.PASSWORD.id());
            _outputStream.write(password ? 1 : 0);

            // clear flag
            if (!_all) {
                passwordChanged = false;
            }
        }

        // write terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }


    //
    private boolean isMultiline() {

        return multiline;
    }

    private void setMultiline(final boolean v) {

        if (multiline == v) {
            return;
        }

        multiline = v;
        multilineChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    private boolean isWordwrap() {

        return wordwrap;
    }

    private void setWordwrap(final boolean v) {

        if (wordwrap == v) {
            return;
        }

        wordwrap = v;
        wordwrapChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    private boolean isPassword() {

        return password;
    }

    private void setPassword(final boolean v) {

        if (password == v) {
            return;
        }

        password = v;
        passwordChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    @Override
    public void dump() {

        super.dump();

        System.out.println("multiline: " + multiline);
        System.out.println("wordwrap: " + wordwrap);
        System.out.println("password: " + password);
    }
}
