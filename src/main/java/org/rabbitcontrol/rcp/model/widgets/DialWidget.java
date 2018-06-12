package org.rabbitcontrol.rcp.model.widgets;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.DialOptions;
import org.rabbitcontrol.rcp.model.RcpTypes.Widgettype;

import java.io.IOException;
import java.io.OutputStream;

public class DialWidget extends WidgetImpl {

    boolean cyclic;
    boolean cyclicChanged;

    public DialWidget() {
        super(Widgettype.DIAL);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final DialOptions option = DialOptions.byId(_propertyId);

        switch (option) {

            case CYCLIC:
                setCyclic(_io.readU1() > 0);
                return true;
        }

        return false;
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        super.write(_outputStream, _all);

        //
        // cyclic
        //
        if (_all || cyclicChanged || initialWrite) {

            _outputStream.write((int)DialOptions.CYCLIC.id());
            _outputStream.write(cyclic ? 1 : 0);

            if (!_all) {
                cyclicChanged = false;
            }
        }


        // write terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    private boolean isCyclic() {

        return cyclic;
    }

    private void setCyclic(final boolean _cyclic) {

        if (cyclic == _cyclic) {
            return;
        }

        cyclic = _cyclic;
        cyclicChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    @Override
    public void dump() {

        super.dump();

        System.out.println("cyclic: " + cyclic);
    }
}
