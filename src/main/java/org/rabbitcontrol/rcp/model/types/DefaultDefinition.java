package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.TypeDefinition;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.interfaces.IDefaultDefinition;

import java.io.IOException;
import java.io.OutputStream;

public abstract class DefaultDefinition<T> extends TypeDefinition implements IDefaultDefinition<T> {

    public static DefaultDefinition<?> parse(final KaitaiStream _io) throws RCPDataErrorException {

        // read typedef...
        final Datatype type = Datatype.byId(_io.readU1());

        DefaultDefinition<?> definition = null;

        switch (type) {
            case BOOLEAN:
                definition = new BooleanDefinition();
                break;

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
                definition =  NumberDefinition.create(type);
                break;

            case RANGE:
                // parse element type
//                DefaultDefinition<?> element_definition = parse(_io);
//                definition = new RangeDefinition(element_definition);
                break;

            case STRING:
                definition =  new StringDefinition();
                break;

            case RGB:
                definition = new RGBDefinition();
                break;

            case RGBA:
                definition = new RGBADefinition();
                break;

            case ENUM:
                definition = new EnumDefinition();
                break;

            case GROUP:
                // no default definition...

            case ARRAY:
                definition = ArrayDefinition.parse(_io);
                break;

            case LIST:
                // dont handle these...?
                // TODO: handle these...
                // read special needs before...
                break;


        }

        if (definition != null) {
            definition.parseOptions(_io);
            return definition;
        }


        throw new RCPDataErrorException();
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    // optional
    protected T defaultValue;
    protected boolean defaultValueChanged;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public DefaultDefinition(final Datatype _datatype) {

        super(_datatype);
    }

    public DefaultDefinition(final Datatype _datatype, final T _defaultValue) {

        super(_datatype);

        defaultValue = _defaultValue;
    }

    public abstract void writeValue(final T _value, final OutputStream _outputStream) throws
                                                                                      IOException;

    public abstract T readValue(final KaitaiStream _io);

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public T getDefault() {

        return defaultValue;
    }

    @Override
    public void setDefault(final T _default) {

        if ((defaultValue == _default) || ((defaultValue != null) && defaultValue.equals(_default))) {
            return;
        }

        defaultValue = _default;
        defaultValueChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }
}
