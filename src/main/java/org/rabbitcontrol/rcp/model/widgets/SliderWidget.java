package org.rabbitcontrol.rcp.model.widgets;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.SliderOptions;
import org.rabbitcontrol.rcp.model.RcpTypes.Widgettype;

import java.io.IOException;
import java.io.OutputStream;

public class SliderWidget extends WidgetImpl {

    //
    boolean horizontal = true;

    boolean horizontalChanged;

    //
    public SliderWidget() {

        super(Widgettype.SLIDER);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final SliderOptions option = SliderOptions.byId(_propertyId);

        switch (option) {

            case HORIZONTAL:
                setHorizontal(_io.readU1() > 0);
                return true;
        }

        return false;
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        super.write(_outputStream, _all);

        //
        // horizontal
        //
        if (_all || horizontalChanged || initialWrite) {

            _outputStream.write((int)SliderOptions.HORIZONTAL.id());
            _outputStream.write(horizontal ? 1 : 0);

            if (!_all) {
                horizontalChanged = false;
            }
        }

        // write terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    private boolean isHorizontal() {

        return horizontal;
    }

    private void setHorizontal(final boolean _cyclic) {

        if (horizontal == _cyclic) {
            return;
        }

        horizontal = _cyclic;
        horizontalChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    @Override
    public void dump() {

        super.dump();

        System.out.println("horizontal: " + horizontal);
    }
}
