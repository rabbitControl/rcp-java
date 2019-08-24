package org.rabbitcontrol.rcp.model.widgets;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.*;
import org.rabbitcontrol.rcp.model.types.DefaultDefinition;
import org.rabbitcontrol.rcp.model.types.NumberDefinition;

import java.io.IOException;
import java.io.OutputStream;

public class NumberboxWidget<T extends Number> extends WidgetImpl {

    private Byte precision;

    private boolean precisionChanged;

    private NumberboxFormat format;

    private boolean formatChanged;

    private T stepsize;

    private boolean stepsizeChanged;

    private boolean cyclic;

    private boolean cyclicChanged;

    //
    public NumberboxWidget() {

        super(Widgettype.NUMBERBOX);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final NumberboxOptions option = NumberboxOptions.byId(_propertyId);

        switch (option) {

            case PRECISION:
                setPrecision(_io.readU1());
                return true;

            case FORMAT:
                setFormat(NumberboxFormat.byId(_io.readU1()));
                return true;

            case STEPSIZE:

                if (parameter != null) {
                    try {
                        // try to cast
                        final DefaultDefinition<T> def = (DefaultDefinition<T>)parameter.getTypeDefinition();

                        setStepsize(def.readValue(_io));
                        return true;
                    }
                    catch (final ClassCastException e) {
                        // nop
                    }
                }
                return false;

            case CYCLIC:
                setCyclic(_io.readU1() > 0);
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
        // order
        //
        if (precision != null) {

            if (_all || precisionChanged || initialWrite) {

                _outputStream.write((int)NumberboxOptions.PRECISION.id());
                _outputStream.write(precision.byteValue());

                if (!_all) {
                    precisionChanged = false;
                }
            }
        }
        else if (precisionChanged) {

            _outputStream.write((int)NumberboxOptions.PRECISION.id());
            _outputStream.write(0);

            precisionChanged = false;
        }

        //
        // format
        //
        if (format != null) {

            if (_all || formatChanged || initialWrite) {

                _outputStream.write((int)NumberboxOptions.FORMAT.id());
                _outputStream.write((int)format.id());

                if (!_all) {
                    formatChanged = false;
                }
            }
        }
        else if (formatChanged) {

            _outputStream.write((int)NumberboxOptions.FORMAT.id());
            _outputStream.write((int)NumberboxFormat.DEC.id());

            formatChanged = false;
        }

        //
        // stepsize
        //
        if ((stepsize != null) && (parameter != null)) {

            if (_all || stepsizeChanged || initialWrite) {

                try {
                    // try to cast
                    final NumberDefinition<T>
                            def
                            = (NumberDefinition<T>)parameter.getTypeDefinition();

                    _outputStream.write((int)NumberboxOptions.STEPSIZE.id());
                    // write via typedefinition
                    def.writeValue(def.convertNumberValue(stepsize), _outputStream);
                }
                catch (final ClassCastException e) {
                    // nop
                }

                if (!_all) {
                    formatChanged = false;
                }
            }
        }
        else if (formatChanged) {

            try {
                // try to cast
                final DefaultDefinition<T>
                        def
                        = (DefaultDefinition<T>)parameter.getTypeDefinition();

                _outputStream.write((int)NumberboxOptions.STEPSIZE.id());
                def.writeValue(null, _outputStream);

            }
            catch (final ClassCastException e) {
                // nop
            }

            formatChanged = false;
        }

        //
        // cyclic
        //
        if (_all || cyclicChanged || initialWrite) {

            _outputStream.write((int)NumberboxOptions.CYCLIC.id());
            _outputStream.write(cyclic ? 1 : 0);

            if (!_all) {
                cyclicChanged = false;
            }
        }

        // write terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    public Byte getPrecision() {

        return precision;
    }

    public void setPrecision(final Integer _precision) {

        final Byte new_precision = _precision.byteValue();

        if ((precision == new_precision) || (new_precision.equals(precision))) {
            return;
        }

        precision = new_precision;
        precisionChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    public NumberboxFormat getFormat() {

        return format;
    }

    public void setFormat(final NumberboxFormat _format) {

        if ((format == _format) || ((format != null) && format.equals(_format))) {
            return;
        }

        format = _format;
        formatChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    public T getStepsize() {

        return stepsize;
    }

    public void setStepsize(final T _stepsize) {

        if ((stepsize == _stepsize) || ((stepsize != null) && stepsize.equals(_stepsize))) {
            return;
        }

        stepsize = _stepsize;
        stepsizeChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    public boolean isCyclic() {

        return cyclic;
    }

    public void setCyclic(final boolean _cyclic) {

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

        System.out.println("precision: " + precision);
        System.out.println("format: " + (format != null ? format.name() : "null"));
        System.out.println("stepsize: " + stepsize);
        System.out.println("cyclic: " + cyclic);
    }
}
