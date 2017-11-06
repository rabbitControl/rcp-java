package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.types.DefaultDefinition;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.ParameterOptions;
import org.rabbitcontrol.rcp.model.interfaces.INumberDefinition;
import org.rabbitcontrol.rcp.model.interfaces.INumberParameter;
import org.rabbitcontrol.rcp.model.types.NumberDefinition;

public class NumberParameter<T extends Number> extends ValueParameter<T> implements
                                                                         INumberParameter<T> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    private final INumberDefinition<T> typeDefinition;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public NumberParameter(final int _id, Datatype _datatype) {

        // create correct
        super(_id, (DefaultDefinition<T>)NumberDefinition.create(_datatype));

        typeDefinition = (INumberDefinition<T>)super.getTypeDefinition();
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public INumberDefinition<T> getTypeDefinition() {

        return typeDefinition;
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final ParameterOptions option = ParameterOptions.byId(_propertyId);

        switch (option) {
            case VALUE:
                Datatype d = typeDefinition.getDatatype();

                switch (d) {

                    case INT8:
                        setValue((T)(Byte)_io.readS1());
                        return true;
                    case UINT8:
                        setValue((T)(Short)(short)_io.readU1());
                        return true;
                    case INT16:
                        setValue((T)(Short)_io.readS2be());
                        return true;
                    case UINT16:
                        setValue((T)(Integer)_io.readU2be());
                        return true;
                    case INT32:
                        setValue((T)(Integer)_io.readS4be());
                        return true;
                    case UINT32:
                        setValue((T)(Long)_io.readU4be());
                        return true;
                    case INT64:
                        setValue((T)(Long)_io.readS8be());
                        return true;
                    case UINT64:
                        setValue((T)(Long)_io.readU8be());
                        return true;
                    case FLOAT32:
                        setValue((T)(Float)_io.readF4be());
                        return true;
                    case FLOAT64:
                        setValue((T)(Double)_io.readF8be());
                        return true;
                }
                break;
        }

        return false;
    }
}
