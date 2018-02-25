package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.interfaces.INumberDefinition;
import org.rabbitcontrol.rcp.model.interfaces.INumberParameter;
import org.rabbitcontrol.rcp.model.types.DefaultDefinition;
import org.rabbitcontrol.rcp.model.types.NumberDefinition;

import java.nio.ByteBuffer;

public class NumberParameter<T extends Number> extends ValueParameter<T> implements
                                                                         INumberParameter<T> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    private final INumberDefinition<T> typeDefinition;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public NumberParameter(final ByteBuffer _id, Datatype _datatype) {

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

}
