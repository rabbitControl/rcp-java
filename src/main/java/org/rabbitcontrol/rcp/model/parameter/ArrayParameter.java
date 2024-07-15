package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.types.*;

import java.awt.*;
import java.lang.reflect.Array;
import java.net.Inet4Address;

public class ArrayParameter<T, E> extends ValueParameter<T> {

    public static Parameter createFixed(
            final short param_id,
            final ArrayDefinition<?, ?> _arrayDefinition,
            final DefaultDefinition<?> _sub_type) {

        switch (_sub_type.getDatatype()) {

            case BOOLEAN:
                return new ArrayParameter<Object, Boolean>(param_id,
                                                           (ArrayDefinition<Object, Boolean>)
                                                                   _arrayDefinition);

            case INT8:
                return new ArrayParameter<Object, Byte>(param_id,
                                                        (ArrayDefinition<Object, Byte>)
                                                                _arrayDefinition);
            case UINT8:
                return new ArrayParameter<Object,Short>(param_id,
                                                              (ArrayDefinition<Object, Short>)
                                                                      _arrayDefinition);
            case INT16:
                return new ArrayParameter<Object,Short>(param_id,
                                                        (ArrayDefinition<Object, Short>)
                                                                _arrayDefinition);
            case UINT16:
                return new ArrayParameter<Object,Integer>(param_id,
                                                          (ArrayDefinition<Object, Integer>)
                                                                  _arrayDefinition);
            case INT32:
                return new ArrayParameter<Object,Integer>(param_id,
                                                          (ArrayDefinition<Object, Integer>)
                                                                  _arrayDefinition);
            case UINT32:
                return new ArrayParameter<Object,Long>(param_id,
                                                       (ArrayDefinition<Object, Long>)
                                                               _arrayDefinition);
            case INT64:
                return new ArrayParameter<Object,Long>(param_id,
                                                       (ArrayDefinition<Object, Long>)
                                                               _arrayDefinition);
            case UINT64:
                return new ArrayParameter<Object,Long>(param_id,
                                                       (ArrayDefinition<Object, Long>)
                                                               _arrayDefinition);
            case FLOAT32:
                return new ArrayParameter<Object,Float>(param_id,
                                                        (ArrayDefinition<Object, Float>)
                                                                _arrayDefinition);
            case FLOAT64:
                return new ArrayParameter<Object,Double>(param_id,
                                                         (ArrayDefinition<Object, Double>)
                                                                 _arrayDefinition);

            case STRING:
                return new ArrayParameter<Object,String>(param_id,
                                                         (ArrayDefinition<Object, String>)
                                                                 _arrayDefinition);

            case ENUM:
                return new ArrayParameter<Object,String>(param_id,
                                                       (ArrayDefinition<Object, String>)
                                                               _arrayDefinition);

            case RGB:
                return new ArrayParameter<Object,Color>(param_id,
                                                        (ArrayDefinition<Object, Color>)
                                                                _arrayDefinition);

            case RGBA:
                return new ArrayParameter<Object,Color>(param_id,
                                                        (ArrayDefinition<Object, Color>)
                                                                _arrayDefinition);

            case IPV4:
                return new ArrayParameter<Object, Inet4Address>(param_id,
                        (ArrayDefinition<Object, Inet4Address>)
                                _arrayDefinition);

            case ARRAY:
                // nonono
                break;

            case LIST:
                //?
                break;

            default:
                break;
        }

        return null;
    }

    public static <T, E> ArrayParameter<T, E> create(
            final short _id,
            final RcpTypes.Datatype _datatype,
            final int... _dimSizes) throws InstantiationException, IllegalAccessException {

        // create subtype
        final DefaultDefinition<E> elemen_def = RCPFactory.createDefaultTypeDefinition(_datatype);

        return create(_id, elemen_def, _dimSizes);
    }

    public static <T, E> ArrayParameter<T, E> create(
            final short _id, final Class<E> _class, final int... _dimSizes) throws
                                                                                            InstantiationException,
                                                                                            IllegalAccessException {

        // create subtype
        final DefaultDefinition<E> elemen_def = RCPFactory.createDefaultTypeDefinition(_class);

        return create(_id, elemen_def, _dimSizes);
    }

    public static <T, E> ArrayParameter<T, E> create(
            final short _id,
            final DefaultDefinition<E> _elementDefinition,
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

            System.out.println("creating arrayParameter with fixed array definition");

//            final T
//                    defaultValue
//                    = (T)Array.newInstance(RCPFactory.getClass(_elementDefinition.getDatatype()),
//                                           _dimSizes);

            // create fixed array parameter
            final ArrayDefinition<T, E>
                    def
                    = new ArrayDefinition<T, E>(_elementDefinition, null, _dimSizes);

            return new ArrayParameter<T, E>(_id, def);
        }
        else {

            System.out.println("creating arrayParameter with dynamic array definition");

            // create dynamic array parameter
            final ListDefinition<T, E> def = new ListDefinition<T, E>(
                    _elementDefinition,
                    _dimSizes);

            return new ArrayParameter<T, E>(_id, def);
        }
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    private final ArrayDefinition<T, E> arrayDefinition;

    private final ListDefinition<T, E> listDefinition;

    //------------------------------------------------------------
    //------------------------------------------------------------
    private ArrayParameter(
            final short _id,
            final ArrayDefinition<T, E> _fixedDefinition) throws RuntimeException {

        super(_id, _fixedDefinition);

        arrayDefinition = _fixedDefinition;
        listDefinition = null;
    }

    private ArrayParameter(
            final short _id, final ListDefinition<T, E> _dynamic) {

        super(_id, _dynamic);

        arrayDefinition = null;
        listDefinition = _dynamic;
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

            _builder.deleteCharAt(_builder.length() - 2);

            _builder.append("], ");

        }
        else {
            _builder.append(_o + ", ");
        }

    }

    @Override
    public String getStringValue() {

        Object v = getValue();

        StringBuilder builder = new StringBuilder();

        addToString(v, builder);
        builder.deleteCharAt(builder.length() - 2);

        return builder.toString().trim();
    }

    public ArrayDefinition<T, E> getArrayDefinition() {
        return arrayDefinition;
    }

    public ListDefinition<T, E> getListDefinition() {
        return listDefinition;
    }

    public DefaultDefinition<E> getElementType() {

        if (arrayDefinition != null) {
            return arrayDefinition.getElementType();
        }

        return listDefinition.getElementType();
    }

}
