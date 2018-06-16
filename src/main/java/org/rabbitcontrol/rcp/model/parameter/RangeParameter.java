package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.types.*;

public class RangeParameter<T extends Number> extends ValueParameter<Range<T>> {

    public RangeParameter(
            final short _id, final Datatype _datatype, Class<T> _cls) {

        super(_id, RangeDefinition.create(_datatype, _cls));
    }

    @Override
    public void setStringValue(final String _value) {

    }
}
