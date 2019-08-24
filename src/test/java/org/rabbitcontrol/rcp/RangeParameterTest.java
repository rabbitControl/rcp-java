package org.rabbitcontrol.rcp;

import org.junit.Assert;
import org.junit.Test;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.RcpTypes;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberScale;
import org.rabbitcontrol.rcp.model.parameter.RangeParameter;
import org.rabbitcontrol.rcp.model.types.Range;

import static org.rabbitcontrol.rcp.ParameterTest.writeAndParse;

public class RangeParameterTest {

    @Test
    public void testRangeParameter() throws Exception {

        final RangeParameter<Byte> param = new RangeParameter<Byte>((short)1, Byte.class);

        param.setValue(new Range<Byte>((byte)1, (byte)2));
        param.getTypeDefinition().setDefault(new Range<Byte>((byte)0, (byte)0));
        param.getRangeDefinition().getElementType().setMinimum((byte)-5);
        param.getRangeDefinition().getElementType().setMaximum((byte)5);
        param.getRangeDefinition().getElementType().setMultipleof((byte)2);
        param.getRangeDefinition().getElementType().setScale(NumberScale.LINEAR);
        param.getRangeDefinition().getElementType().setUnit("test");

        // write and parse
        final Parameter parsed_parameter = writeAndParse(param);
        parsed_parameter.dump();

        Assert.assertEquals("wrong datatype",
                            RcpTypes.Datatype.RANGE,
                            parsed_parameter.getTypeDefinition().getDatatype());

        Assert.assertEquals("wrong value",
                            param.getValue(),
                            ((RangeParameter)parsed_parameter).getValue());

        Assert.assertEquals("wrong default",
                            param.getTypeDefinition().getDefault(),
                            ((RangeParameter)parsed_parameter).getRangeDefinition()
                                                              .getDefault());

        Assert.assertEquals("wrong minimum",
                            param.getRangeDefinition().getElementType().getMinimum(),
                            ((RangeParameter)parsed_parameter).getRangeDefinition()
                                                              .getElementType()
                                                              .getMinimum());

        Assert.assertEquals("wrong maximum",
                            param.getRangeDefinition().getElementType().getMaximum(),
                            ((RangeParameter)parsed_parameter).getRangeDefinition()
                                                              .getElementType()
                                                              .getMaximum());

        Assert.assertEquals("wrong multiple",
                            param.getRangeDefinition().getElementType().getMultipleof(),
                            ((RangeParameter)parsed_parameter).getRangeDefinition()
                                                              .getElementType()
                                                              .getMultipleof());

        Assert.assertEquals("wrong scale",
                            param.getRangeDefinition().getElementType().getScale(),
                            ((RangeParameter)parsed_parameter).getRangeDefinition()
                                                              .getElementType()
                                                              .getScale());

        Assert.assertEquals("wrong unit",
                            param.getRangeDefinition().getElementType().getUnit(),
                            ((RangeParameter)parsed_parameter).getRangeDefinition()
                                                              .getElementType()
                                                              .getUnit());
    }
}
