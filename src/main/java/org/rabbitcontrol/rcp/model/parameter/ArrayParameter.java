package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.ParameterOptions;
import org.rabbitcontrol.rcp.model.types.ArrayDefinition;

import java.util.List;

public class ArrayParameter<T> extends ValueParameter<List<T>> {

    private final ArrayDefinition<T> arrayDefinition;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public ArrayParameter(final int _id, final ArrayDefinition<T> _definition) {

        super(_id, _definition);

        arrayDefinition = _definition;
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final ParameterOptions option = ParameterOptions.byId(_propertyId);

        switch (option) {
            case VALUE:
                setValue(arrayDefinition.readValue(_io));
                return true;
        }

        return false;
    }

}
