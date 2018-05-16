package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.types.*;

import java.lang.reflect.Array;

public class ArrayParameter<T, E> extends ValueParameter<T> {

    public static Parameter createFixed(
            final short param_id,
            final ArrayDefinitionFixed<?, ?> _arrayDefinition,
            final DefaultDefinition<?> _sub_type) {

        final Object value = Array.newInstance(RCPFactory.getClass(_sub_type.getDatatype()),
                                               _arrayDefinition.getDimSizes());

        switch (_sub_type.getDatatype()) {

            case BOOLEAN:
                return new ArrayParameter<Object, Boolean>(param_id,
                                                           (ArrayDefinitionFixed<Object,
                                                                   Boolean>)_arrayDefinition,
                                                           value);

            case INT8:
                return new ArrayParameter<Object, Byte>(param_id,
                                                        (ArrayDefinitionFixed<Object, Byte>)
                                                                _arrayDefinition,
                                                        value);
            //            case UINT8:
            //                return new ArrayDefinitionFixed<Collection, Short>(
            // (DefaultDefinition<Short>)_sub_type, _dimSizes);
            //            case INT16:
            //                return new ArrayDefinitionFixed<Collection, Short>(
            // (DefaultDefinition<Short>)_sub_type, _dimSizes);
            //            case UINT16:
            //                return new ArrayDefinitionFixed<Collection, Integer>(
            // (DefaultDefinition<Integer>)_sub_type, _dimSizes);
            //            case INT32:
            //                return new ArrayDefinitionFixed<Collection, Integer>(
            // (DefaultDefinition<Integer>)_sub_type, _dimSizes);
            //            case UINT32:
            //                return new ArrayDefinitionFixed<Collection, Long>(
            // (DefaultDefinition<Long>)_sub_type, _dimSizes);
            //            case INT64:
            //                return new ArrayDefinitionFixed<Collection, Long>(
            // (DefaultDefinition<Long>)_sub_type, _dimSizes);
            //            case UINT64:
            //                return new ArrayDefinitionFixed<Collection, Long>(
            // (DefaultDefinition<Long>)_sub_type, _dimSizes);
            //            case FLOAT32:
            //                return new ArrayDefinitionFixed<Collection, Float>(
            // (DefaultDefinition<Float>)_sub_type, _dimSizes);
            //            case FLOAT64:
            //                return new ArrayDefinitionFixed<Collection, Double>(
            // (DefaultDefinition<Double>)_sub_type, _dimSizes);
            //
            //            case STRING:
            //                return new ArrayDefinitionFixed<Collection, String>(
            // (DefaultDefinition<String>)_sub_type, _dimSizes);
            //
            //            case ENUM:
            //                return new ArrayDefinitionFixed<Collection, Long>(
            // (DefaultDefinition<Long>)_sub_type, _dimSizes);
            //
            //            case RGB:
            //                return new ArrayDefinitionFixed<Collection, Color>(
            // (DefaultDefinition<Color>)_sub_type, _dimSizes);
            //
            //            case RGBA:
            //                return new ArrayDefinitionFixed<Collection, Color>(
            // (DefaultDefinition<Color>)_sub_type, _dimSizes);
            //
            //            case FIXED_ARRAY:

            //                break;

            default:
                break;
        }

        return null;
    }

    public static <T, E> ArrayParameter<T, E> create(
            final short _id,
            final RcpTypes.Datatype _datatype,
            final T _value,
            final int... _dimSizes) throws InstantiationException, IllegalAccessException {

        // create subtype
        final DefaultDefinition<E> elemen_def = RCPFactory.createDefaultTypeDefinition(_datatype);

        return create(_id, elemen_def, _value, _dimSizes);
    }

    public static <T, E> ArrayParameter<T, E> create(
            final short _id, final Class<E> _class, final T _value, final int... _dimSizes) throws
                                                                                      InstantiationException,
                                                                                      IllegalAccessException {

        // create subtype
        final DefaultDefinition<E> elemen_def = RCPFactory.createDefaultTypeDefinition(_class);

        return create(_id, elemen_def, _value, _dimSizes);
    }

    public static <T, E> ArrayParameter<T, E> create(
            final short _id,
            final DefaultDefinition<E> _elementDefinition,
            final T _value,
            final int... _dimSizes) throws IllegalAccessException, InstantiationException {

        boolean isFixed = true;
        for (final int dim_size : _dimSizes) {
            if (dim_size <= 0) {
                // dynamic array!
                isFixed = false;
                break;
            }
        }

        if (isFixed) {

            if (_value == null) {
                throw new RuntimeException("need a value");
            }

            System.out.println("creating arrayParameter with fixed array definition");

            final T
                    defaultValue
                    = (T)Array.newInstance(RCPFactory.getClass(_elementDefinition.getDatatype()),
                                           _dimSizes);

            // create fixed array parameter
            final ArrayDefinitionFixed<T, E>
                    def
                    = new ArrayDefinitionFixed<T, E>(_elementDefinition, defaultValue, _dimSizes);

            return new ArrayParameter<T, E>(_id, def, _value);
        }
        else {

            System.out.println("creating arrayParameter with dynamic array definition");

            // create dynamic array parameter
            final ArrayDefinitionDynamic<T, E> def = new ArrayDefinitionDynamic<T, E>(
                    _elementDefinition,
                    _dimSizes);

            return new ArrayParameter<T, E>(_id, def, _value);
        }
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    private final ArrayDefinitionFixed<T, E> arrayDefinitionFixed;

    private final ArrayDefinitionDynamic<T, E> arrayDefinitionDynamic;

    //------------------------------------------------------------
    //------------------------------------------------------------
    private ArrayParameter(
            final short _id, final ArrayDefinitionFixed<T, E> _fixedDefinition, final T _value) throws
                                                                                          RuntimeException {

        super(_id, _fixedDefinition, _value);

        if ((_value == null) || !_value.getClass().isArray()) {
            throw new RuntimeException("need a value!");
        }

        arrayDefinitionFixed = _fixedDefinition;
        arrayDefinitionDynamic = null;
    }

    private ArrayParameter(
            final short _id, final ArrayDefinitionDynamic<T, E> _dynamic, final T _value) {

        super(_id, _dynamic, _value);

        arrayDefinitionFixed = null;
        arrayDefinitionDynamic = _dynamic;
    }

    @Override
    public void setStringValue(final String _value) {
        //
        System.out.println("not setting: " + _value);
    }


    private void addToString(Object _o, StringBuilder _builder) {

        if (_o.getClass().isArray()) {

            _builder.append("[ ");

            for (int i = 0; i < Array.getLength(_o); i++) {
                addToString(Array.get(_o, i), _builder);
            }

            _builder.deleteCharAt(_builder.length()-2);

            _builder.append("], ");

        } else {
            _builder.append(_o + ", ");
        }

    }


    @Override
    public String getStringValue() {
        Object v = getValue();

        StringBuilder builder = new StringBuilder();

        addToString(v, builder);
        builder.deleteCharAt(builder.length()-2);

        return builder.toString().trim();
    }
}
