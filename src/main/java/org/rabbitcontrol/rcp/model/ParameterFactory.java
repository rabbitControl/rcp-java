package org.rabbitcontrol.rcp.model;

import org.rabbitcontrol.rcp.model.exceptions.TypeMissmatch;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.interfaces.INumberParameter;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.model.types.ArrayDefinition;

public class ParameterFactory {

    public static IParameter createParameter(final short _id, Datatype _datatype) {

        switch (_datatype) {
            case BOOLEAN:
                return new BooleanParameter(_id);
            case INT8:
                return new Int8Parameter(_id);
            case UINT8:
                return new UInt8Parameter(_id);
            case INT16:
                return new Int16Parameter(_id);
            case UINT16:
                return new UInt16Parameter(_id);
            case INT32:
                return new Int32Parameter(_id);
            case UINT32:
                return new UInt32Parameter(_id);
            case INT64:
                return new Int64Parameter(_id);
            case UINT64:
                return new UInt64Parameter(_id);
            case FLOAT32:
                return new Float32Parameter(_id);
            case FLOAT64:
                return new Float64Parameter(_id);

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
            short _id, Datatype _datatype, Class<T> _class) throws TypeMissmatch {

        switch (_datatype) {
            // TODO: implement

            case UINT8:
                break;

            case INT8:
                break;

            case UINT16:
                break;

            case INT16:
                break;

            case UINT32:
                break;
            case INT32:
                break;

            case UINT64:
                break;

            case INT64:
                break;


            case FLOAT32:
                if (_class.equals(Float.class)) {
                    return new NumberParameter<T>(_id, _datatype);
                }

                // type missmatch
                throw new TypeMissmatch();

            case FLOAT64:
                break;
        }

        throw new RuntimeException("type not implemented");
    }

    public static <T extends Number> INumberParameter<T> createNumberParameter(
            short _id, Class<T> _class) {

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

    public static NumberParameter<Byte> createUInt8Parameter(final short _id) {
        return new NumberParameter<Byte>(_id, Datatype.UINT8);
    }

    public static NumberParameter<Byte> createInt8Parameter(final short _id) {
        return new NumberParameter<Byte>(_id, Datatype.INT8);
    }


    public static BooleanParameter createBooleanParameter(final short _id) {

        return new BooleanParameter(_id);
    }

    public static StringParameter createStringParameter(final short _id) {

        return new StringParameter(_id);
    }

    public static <T> ArrayParameter<T> createArrayParameter(final short _id, ArrayDefinition<T>
            _definition) {

        return new ArrayParameter<T>(_id, _definition);
    }

}
