package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.types.*;

import java.util.Collection;

public class ArrayParameter<T extends Collection, E> extends ValueParameter<T> {


    public static Parameter createFixed(
            short param_id,
            final ArrayDefinitionFixed<?, ?> _arrayDefinition,
            DefaultDefinition<?> _sub_type) {

        switch (_sub_type.getDatatype()) {

            case BOOLEAN:
                return new ArrayParameter<Collection, Boolean>(param_id,
                                                               (ArrayDefinitionFixed<Collection,
                                                                       Boolean>)_arrayDefinition);

            case INT8:
                return new ArrayParameter<Collection, Byte>(param_id,
                                                               (ArrayDefinitionFixed<Collection,
                                                                       Byte>)_arrayDefinition);
//            case UINT8:
//                return new ArrayDefinitionFixed<Collection, Short>((DefaultDefinition<Short>)_sub_type, _dimSizes);
//            case INT16:
//                return new ArrayDefinitionFixed<Collection, Short>((DefaultDefinition<Short>)_sub_type, _dimSizes);
//            case UINT16:
//                return new ArrayDefinitionFixed<Collection, Integer>((DefaultDefinition<Integer>)_sub_type, _dimSizes);
//            case INT32:
//                return new ArrayDefinitionFixed<Collection, Integer>((DefaultDefinition<Integer>)_sub_type, _dimSizes);
//            case UINT32:
//                return new ArrayDefinitionFixed<Collection, Long>((DefaultDefinition<Long>)_sub_type, _dimSizes);
//            case INT64:
//                return new ArrayDefinitionFixed<Collection, Long>((DefaultDefinition<Long>)_sub_type, _dimSizes);
//            case UINT64:
//                return new ArrayDefinitionFixed<Collection, Long>((DefaultDefinition<Long>)_sub_type, _dimSizes);
//            case FLOAT32:
//                return new ArrayDefinitionFixed<Collection, Float>((DefaultDefinition<Float>)_sub_type, _dimSizes);
//            case FLOAT64:
//                return new ArrayDefinitionFixed<Collection, Double>((DefaultDefinition<Double>)_sub_type, _dimSizes);
//
//            case STRING:
//                return new ArrayDefinitionFixed<Collection, String>((DefaultDefinition<String>)_sub_type, _dimSizes);
//
//            case ENUM:
//                return new ArrayDefinitionFixed<Collection, Long>((DefaultDefinition<Long>)_sub_type, _dimSizes);
//
//            case RGB:
//                return new ArrayDefinitionFixed<Collection, Color>((DefaultDefinition<Color>)_sub_type, _dimSizes);
//
//            case RGBA:
//                return new ArrayDefinitionFixed<Collection, Color>((DefaultDefinition<Color>)_sub_type, _dimSizes);
//
//            case FIXED_ARRAY:

//                break;

            default:
                break;
        }

        return null;
    }


    public static <T extends Collection, E> ArrayParameter<T, E> create(
            final short _id, final RcpTypes.Datatype _datatype, final int... _dimSizes) {

        // create subtype
        final DefaultDefinition<E> elemen_def = RCPFactory.createDefaultTypeDefinition(_datatype);

        return create(_id, elemen_def, _dimSizes);
    }

    public static <T extends Collection, E> ArrayParameter<T, E> create(
            final short _id, final Class<E> _class, final int... _dimSizes) {

        // create subtype
        final DefaultDefinition<E> elemen_def = RCPFactory.createDefaultTypeDefinition(_class);

        return create(_id, elemen_def, _dimSizes);
    }

    public static <T extends Collection, E> ArrayParameter<T, E> create(
            final short _id,
            final DefaultDefinition<E> _elementDefinition,
            final int... _dimSizes) {

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

            // create fixed array parameter
            final ArrayDefinitionFixed<T, E> def = new ArrayDefinitionFixed<T, E>(_elementDefinition,
                                                                                  _dimSizes);

            return new ArrayParameter<T, E>(_id, def);
        }
        else {

            System.out.println("creating arrayParameter with dynamic array definition");

            // create dynamic array parameter
            final ArrayDefinitionDynamic<T, E> def = new ArrayDefinitionDynamic<T, E>(_elementDefinition,
                                                                                      _dimSizes);

            return new ArrayParameter<T, E>(_id, def);
        }
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    private final ArrayDefinitionFixed<T, E> arrayDefinitionFixed;

    private final ArrayDefinitionDynamic<T, E> arrayDefinitionDynamic;

    //------------------------------------------------------------
    //------------------------------------------------------------
    private ArrayParameter(
            final short _id, final ArrayDefinitionFixed<T, E> _fixedDefinition) {

        super(_id, _fixedDefinition);

        arrayDefinitionFixed = _fixedDefinition;
        arrayDefinitionDynamic = null;
    }

    private ArrayParameter(
            final short _id, final ArrayDefinitionDynamic<T, E> _dynamic) {

        super(_id, _dynamic);

        arrayDefinitionFixed = null;
        arrayDefinitionDynamic = _dynamic;
    }

    @Override
    public void setStringValue(final String _value) {
        //
    }
}
