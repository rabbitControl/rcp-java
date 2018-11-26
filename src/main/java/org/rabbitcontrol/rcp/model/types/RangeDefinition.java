package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.RangeOptions;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;

import java.io.IOException;
import java.io.OutputStream;

public class RangeDefinition<T extends Number> extends DefaultDefinition<Range<T>> {

    public static <T extends Number> DefaultDefinition<Range<T>> create(final Class<T> _cls) {

        if (_cls == Byte.class) {
            return (RangeDefinition<T>)new RangeDefinition<Byte>(new Int8Definition());
        }
        if (_cls == Short.class) {
            return (RangeDefinition<T>)new RangeDefinition<Short>(new Int16Definition());
        }
        if (_cls == Integer.class) {
            return (RangeDefinition<T>)new RangeDefinition<Integer>(new Int32Definition());
        }
        if (_cls == Long.class) {
            return (RangeDefinition<T>)new RangeDefinition<Long>(new Int64Definition());
        }
        if (_cls == Float.class) {
            return (RangeDefinition<T>)new RangeDefinition<Float>(new Float32Definition());
        }
        if (_cls == Double.class) {
            return (RangeDefinition<T>)new RangeDefinition<Double>(new Float64Definition());
        }

        throw new RuntimeException("unhandled element type for RangeDefinition");
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    private final NumberDefinition<T> elementType;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public RangeDefinition(final NumberDefinition<T> elementType) {

        super(Datatype.RANGE);

        this.elementType = elementType;
    }

    @Override
    public void parseOptions(final KaitaiStream _io) throws RCPDataErrorException {

        if (elementType == null) {
            throw new RCPDataErrorException("no element type in arraydefinition");
        }

        elementType.parseOptions(_io);

        super.parseOptions(_io);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final RangeOptions option = RangeOptions.byId(_propertyId);

        switch (option) {
            case DEFAULT:
                final T v1 = elementType.readValue(_io);
                final T v2 = elementType.readValue(_io);
                setDefault(new Range<T>(v1, v2));
                return true;
        }

        return false;
    }

    @Override
    public Range<T> readValue(final KaitaiStream _io) {

        final T v1 = elementType.readValue(_io);
        final T v2 = elementType.readValue(_io);

        return new Range<T>(v1, v2);
    }

    @Override
    public void writeValue(final Range<T> _value, final OutputStream _outputStream) throws
                                                                                    IOException {

        if (_value != null) {
            elementType.writeValue(_value.getValue1(), _outputStream);
            elementType.writeValue(_value.getValue2(), _outputStream);
        }
        else {
            elementType.writeValue(defaultValue.getValue1(), _outputStream);
            elementType.writeValue(defaultValue.getValue2(), _outputStream);
        }
    }

    // override to write mandatory data after datatype and before options
    @Override
    public void writeMandatory(final OutputStream _outputStream) throws RCPException, IOException {

        if (elementType == null) {
            throw new RCPException("no elementtype");
        }

        _outputStream.write((int)elementType.getDatatype().id());
        elementType.writeMandatory(_outputStream);
    }

    @Override
    public void writeOptions(final OutputStream _outputStream, final boolean _all) throws
                                                                                   IOException,
                                                                                   RCPException {
        // write elementType options
        if (elementType == null) {
            throw new RCPException("no elementtype");
        }
        elementType.writeOptions(_outputStream, _all);
        _outputStream.write(RCPParser.TERMINATOR);

        //
        // default
        //
        if (getDefault() != null) {

            if (_all || defaultValueChanged || initialWrite) {

                // use any of the default values id
                _outputStream.write((int)RangeOptions.DEFAULT.id());
                writeValue(getDefault(), _outputStream);

                if (!_all) {
                    defaultValueChanged = false;
                }
            }
        }
        else if (defaultValueChanged) {

            _outputStream.write((int)RangeOptions.DEFAULT.id());
            writeValue(null, _outputStream);

            defaultValueChanged = false;
        }
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    public NumberDefinition<T> getElementType() {

        return elementType;
    }

    @Override
    public void dump() {

        System.out.println("--- rangetype ---");
        super.dump();

        System.out.println("--- element type ---");
        elementType.dump();
        System.out.println("------");

        System.out.println("default value: " + getDefault());
    }
}
