package org.rabbitcontrol.rcp;

import org.junit.Assert;
import org.junit.Test;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.RcpTypes;
import org.rabbitcontrol.rcp.model.parameter.RangeParameter;
import org.rabbitcontrol.rcp.model.types.Range;

import static org.rabbitcontrol.rcp.ParameterTest.writeAndParse;

public class RangeParameterTest {

    @Test
    public void testRangeParameter() {

        RangeParameter<Byte> param = new RangeParameter<Byte>((short)1,
                                                              RcpTypes.Datatype.INT8,
                                                              Byte.class);

        param.setValue(new Range<Byte>((byte)1, (byte)2));
        param.getTypeDefinition().setDefault(new Range<Byte>((byte)0, (byte)0));
        param.getRangeDefinition().getElementType().setMinimum((byte)-5);
        param.getRangeDefinition().getElementType().setMaximum((byte)5);

        // write and parse
        final Parameter parsed_parameter = writeAndParse(param);
        parsed_parameter.dump();

        Assert.assertEquals("wrong datatype",
                            parsed_parameter.getTypeDefinition().getDatatype(),
                            RcpTypes.Datatype.RANGE);

        Assert.assertEquals("wrong value",
                            ((RangeParameter)parsed_parameter).getValue(),
                            param.getValue());

        Assert.assertEquals("wrong default",
                            ((RangeParameter)parsed_parameter).getRangeDefinition()
                                                              .getDefault(),
                            param.getRangeDefinition().getDefault());

        Assert.assertEquals("wrong minimum",
                            ((RangeParameter)parsed_parameter).getRangeDefinition()
                                                              .getElementType()
                                                              .getMinimum(),
                            param.getRangeDefinition().getElementType().getMinimum());

        Assert.assertEquals("wrong maximum",
                            ((RangeParameter)parsed_parameter).getRangeDefinition()
                                                              .getElementType()
                                                              .getMaximum(),
                            param.getRangeDefinition().getElementType().getMaximum());
    }
}
