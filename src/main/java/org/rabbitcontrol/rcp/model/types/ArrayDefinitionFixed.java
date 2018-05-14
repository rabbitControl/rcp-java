package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.StringOptions;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.*;

public class ArrayDefinitionFixed<T extends Collection, E> extends DefaultDefinition<T> {

    public static ArrayDefinitionFixed<?, ?> parse(final KaitaiStream _io) throws
                                                                         RCPDataErrorException {

        // parse mandatory subtype
        final DefaultDefinition<?> subtype_def = DefaultDefinition.parse(_io);

        // read mandatory length
        final int dimensions = (int)_io.readU4be();
        int[] dim_sizes = new int[dimensions];

        for (int i = 0; i < dimensions; i++) {
            dim_sizes[i] = (int)_io.readU4be();
        }

        // create ArrayDefinitionFixed
        final ArrayDefinitionFixed<?, ?> definition = create(subtype_def, dim_sizes);

        if (definition != null) {
            definition.parseOptions(_io);
        }

        return definition;
    }

    public static ArrayDefinitionFixed<?, ?> create(
            final DefaultDefinition<?> _sub_type,
            final int... _dimSizes) {

        switch (_sub_type.getDatatype()) {
            case BOOLEAN:
                return new ArrayDefinitionFixed<Collection, Boolean>((DefaultDefinition<Boolean>)
                                                                      _sub_type, _dimSizes);

            case INT8:
                return new ArrayDefinitionFixed<Collection, Byte>((DefaultDefinition<Byte>)_sub_type, _dimSizes);
            case UINT8:
                return new ArrayDefinitionFixed<Collection, Short>((DefaultDefinition<Short>)_sub_type, _dimSizes);
            case INT16:
                return new ArrayDefinitionFixed<Collection, Short>((DefaultDefinition<Short>)_sub_type, _dimSizes);
            case UINT16:
                return new ArrayDefinitionFixed<Collection, Integer>((DefaultDefinition<Integer>)_sub_type, _dimSizes);
            case INT32:
                return new ArrayDefinitionFixed<Collection, Integer>((DefaultDefinition<Integer>)_sub_type, _dimSizes);
            case UINT32:
                return new ArrayDefinitionFixed<Collection, Long>((DefaultDefinition<Long>)_sub_type, _dimSizes);
            case INT64:
                return new ArrayDefinitionFixed<Collection, Long>((DefaultDefinition<Long>)_sub_type, _dimSizes);
            case UINT64:
                return new ArrayDefinitionFixed<Collection, Long>((DefaultDefinition<Long>)_sub_type, _dimSizes);
            case FLOAT32:
                return new ArrayDefinitionFixed<Collection, Float>((DefaultDefinition<Float>)_sub_type, _dimSizes);
            case FLOAT64:
                return new ArrayDefinitionFixed<Collection, Double>((DefaultDefinition<Double>)_sub_type, _dimSizes);

            case STRING:
                return new ArrayDefinitionFixed<Collection, String>((DefaultDefinition<String>)_sub_type, _dimSizes);

            case ENUM:
                return new ArrayDefinitionFixed<Collection, Long>((DefaultDefinition<Long>)_sub_type, _dimSizes);

            case RGB:
                return new ArrayDefinitionFixed<Collection, Color>((DefaultDefinition<Color>)_sub_type, _dimSizes);

            case RGBA:
                return new ArrayDefinitionFixed<Collection, Color>((DefaultDefinition<Color>)_sub_type, _dimSizes);

            case FIXED_ARRAY:

                break;

            default:
                break;
        }

        return null;
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    final DefaultDefinition<E> subtype;

    private final int dimensions;

    private final int[] dimSizes;

    //
    private final boolean isFixed           = true;
    private       long    totalElementCount = 1;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public ArrayDefinitionFixed(final DefaultDefinition<E> _subtype, final int...
            _dimSizes) {

        super(Datatype.FIXED_ARRAY);

        subtype = _subtype;
        dimSizes = _dimSizes;
        dimensions = _dimSizes.length;

        for (final int dim_size : dimSizes) {
            if (dim_size <= 0) {
                // dynamic array!
                throw new RuntimeException("not fixed array in fixed array definition");
            }

            totalElementCount *= dim_size;
        }

        System.out.println("total element count: " + totalElementCount);
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
    public T readValue(final KaitaiStream _io) {






        T value;

        for (long i = 0; i < dimensions; i++) {
            // read from stream
            //value.add(subtype.readValue(_io));
        }

        //return value.toArray();
        return null;
    }


    private void writeCollection(final Collection<?> _value, final OutputStream _outputStream) throws
                                                                                               IOException {
        for (final Object o : _value) {

            if (o instanceof Collection) {
                writeCollection((Collection<?>)o, _outputStream);
            } else {
                // write value
                try {
                    subtype.writeValue((E)o, _outputStream);
                } catch (final ClassCastException _e) {
                    _e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void writeValue(final T _value, final OutputStream _outputStream) throws
                                                                                   IOException {


        if (_value != null) {
            writeCollection(_value, _outputStream);

            // check size...
//            if (_value.size() == arrayLength) {
//
//                for (final T t : _value) {
//                    subtype.writeValue(t, _outputStream);
//                }
//
//            } else {
//                // error!!
//                // we have to write something!!
//                // TODO: handle if _value.size() > arrayLength (truncate list...)
//
//                for (int i=0; i<arrayLength; i++) {
//                    subtype.writeValue(subtype.getDefault(), _outputStream);
//                }
//            }

        } else {

            // we have to write something
            for (int i=0; i<totalElementCount; i++) {
                subtype.writeValue(subtype.getDefault(), _outputStream);
            }

        }

    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        // write mandatory fields and defaultValue
        _outputStream.write((int)getDatatype().id());

        // write subtype
        subtype.write(_outputStream, _all);

        // write dimensions (4byte)
        _outputStream.write(ByteBuffer.allocate(4).putInt(dimensions).array());

        // write length for dimensions
        for (final int dimSize : dimSizes) {
            _outputStream.write(ByteBuffer.allocate(4).putInt(dimSize).array());
        }

        // write options
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

            // write list of subtype default values
            writeValue(null, _outputStream);

            defaultValueChanged = false;
        }

        if (!_all) {
            initialWrite = false;
        }

        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    public DefaultDefinition<E> getSubtype() {
        return subtype;
    }
}
