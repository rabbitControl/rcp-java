package org.rabbitcontrol.rcp.model;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.model.types.*;

import java.awt.*;

public class RCPFactory {

    public static IParameter createParameter(final short _id, final Datatype _datatype) {

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

            case ARRAY:

        }

        return null;
    }

    //    public static <T extends Collection, E> ArrayParameter<T, E> createArrayParameter(final
    // short
    //
    //  _id, final
    //    Datatype
    //            _datatype) {
    //
    //        return new ArrayParameter<T>(_id, _datatype, 1);
    //    }

    public static <T extends Number> RangeParameter<T> createRangeParameter(
            final short _id, final Datatype element_type) {

        switch (element_type) {

            case INT8:
                return (RangeParameter<T>)new RangeParameter<Byte>(_id, element_type, Byte.class);
            case UINT8:
                return (RangeParameter<T>)new RangeParameter<Byte>(_id, element_type, Byte.class);
            case INT16:
                return (RangeParameter<T>)new RangeParameter<Short>(_id, element_type, Short.class);
            case UINT16:
                return (RangeParameter<T>)new RangeParameter<Short>(_id, element_type, Short.class);
            case INT32:
                return (RangeParameter<T>)new RangeParameter<Integer>(_id,
                                                                      element_type,
                                                                      Integer.class);
            case UINT32:
                return (RangeParameter<T>)new RangeParameter<Integer>(_id,
                                                                      element_type,
                                                                      Integer.class);
            case INT64:
                return (RangeParameter<T>)new RangeParameter<Long>(_id, element_type, Long.class);
            case UINT64:
                return (RangeParameter<T>)new RangeParameter<Long>(_id, element_type, Long.class);
            case FLOAT32:
                return (RangeParameter<T>)new RangeParameter<Float>(_id, element_type, Float.class);
            case FLOAT64:
                return (RangeParameter<T>)new RangeParameter<Double>(_id,
                                                                     element_type,
                                                                     Double.class);

        }

        return null;

    }

    public static <T> DefaultDefinition createDefaultTypeDefinition(final Class<T> _class) {

        if (_class == null) {
            return null;
        }

        if (_class.equals(Boolean.class)) {
            return new BooleanDefinition();
        }
        else if (_class.equals(Byte.class)) {
            return new Int8Definition();
        }
        else if (_class.equals(Short.class)) {
            return new Int16Definition();
        }
        else if (_class.equals(Integer.class)) {
            return new Int32Definition();
        }
        else if (_class.equals(Long.class)) {
            return new Int64Definition();
        }
        else if (_class.equals(Float.class)) {
            return new Float32Definition();
        }
        else if (_class.equals(Double.class)) {
            return new Float64Definition();
        }
        else if (_class.equals(String.class)) {
            return new StringDefinition();
        }
        else if (_class.equals(Color.class)) {
            return new RGBADefinition();
        }
        else if (_class.equals(Enum.class)) {
            return new EnumDefinition();
        }

        return null;
    }

    public static DefaultDefinition createDefaultTypeDefinition(final Datatype _datatype) {

        switch (_datatype) {
            case BOOLEAN:
                return new BooleanDefinition();

            case INT8:
                return new Int8Definition();
            case UINT8:
                return new UInt8Definition();
            case INT16:
                return new Int16Definition();
            case UINT16:
                return new UInt16Definition();
            case INT32:
                return new Int32Definition();
            case UINT32:
                return new UInt32Definition();
            case INT64:
                return new Int64Definition();
            case UINT64:
                return new UInt64Definition();
            case FLOAT32:
                return new Float32Definition();
            case FLOAT64:
                return new Float64Definition();

            case STRING:
                return new StringDefinition();

            case RGB:
                return new RGBDefinition();

            case RGBA:
                return new RGBADefinition();

            case ENUM:
                return new EnumDefinition();

            case GROUP:
                //                return new GroupDefinition();

            case ARRAY:

        }

        return null;
    }

    public static Class<?> getClass(final Datatype _datatype) {

        switch (_datatype) {
            case BOOLEAN:
                return Boolean.class;

            case INT8:
                return Byte.class;
            case UINT8:
                return Byte.class;
            case INT16:
                return Short.class;
            case UINT16:
                return Short.class;
            case INT32:
                return Integer.class;
            case UINT32:
                return Integer.class;
            case INT64:
                return Long.class;
            case UINT64:
                return Long.class;
            case FLOAT32:
                return Float.class;
            case FLOAT64:
                return Double.class;

            case STRING:
                return String.class;

            case RGB:
                return Color.class;

            case RGBA:
                return Color.class;

            case ENUM:
                return String.class;

            case GROUP:
                //                return new GroupDefinition();

            case ARRAY:

        }

        return null;
    }

}
