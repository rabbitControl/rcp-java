package org.rabbitcontrol.rcp.model;

import org.rabbitcontrol.rcp.model.exceptions.TypeMissmatch;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.interfaces.*;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.model.types.ArrayDefinition;

public class ParameterFactory {

    public static IParameter createParameter(final int _id, Datatype _datatype) {

        switch (_datatype) {
            case BOOLEAN:
                return new BooleanParameter(_id);

            case INT8:
            case UINT8:
            case INT16:
            case UINT16:
            case INT32:
            case UINT32:
            case INT64:
            case UINT64:
            case FLOAT32:
            case FLOAT64:
                return new NumberParameter(_id, _datatype);

            case STRING:
                return new StringParameter(_id);

            case RGB:
                return new RGBParameter(_id);

            case RGBA:
                return new RGBAParameter(_id);

            case ENUM:
                return new EnumParameter(_id);

            case GROUP:
                return new GroupParameter(_id);

            case FIXED_ARRAY:


        }

        return null;
    }

    public static <T extends Number> INumberParameter<T> createNumberParameter(
            int _id, Datatype _datatype, Class<T> _class) throws TypeMissmatch {

        switch (_datatype) {
            case FLOAT32:
                if (_class.equals(Float.class)) {
                    return new NumberParameter<T>(_id, _datatype);
                }

                // type missmatch
                throw new TypeMissmatch();
        }

        throw new RuntimeException("type not implemented");
    }

    public static <T extends Number> INumberParameter<T> createNumberParameter(
            int _id, Class<T> _class) {

        // best guess from type
        // not dealing with signed/unsigned

        if (_class.equals(Byte.class)) {
            return new NumberParameter<T>(_id, Datatype.INT8);
        }
        else if (_class.equals(Short.class)) {
            return new NumberParameter<T>(_id, Datatype.INT16);
        }
        else if (_class.equals(Integer.class)) {
            return new NumberParameter<T>(_id, Datatype.INT32);
        }
        else if (_class.equals(Long.class)) {
            return new NumberParameter<T>(_id, Datatype.INT64);
        }
        else if (_class.equals(Float.class)) {
            return new NumberParameter<T>(_id, Datatype.FLOAT32);
        }
        else if (_class.equals(Double.class)) {
            return new NumberParameter<T>(_id, Datatype.FLOAT64);
        }

        return null;
    }

    public static BooleanParameter createBooleanParameter(final int _id) {

        return new BooleanParameter(_id);
    }

    public static StringParameter createStringParameter(final int _id) {

        return new StringParameter(_id);
    }

    public static <T> ArrayParameter<T> createArrayParameter(final int _id, ArrayDefinition<T>
            _definition) {

        return new ArrayParameter<T>(_id, _definition);
    }

}
