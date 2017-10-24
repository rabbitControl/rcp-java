package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPTypes.Datatype;
import org.rabbitcontrol.rcp.model.RCPTypes.TypeDefinition;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.types.*;

import java.io.IOException;
import java.io.OutputStream;

public abstract class RCPTypeDefinition<T> implements RCPWritable {

//    public static <E> RCPTypeDefinition<E> create(final RCPTypeDefinition<E> _definition) {
//
//        switch (_definition.getTypeid()) {
//
//            case BOOL:
//                return new RCPTypeBOOL();
//            // number types
//            case INT8:
//                return new RCPTypeINT8();
//            case UINT8:
//                return new RCPTypeUINT8();
//            case INT16:
//                return new RCPTypeINT16();
//            case UINT16:
//                return new RCPTypeUINT16();
//            case INT32:
//                return new RCPTypeINT32();
//            case UINT32:
//                return new RCPTypeUINT32();
//            case INT64:
//                return new RCPTypeINT64();
//            case UINT64:
//                return new RCPTypeUINT64();
//            case FLOAT32:
//                return new RCPTypeFLOAT32();
//            case FLOAT64:
//                return new RCPTypeFLOAT64();
//
//            // string
//            case TSTR:
//                break;
//            case SSTR:
//                break;
//            case LSTR:
//                return new RCPTypeSTRING();
//        }
//
//        return new RCPTypeSTRING();
//    }

    //------------------------------------------------------------
    public static RCPTypeDefinition<?> parse(final KaitaiStream _io) throws RCPDataErrorException {

        // read mandatory type
        final Datatype typeid = Datatype.byId(_io.readU1());

        if (typeid == null) {
            throw new RCPDataErrorException();
        }

        RCPTypeDefinition<?> type = null;

        switch (typeid) {

            case BOOL:
                type = RCPTypeBOOL.parse(_io);
                break;

            // number types
            case INT8:
                type = RCPTypeINT8.parse(_io);
                break;
            case UINT8:
                type = RCPTypeUINT8.parse(_io);
                break;
            case INT16:
                type = RCPTypeINT16.parse(_io);
                break;
            case UINT16:
                type = RCPTypeUINT16.parse(_io);
                break;
            case INT32:
                type = RCPTypeINT32.parse(_io);
                break;
            case UINT32:
                type = RCPTypeUINT32.parse(_io);
                break;
            case INT64:
                type = RCPTypeINT64.parse(_io);
                break;
            case UINT64:
                type = RCPTypeUINT64.parse(_io);
                break;
            case FLOAT32:
                type = RCPTypeFLOAT32.parse(_io);
                break;
            case FLOAT64:
                type = RCPTypeFLOAT64.parse(_io);
                break;

            // string
            case TSTR:
                break;
            case SSTR:
                break;
            case LSTR:
                type = RCPTypeSTRING.parse(_io);
                break;
        }

        if (type == null) {
            throw new RCPDataErrorException();
        }

        return type;
    }

    //------------------------------------------------------------
    private final Datatype typeid;

    private T defaultValue;

    //------------------------------------------------------------
    public RCPTypeDefinition(final RCPTypes.Datatype _typeid) {

        typeid = _typeid;
    }

    public abstract RCPTypeDefinition<T> cloneEmpty();


    @Override
    public void write(final OutputStream _outputStream) throws IOException {

        _outputStream.write((int)typeid.id());

        if (defaultValue != null) {
            _outputStream.write((int)TypeDefinition.DEFAULTVALUE.id());
            writeValue(defaultValue, _outputStream);
        }
    }

    public abstract void writeValue(final T _value, final OutputStream _outputStream) throws
                                                                                      IOException;

//    public abstract void update(RCPTypeDefinition<?> _othertype);

    //------------------------------------------------------------
    public Datatype getTypeid() {

        return typeid;
    }

    public T getDefaultValue() {

        if (defaultValue == null) {
            return getTypeDefault();
        }

        return defaultValue;
    }

    public abstract T getTypeDefault();


    public void setDefaultValue(final T _defaultValue) {

        defaultValue = _defaultValue;
    }

}
