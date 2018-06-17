package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.types.Range;
import org.rabbitcontrol.rcp.model.types.RangeDefinition;

public class RangeParameter<T extends Number> extends ValueParameter<Range<T>> {



    final RangeDefinition<T> rangeDefinition;

    public RangeParameter(
            final short _id, final Datatype _datatype, Class<T> _cls) {

        super(_id, RangeDefinition.create(_datatype, _cls));

        rangeDefinition = (RangeDefinition<T>)getTypeDefinition();
    }

    @Override
    protected void parseTypeOptions(final KaitaiStream _io) throws RCPDataErrorException {

        rangeDefinition.getElementType().parseOptions(_io);

        super.parseTypeOptions(_io);
    }

    public RangeDefinition<T> getRangeDefinition() {
        return rangeDefinition;
    }

    @Override
    public void setStringValue(final String _value) {

    }
}
