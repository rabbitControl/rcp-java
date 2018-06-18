package org.rabbitcontrol.rcp;

import org.junit.Assert;
import org.junit.Test;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.RcpTypes;
import org.rabbitcontrol.rcp.model.parameter.ArrayParameter;

public class ArrayParameterTest {

    @Test
    public void testArray() throws IllegalAccessException, InstantiationException {

        final Byte[][][] bla = { { { 1, 2 }, { 3, 4 } },
                                 { { 11, 12 }, { 13, 14 } },
                                 { { 21, 22 }, { 23, 24 } } };

        final ArrayParameter<Byte[][][], Byte> arr = ArrayParameter.create((short)1,
                                                                           Byte.class,
                                                                           3,
                                                                           2,
                                                                           2);
        arr.setValue(bla);

        // write and parse
        final Parameter parsed_parameter = ParameterTest.writeAndParse(arr);

        Assert.assertEquals("wrong datatype",
                            parsed_parameter.getTypeDefinition().getDatatype(),
                            RcpTypes.Datatype.ARRAY);

        final ArrayParameter p = (ArrayParameter)parsed_parameter;

        Assert.assertArrayEquals("array not equal", bla, (Byte[][][])p.getValue());
    }
}
