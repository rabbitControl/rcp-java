package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.LongString;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.ParameterOptions;
import org.rabbitcontrol.rcp.model.types.StringDefinition;

public class StringParameter extends ValueParameter<String> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public StringParameter(final int _id) {

        super(_id, new StringDefinition());
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final ParameterOptions option = ParameterOptions.byId(_propertyId);

        switch (option) {
            case VALUE:
                final LongString longString = new LongString(_io);
                setValue(longString.data());
                return true;
        }

        return false;
    }
}
