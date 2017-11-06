package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.StringOptions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ArrayDefinition<T> extends DefaultDefinition<List<T>> {

    public static ArrayDefinition<?> parse(final KaitaiStream _io) throws RCPDataErrorException {

        // parse mandatory subtype
        final DefaultDefinition<?> subtype_def = DefaultDefinition.parse(_io);

        // read mandatory length
        final long arraysize = _io.readU4be();

        // create ArrayDefinition
        final ArrayDefinition<?> definition = create(subtype_def, arraysize);

        if (definition != null) {
            definition.parseOptions(_io);
        }

        return definition;
    }

    public static ArrayDefinition<?> create(
            final DefaultDefinition<?> _sub_type,
            final long _size) {

        switch (_sub_type.getDatatype()) {
            case BOOLEAN:
                return new ArrayDefinition<Boolean>((DefaultDefinition<Boolean>)_sub_type, _size);

            case INT8:
                return new ArrayDefinition<Byte>((DefaultDefinition<Byte>)_sub_type, _size);
            case UINT8:
                return new ArrayDefinition<Short>((DefaultDefinition<Short>)_sub_type, _size);
            case INT16:
                return new ArrayDefinition<Short>((DefaultDefinition<Short>)_sub_type, _size);
            case UINT16:
                return new ArrayDefinition<Integer>((DefaultDefinition<Integer>)_sub_type, _size);
            case INT32:
                return new ArrayDefinition<Integer>((DefaultDefinition<Integer>)_sub_type, _size);
            case UINT32:
                return new ArrayDefinition<Long>((DefaultDefinition<Long>)_sub_type, _size);
            case INT64:
                return new ArrayDefinition<Long>((DefaultDefinition<Long>)_sub_type, _size);
            case UINT64:
                return new ArrayDefinition<Long>((DefaultDefinition<Long>)_sub_type, _size);
            case FLOAT32:
                return new ArrayDefinition<Float>((DefaultDefinition<Float>)_sub_type, _size);
            case FLOAT64:
                return new ArrayDefinition<Double>((DefaultDefinition<Double>)_sub_type, _size);

            case STRING:
                return new ArrayDefinition<String>((DefaultDefinition<String>)_sub_type, _size);

            case FIXED_ARRAY:
                return new ArrayDefinition<ArrayDefinition<?>>(
                        (DefaultDefinition<ArrayDefinition<?>>)_sub_type,
                                                               _size);

            default:
                break;
        }

        return null;
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    final DefaultDefinition<T> subtype;

    final long arrayLength;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public ArrayDefinition(final DefaultDefinition<T> _subtype, final long _size) {

        super(Datatype.FIXED_ARRAY);

        subtype = _subtype;
        arrayLength = _size;
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final StringOptions option = StringOptions.byId(_propertyId);

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
    public List<T> readValue(final KaitaiStream _io) {

        List<T> value = new ArrayList<T>();
        for (long i = 0; i < arrayLength; i++) {
            // read from stream
            value.add(subtype.readValue(_io));
        }

        return value;
    }

    @Override
    public void writeValue(final List<T> _value, final OutputStream _outputStream) throws
                                                                                   IOException {

        for (final T t : _value) {
            subtype.writeValue(t, _outputStream);
        }
    }

    @Override
    public void write(final OutputStream _outputStream) throws IOException {

        // write mandatory fields and defaultValue
        _outputStream.write((int)getDatatype().id());

        // write subtype
        subtype.write(_outputStream);

        // write length (4byte)
        _outputStream.write(ByteBuffer.allocate(4).putInt((int)arrayLength).array());

        // write options
        if (getDefault() != null) {
            // use any of the default values id
            _outputStream.write((int)StringOptions.DEFAULT.id());
            writeValue(getDefault(), _outputStream);
        }

        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }
}
