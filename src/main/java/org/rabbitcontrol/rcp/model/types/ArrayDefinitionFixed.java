package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPFactory;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.StringOptions;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;

public class ArrayDefinitionFixed<T, E> extends DefaultDefinition<T> {

    public static ArrayDefinitionFixed<?, ?> parse(final KaitaiStream _io) throws
                                                                           RCPDataErrorException {

        // parse mandatory subtype
        final DefaultDefinition<?> subtype_def = DefaultDefinition.parse(_io);

        // read mandatory length
        final int   dimensions = (int)_io.readU4be();
        final int[] dim_sizes  = new int[dimensions];

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
            final DefaultDefinition<?> _sub_type, final int... _dimSizes) {

        final Object default_value = Array.newInstance(RCPFactory.getClass(_sub_type.getDatatype()), _dimSizes);

        switch (_sub_type.getDatatype()) {
            case BOOLEAN: {

                return new ArrayDefinitionFixed<Object, Boolean>((DefaultDefinition<Boolean>)
                                                                         _sub_type,
                                                                 default_value,
                                                                 _dimSizes);
            }

            case INT8: {
                return new ArrayDefinitionFixed<Object, Byte>((DefaultDefinition<Byte>)_sub_type,
                                                              default_value,
                                                              _dimSizes);
            }
            case UINT8:
                return new ArrayDefinitionFixed<Object, Short>((DefaultDefinition<Short>)_sub_type,
                                                               default_value,
                                                               _dimSizes);
            case INT16:
                return new ArrayDefinitionFixed<Object, Short>((DefaultDefinition<Short>)_sub_type,
                                                               default_value,
                                                               _dimSizes);
            case UINT16:
                return new ArrayDefinitionFixed<Object, Integer>((DefaultDefinition<Integer>)
                                                                         _sub_type,
                                                                 default_value,
                                                                 _dimSizes);
            case INT32:
                return new ArrayDefinitionFixed<Object, Integer>((DefaultDefinition<Integer>)
                                                                         _sub_type,
                                                                 default_value,
                                                                 _dimSizes);
            case UINT32:
                return new ArrayDefinitionFixed<Object, Long>((DefaultDefinition<Long>)_sub_type,
                                                              default_value,
                                                              _dimSizes);
            case INT64:
                return new ArrayDefinitionFixed<Object, Long>((DefaultDefinition<Long>)_sub_type,
                                                              default_value,
                                                              _dimSizes);
            case UINT64:
                return new ArrayDefinitionFixed<Object, Long>((DefaultDefinition<Long>)_sub_type,
                                                              default_value,
                                                              _dimSizes);
            case FLOAT32:
                return new ArrayDefinitionFixed<Object, Float>((DefaultDefinition<Float>)_sub_type,
                                                               default_value,
                                                               _dimSizes);
            case FLOAT64:
                return new ArrayDefinitionFixed<Object, Double>((DefaultDefinition<Double>)
                                                                        _sub_type,
                                                                default_value,
                                                                _dimSizes);

            case STRING:
                return new ArrayDefinitionFixed<Object, String>((DefaultDefinition<String>)
                                                                        _sub_type,
                                                                default_value,
                                                                _dimSizes);

            case ENUM:
                return new ArrayDefinitionFixed<Object, String>((DefaultDefinition<String>)
                                                                        _sub_type,
                                                                default_value,
                                                              _dimSizes);

            case RGB:
                return new ArrayDefinitionFixed<Object, Color>((DefaultDefinition<Color>)_sub_type,
                                                               default_value,
                                                               _dimSizes);

            case RGBA:
                return new ArrayDefinitionFixed<Object, Color>((DefaultDefinition<Color>)_sub_type,
                                                               default_value,
                                                               _dimSizes);

            case FIXED_ARRAY:
                //throw new RCPParameterException("no array of array...");
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

    public ArrayDefinitionFixed(
            final DefaultDefinition<E> _subtype,
            final T _value,
            final int... _dimSizes) {

        super(Datatype.FIXED_ARRAY, _value);

        if ((_value == null) || !_value.getClass().isArray()) {
            throw new RuntimeException("needs an array!");
        }

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

    private void readToArray(
            final Object _array,
            final int _dim,
            final KaitaiStream _io) {

        if (_dim >= dimSizes.length) {
            // error!
            System.err.println("reading dimension count!!");
            return;
        }

        final int         max  = dimSizes[_dim];
        for (int i = 0; i < max; i++) {

            Object o = Array.get(_array, i);

            if ((o == null) || !o.getClass().isArray()) {
                E v = subtype.readValue(_io);
                Array.set(_array, i, v);
            }
            else {
                readToArray(o, _dim + 1, _io);
            }
        }
    }

    @Override
    public T readValue(final KaitaiStream _io) {

        Object array = Array.newInstance(RCPFactory.getClass(subtype.getDatatype()), dimSizes);

        readToArray(array, 0, _io);

        //return value.toArray();
        return (T)array;
    }

    private void writeArray(
            final Object _array,
            final int _dim,
            final OutputStream _outputStream) throws
                                              IOException {

        if (_dim >= dimSizes.length) {
            // error!
            System.err.println("wring dimension count!!");
            return;
        }

        for (int i = 0; i < Array.getLength(_array); i++) {

            final Object o = Array.get(_array, i);

            if (o == null) {
                subtype.writeValue(null, _outputStream);
            }
            else if (o instanceof Collection) {
                writeCollection((Collection<?>)o, _dim + 1, _outputStream);
            }
            else if (o.getClass().isArray()) {
                writeArray(o, _dim + 1, _outputStream);
            }
            else {
                // write value
                try {
                    subtype.writeValue((E)o, _outputStream);
                }
                catch (final ClassCastException _e) {
                    _e.printStackTrace();
                }
            }
        }

    }

    private void writeCollection(
            final Collection<?> _value,
            final int _dim,
            final OutputStream _outputStream) throws
                                              IOException {

        if (_dim >= dimSizes.length) {
            // error!
            System.err.println("wring dimension count!!");
            return;
        }

        final Iterator<?> iter = _value.iterator();
        final int         max  = dimSizes[_dim];

        for (int i = 0; i < max; i++) {
            if (iter.hasNext()) {
                final Object o = iter.next();

                if (o instanceof Collection) {
                    writeCollection((Collection<?>)o, _dim + 1, _outputStream);
                }
                else if (o.getClass().isArray()) {
                    writeArray(o, _dim + 1, _outputStream);
                }
                else {
                    // write value
                    try {
                        subtype.writeValue((E)o, _outputStream);
                    }
                    catch (final ClassCastException _e) {
                        _e.printStackTrace();
                    }

                }

            }
            else {
                // write 0
                subtype.writeValue(null, _outputStream);
            }
        }
    }

    @Override
    public void writeValue(final T _value, final OutputStream _outputStream) throws IOException {

        if (_value != null) {
            if (_value.getClass().isArray()) {
                writeArray(_value, 0, _outputStream);
            }
            else {
                // error
            }

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

        }
        else {

            // we have to write something
            for (int i = 0; i < totalElementCount; i++) {
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
        }
        else if (defaultValueChanged) {

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

    public int[] getDimSizes() {
        return dimSizes;
    }
}
