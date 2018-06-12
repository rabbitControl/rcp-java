package org.rabbitcontrol.rcp.model.widgets;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RcpTypes.WidgetOptions;
import org.rabbitcontrol.rcp.model.RcpTypes.Widgettype;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.NumberParameter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public abstract class WidgetImpl implements Widget {


    public static <T extends Number> Widget parse(final KaitaiStream _io, NumberParameter<T>
            parameter) throws
                                                                                   RCPDataErrorException {

        // read mandatory widgettype
        final Widgettype widget_type = Widgettype.byId(_io.readS2be());

        if (widget_type == Widgettype.NUMBERBOX) {

            final NumberboxWidget<T> widget = new NumberboxWidget<T>();
            widget.setParameter(parameter);
            widget.parseOptions(_io);
            return widget;
        }

        return nonTypedWidget(widget_type, _io);
    }

    public static Widget parse(final KaitaiStream _io) throws RCPDataErrorException {

        // read mandatory widgettype
        final Widgettype widget_type = Widgettype.byId(_io.readS2be());

        return nonTypedWidget(widget_type, _io);
    }

    private static Widget nonTypedWidget(final Widgettype wt, final KaitaiStream _io) throws
                                                                                     RCPDataErrorException {

        if (wt == null) {
            throw new RCPDataErrorException();
        }

        WidgetImpl widget = null;

        switch (wt) {

            case CUSTOMWIDGET:
                widget = new CustomWidget();
                break;
            case INFO:
                widget = new InfoWidget();
                break;
            case TEXTBOX:
                widget = new TextboxWidget();
                break;
            case BANG:
                widget = new BangWidget();
                break;
            case PRESS:
                widget = new PressWidget();
                break;
            case TOGGLE:
                widget = new ToggleWidget();
                break;
            case NUMBERBOX:
                // no type...
                //widget = new NumberboxWidget<>()
                break;
            case DIAL:
                widget = new DialWidget();
                break;
            case SLIDER:
                widget = new SliderWidget();
                break;
            case SLIDER2D:
                widget = new Slider2DWidget();
                break;
            case RANGE:
                widget = new RangeWidget();
                break;
            case DROPDOWN:
                widget = new DropdownWidget();
                break;
            case RADIOBUTTON:
                widget = new RadiobuttonWidget();
                break;
            case COLORBOX:
                widget = new ColorboxWidget();
                break;
            case TABLE:
                widget = new TableWidget();
                break;
            case FILECHOOSER:
                widget = new FilechooserWidget();
                break;
            case DIRECTORYCHOOSER:
                widget = new DirectorychooserWidget();
                break;
            case IP:
                widget = new IPWidget();
                break;
            case LIST:
                widget = new ListWidget();
                break;
            case LISTPAGE:
                widget = new ListpageWidget();
                break;
            case TABS:
                widget = new TabsWidget();
                break;
        }

        if (widget != null) {
            widget.parseOptions(_io);
        }

        return widget;
    }

    // mandatory
    private final Widgettype widgettype;

    // optional
    private boolean enabled = true;
    private boolean enableChanged;

    private boolean labelVisible = true;
    private boolean labelVisibleChanged;

    private boolean valueVisible = true;
    private boolean valueVisibleChanged;

    protected boolean initialWrite = true; // one-time-flag

    protected IParameter parameter;
    //
    WidgetImpl(final Widgettype widgettype) {

        this.widgettype = widgettype;
    }

    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {
        return false;
    }

    protected void parseOptions(final KaitaiStream _io) throws RCPDataErrorException {

        // get options from the stream
        while (true) {

            // get data-id
            final int property_id = _io.readU1();

            if (property_id == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            final WidgetOptions option = WidgetOptions.byId(property_id);

            if (option == null) {
                if (!handleOption(property_id, _io)) {
                    throw new RCPDataErrorException();
                }
                continue;
            }

            switch (option) {
                case ENABLED:
                    setEnabled(_io.readU1() > 0);
                    break;
                case LABEL_VISIBLE:
                    setLabelVisible(_io.readU1() > 0);
                    break;
                case VALUE_VISIBLE:
                    setValueVisible(_io.readU1() > 0);
                    break;

                default:
                    if (!handleOption(property_id, _io)) {
                        throw new RCPDataErrorException();
                    }
            }
        }
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        // write mandatory fields and defaultValue
        _outputStream.write(ByteBuffer.allocate(2).putShort((short)widgettype.id()).array());


        //
        // enabled
        //
        if (_all || enableChanged || initialWrite) {

            _outputStream.write((int)WidgetOptions.ENABLED.id());
            _outputStream.write(enabled ? 1 : 0);

            // clear flag
            if (!_all) {
                enableChanged = false;
            }
        }

        //
        // label visible
        //
        if (_all || labelVisibleChanged || initialWrite) {

            _outputStream.write((int)WidgetOptions.LABEL_VISIBLE.id());
            _outputStream.write(labelVisible ? 1 : 0);

            // clear flag
            if (!_all) {
                labelVisibleChanged = false;
            }
        }


        //
        // value visible
        //
        if (_all || valueVisibleChanged || initialWrite) {

            _outputStream.write((int)WidgetOptions.VALUE_VISIBLE.id());
            _outputStream.write(valueVisible ? 1 : 0);

            // clear flag
            if (!_all) {
                valueVisibleChanged = false;
            }
        }


        if (!_all) {
            initialWrite = false;
        }
    }

    @Override
    public Widgettype getWidgettype() {

        return widgettype;
    }

    @Override
    public void setEnabled(final boolean enable) {

        if (enabled == enable) {
            return;
        }

        enabled = enable;
        enableChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    @Override
    public boolean isEnabled() {

        return enabled;
    }

    @Override
    public void setLabelVisible(final boolean visible) {

        if (labelVisible == visible) {
            return;
        }

        labelVisible = visible;
        labelVisibleChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    @Override
    public boolean isLabelVisible() {

        return labelVisible;
    }

    @Override
    public void setValueVisible(final boolean visible) {

        if (valueVisible == visible) {
            return;
        }

        valueVisible = visible;
        valueVisibleChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    @Override
    public boolean isValueVisible() {

        return valueVisible;
    }

    @Override
    public void setParameter(IParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public void dump() {

        System.out.println("--- widget type: " + widgettype.name());
        System.out.println("enabled: " + enabled);
        System.out.println("label-visible: " + labelVisible);
        System.out.println("value-visible: " + valueVisible);
    }
}
