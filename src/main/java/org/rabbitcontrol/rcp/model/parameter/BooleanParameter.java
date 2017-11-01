package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.gen.RcpTypes;
import org.rabbitcontrol.rcp.model.types.BooleanDefinition;

public class BooleanParameter extends ValueParameter<Boolean> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public BooleanParameter(final int _id) {

        super(_id, new BooleanDefinition());
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final RcpTypes.Parameter property = RcpTypes.Parameter.byId(_propertyId);

        switch (property) {
            case VALUE:
                setValue(_io.readS1() != 0);
                return true;
        }

        return false;
    }

}
