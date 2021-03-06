package org.rabbitcontrol.rcp.model.widgets;

import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.Widgettype;

import java.io.IOException;
import java.io.OutputStream;

public class DropdownWidget extends WidgetImpl {

    public DropdownWidget() {
        super(Widgettype.DROPDOWN);
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        super.write(_outputStream, _all);

        // finalize parameter with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }
}
