package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.RangeOptions;

import java.io.IOException;
import java.io.OutputStream;

public class RangeDefinition<T extends Number> extends DefaultDefinition<Range<T>> {


    public static <T extends Number> DefaultDefinition<Range<T>> create(final Datatype _datatype,
                                                                        final Class<T> _cls) {

        switch (_datatype) {

            case INT8:
                return (RangeDefinition<T>)new RangeDefinition<Byte>(new Int8Definition());
            case UINT8:
                return (RangeDefinition<T>)new RangeDefinition<Byte>(new UInt8Definition());
            case INT16:
                return (RangeDefinition<T>)new RangeDefinition<Short>(new Int16Definition());
            case UINT16:
                return (RangeDefinition<T>)new RangeDefinition<Short>(new UInt16Definition());
            case INT32:
                return (RangeDefinition<T>)new RangeDefinition<Integer>(new Int32Definition());
            case UINT32:
                return (RangeDefinition<T>)new RangeDefinition<Integer>(new UInt32Definition());
            case INT64:
                return (RangeDefinition<T>)new RangeDefinition<Long>(new Int64Definition());
            case UINT64:
                return (RangeDefinition<T>)new RangeDefinition<Long>(new UInt64Definition());
            case FLOAT32:
                return (RangeDefinition<T>)new RangeDefinition<Float>(new Float32Definition());
            case FLOAT64:
                return (RangeDefinition<T>)new RangeDefinition<Double>(new Float64Definition());

        }

        throw new RuntimeException("unhandled element type for RangeDefinition");
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
//    public static void parseOption(
//            final NumberDefinition<?> _typeDefinition,
//            final NumberProperty _dataid,
//            final KaitaiStream _io) throws RCPDataErrorException {
//
//        switch (_dataid) {
//
//            case SCALE:
//                _typeDefinition.setScale(NumberScale.byId(_io.readU1()));
//                break;
//
//            case UNIT:
//                final TinyString tinyString = new TinyString(_io);
//                _typeDefinition.setUnit(tinyString.data());
//                break;
//
//            default:
//                // not a number data id!!
//                throw new RCPDataErrorException();
//        }
//    }


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

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        // write mandatory fields and defaultValue
        _outputStream.write((int)getDatatype().id());

        elementType.write(_outputStream, _all);

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
        } else if (defaultValueChanged) {

            _outputStream.write((int)RangeOptions.DEFAULT.id());
            writeValue(null, _outputStream);

            defaultValueChanged = false;
        }

        if (!_all) {
            initialWrite = false;
        }

        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
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
