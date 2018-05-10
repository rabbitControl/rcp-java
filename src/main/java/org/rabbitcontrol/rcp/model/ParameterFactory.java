package org.rabbitcontrol.rcp.model;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
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

    public static <T> ArrayParameter<T> createArrayParameter(final short _id, ArrayDefinition<T>
            _definition) {

        return new ArrayParameter<T>(_id, _definition);
    }

}
