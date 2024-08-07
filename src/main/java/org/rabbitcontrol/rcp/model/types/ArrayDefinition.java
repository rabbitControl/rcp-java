package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPFactory;
import org.rabbitcontrol.rcp.model.RcpTypes.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.nio.ByteBuffer;

public class ArrayDefinition<T, E> extends DefaultDefinition<T> {

    public static ArrayDefinition<?, ?> parse(final KaitaiStream _io) throws
                                                                           RCPDataErrorException {

        // parse mandatory elementType
        final DefaultDefinition<?> subtype_def = DefaultDefinition.parse(_io);

        // get structure from stream
        final int   dims = _io.readS4be();
        final int[] dim_sizes  = new int[dims];
        for (int i = 0; i < dims; i++) {
            dim_sizes[i] = _io.readS4be();
        }

        // create ArrayDefinition
        final ArrayDefinition<?, ?> definition = create(subtype_def, dim_sizes);

        return definition;
    }

    public static ArrayDefinition<?, ?> create(
            final DefaultDefinition<?> _sub_type, final int... _dimSizes) {

        switch (_sub_type.getDatatype()) {
            case BOOLEAN:

                return new ArrayDefinition<Object, Boolean>((DefaultDefinition<Boolean>)
                                                                         _sub_type,
                                                            _dimSizes);

            case INT8:
                return new ArrayDefinition<Object, Byte>((DefaultDefinition<Byte>)_sub_type,
                                                         _dimSizes);
            case UINT8:
                return new ArrayDefinition<Object, Short>((DefaultDefinition<Short>)_sub_type,
                                                          _dimSizes);
            case INT16:
                return new ArrayDefinition<Object, Short>((DefaultDefinition<Short>)_sub_type,
                                                          _dimSizes);
            case UINT16:
                return new ArrayDefinition<Object, Integer>((DefaultDefinition<Integer>)
                                                                         _sub_type,
                                                            _dimSizes);
            case INT32:
                return new ArrayDefinition<Object, Integer>((DefaultDefinition<Integer>)
                                                                         _sub_type,
                                                            _dimSizes);
            case UINT32:
                return new ArrayDefinition<Object, Long>((DefaultDefinition<Long>)_sub_type,
                                                         _dimSizes);
            case INT64:
                return new ArrayDefinition<Object, Long>((DefaultDefinition<Long>)_sub_type,
                                                         _dimSizes);
            case UINT64:
                return new ArrayDefinition<Object, Long>((DefaultDefinition<Long>)_sub_type,
                                                         _dimSizes);
            case FLOAT32:
                return new ArrayDefinition<Object, Float>((DefaultDefinition<Float>)_sub_type,
                                                          _dimSizes);
            case FLOAT64:
                return new ArrayDefinition<Object, Double>((DefaultDefinition<Double>)
                                                                        _sub_type,
                                                           _dimSizes);

            case STRING:
                return new ArrayDefinition<Object, String>((DefaultDefinition<String>)
                                                                        _sub_type,
                                                           _dimSizes);

            case ENUM:
                return new ArrayDefinition<Object, String>((DefaultDefinition<String>)
                                                                        _sub_type,
                                                           _dimSizes);

            case RGB:
                return new ArrayDefinition<Object, Color>((DefaultDefinition<Color>)_sub_type,
                                                          _dimSizes);

            case RGBA:
                return new ArrayDefinition<Object, Color>((DefaultDefinition<Color>)_sub_type,
                                                          _dimSizes);

            case IPV4:
                return new ArrayDefinition<Object, Inet4Address>((DefaultDefinition<Inet4Address>)_sub_type,
                        _dimSizes);

            case ARRAY:
                //throw new RCPParameterException("no array of array...");
                break;

            case LIST:
                break;

            default:
                break;
        }

        return null;
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    final DefaultDefinition<E> elementType;

    private int dimensions;

    private int[] dimSizes;

    //------------------------------------------------------------
    //------------------------------------------------------------

    public ArrayDefinition(
            final DefaultDefinition<E> _elementType,
            final int... _dimSizes) {

        this(_elementType, null, _dimSizes);
    }

    public ArrayDefinition(
            final DefaultDefinition<E> _elementType,
            final T _defaultValue,
            final int... _dimSizes) {

        super(Datatype.ARRAY, _defaultValue);

        elementType = _elementType;
        dimensions = _dimSizes.length;
        dimSizes = _dimSizes;

        // calculate total element count
        for (final int dim_size : dimSizes) {
            if (dim_size <= 0) {
                // invalid dimension size!
                // dynamic array?
                throw new RuntimeException("not fixed array in fixed array definition");
            }
        }
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final ArrayOptions option = ArrayOptions.byId(_propertyId);

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
            final int[] _dim_sizes,
            final KaitaiStream _io) {

        if (_dim >= _dim_sizes.length) {
            // error!
            System.err.println("reading dimension count!!");
            return;
        }

        final int         max  = _dim_sizes[_dim];
        for (int i = 0; i < max; i++) {

            final Object o = Array.get(_array, i);

            if ((o == null) || !o.getClass().isArray()) {
                final E v = elementType.readValue(_io);
                Array.set(_array, i, v);
            }
            else {
                readToArray(o, _dim + 1, _dim_sizes, _io);
            }
        }
    }

    @Override
    public T readValue(final KaitaiStream _io) throws ClassCastException {

        // create array
        final Object array = Array.newInstance(RCPFactory.getClass(elementType.getDatatype()), dimSizes);

        readToArray(array, 0, dimSizes, _io);

        // this may fail if dimensions are different...
        return (T)array;
    }

//    private void writeArray(
//            final Object _array,
//            final int _dim,
//            final int[] _dimSizes,
//            final OutputStream _outputStream) throws
//                                              IOException {
//
//        if (_dim >= _dimSizes.length) {
//            // error!
//            System.err.println("wrong dimension count!!");
//            return;
//        }
//
//        for (int i = 0; i < Array.getLength(_array); i++) {
//
//            final Object o = Array.get(_array, i);
//
//            if (o instanceof Collection) {
//                writeCollection((Collection<?>)o, _dim + 1, _dimSizes, _outputStream);
//            }
//            else if (o.getClass().isArray()) {
//                writeArray(o, _dim + 1, _dimSizes, _outputStream);
//            }
//            else {
//                // write value
//                try {
//                    elementType.writeValue((E)o, _outputStream);
//                }
//                catch (final ClassCastException _e) {
//                    _e.printStackTrace();
//                }
//            }
//        }
//
//    }
//
//    private void writeCollection(
//            final Collection<?> _value,
//            final int _dim, final int[] _dimSizes,
//            final OutputStream _outputStream) throws
//                                              IOException {
//
//        if (_dim >= _dimSizes.length) {
//            // error!
//            System.err.println("wring dimension count!!");
//            return;
//        }
//
//        final Iterator<?> iter = _value.iterator();
//        final int         max  = dimSizes[_dim];
//
//        for (int i = 0; i < max; i++) {
//            if (iter.hasNext()) {
//                final Object o = iter.next();
//
//                if (o instanceof Collection) {
//                    writeCollection((Collection<?>)o, _dim + 1, _dimSizes, _outputStream);
//                }
//                else if (o.getClass().isArray()) {
//                    writeArray(o, _dim + 1, _dimSizes, _outputStream);
//                }
//                else {
//                    // write value
//                    try {
//                        elementType.writeValue((E)o, _outputStream);
//                    }
//                    catch (final ClassCastException _e) {
//                        _e.printStackTrace();
//                    }
//
//                }
//
//            }
//            else {
//                // write 0
//                elementType.writeValue(null, _outputStream);
//            }
//        }
//    }

    void writeArrayData(
            final Object _array,
            final int _dim,
            final int[] _dimSizes,
            final OutputStream _outputStream) throws IOException {

        if (_dim >= _dimSizes.length) {
            System.err.println("dimensions > size of dimensions sizes!");
            return;
        }

        if (!_array.getClass().isArray()) {
            throw new RuntimeException("object is not an array!");
        }

        // get array length
        final int arr_length = Array.getLength(_array);

        if ((_dimSizes[_dim] > 0) && (_dimSizes[_dim] != arr_length)) {
            throw new RuntimeException("can not write jagged array!");
        }

        // set dimension size
        _dimSizes[_dim] = arr_length;

        // iterate array, set values...
        for (int i = 0; i < Array.getLength(_array); i++) {

            // get object from array
            final Object o = Array.get(_array, i);

            if (o == null) {
                // write default value
                elementType.writeValue(null, _outputStream);
                continue;
            }

            if (o.getClass().isArray()) {
                writeArrayData(o, _dim + 1, _dimSizes, _outputStream);
            } else {
                // write 0
                // write value
                elementType.writeValue((E)o, _outputStream);
            }
        }

    }

    @Override
    public void writeValue(final T _value, final OutputStream _outputStream) throws IOException {

        final T value_to_write;
        if (_value != null) {
            value_to_write = _value;
        } else {
            value_to_write = defaultValue;
        }

        if (value_to_write != null) {

            if (value_to_write.getClass().isArray()) {

                final ByteArrayOutputStream array_data = new ByteArrayOutputStream();
                try {
                    writeArrayData(value_to_write, 0, dimSizes, array_data);

                    // write data
                    _outputStream.write(array_data.toByteArray());

                } catch (final RuntimeException _e) {
                    throw new IOException(_e.getMessage());
                } finally {
                    array_data.close();
                }

            }
            else {
                // error - write 0
                _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());
            }

        }
        else {
            // write 0
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());
        }

    }

    // override to write mandatory data after datatype and before options
    @Override
    public void writeMandatory(final OutputStream _outputStream) throws RCPException, IOException {

        if (elementType == null) {
            throw new RCPException("no elementtype");
        }

        elementType.write(_outputStream, false);

        // write structure
        _outputStream.write(ByteBuffer.allocate(4).putInt(dimensions).array());
        for (int i = 0; i < dimensions; i++) {
            _outputStream.write(ByteBuffer.allocate(4).putInt(dimSizes[i]).array());
        }
    }

    @Override
    public void writeOptions(final OutputStream _outputStream, final boolean _all) throws
                                                                                   IOException,
                                                                                   RCPException {

        // write default value
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

            // write list of elementType default values
            writeValue(null, _outputStream);

            defaultValueChanged = false;
        }

    }

    public DefaultDefinition<E> getElementType() {

        return elementType;
    }

    public int[] getDimSizes() {
        return dimSizes;
    }
}
