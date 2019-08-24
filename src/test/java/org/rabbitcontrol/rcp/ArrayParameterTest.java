package org.rabbitcontrol.rcp;

import org.junit.Assert;
import org.junit.Test;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberScale;
import org.rabbitcontrol.rcp.model.parameter.ArrayParameter;
import org.rabbitcontrol.rcp.model.types.Int8Definition;

public class ArrayParameterTest {

    @Test
    public void testArray() throws Exception {

        final Byte[][][] array_value = { { { 1, 2 }, { 3, 4 } },
                                 { { 11, 12 }, { 13, 14 } },
                                 { { 21, 22 }, { 23, 24 } } };


        final Byte[][][] array_default = { { { 1, 1 }, { 1, 1 } },
                                         { { 11, 11 }, { 11, 11 } },
                                         { { 21, 21 }, { 21, 21 } } };

        final ArrayParameter<Byte[][][], Byte> array_parameter = ArrayParameter.create((short)1,
                                                                           Byte.class,
                                                                           3,
                                                                           2,
                                                                           2);

        array_parameter.getArrayDefinition().setDefault(array_default);

        final Int8Definition array_element_typedef = (Int8Definition)array_parameter.getArrayDefinition()
                                                                    .getElementType();
        array_element_typedef.setMin(-10);
        array_element_typedef.setMax(100);
        array_element_typedef.setMult(1);
        array_element_typedef.setScale(NumberScale.EXP2);
        array_element_typedef.setUnit("arrayunit");

        array_parameter.setValue(array_value);

        // write and parse
        final Parameter parsed_parameter = ParameterTest.writeAndParse(array_parameter);

        Assert.assertEquals("wrong datatype",
                            Datatype.ARRAY,
                            parsed_parameter.getTypeDefinition().getDatatype());

        final ArrayParameter parsed_array_param = (ArrayParameter)parsed_parameter;
        final Int8Definition parsed_element_typedef = (Int8Definition)parsed_array_param.getArrayDefinition().getElementType();

                Assert.assertArrayEquals("array value not equal",
                                 array_value,
                                 (Byte[][][])parsed_array_param.getValue());

        Assert.assertArrayEquals("array default value not equal",
                                 array_default,
                                 (Byte[][][])parsed_array_param.getArrayDefinition().getDefault());


        Assert.assertEquals("array minimum different",
                                 array_element_typedef.getMinimum(),
                                 parsed_element_typedef.getMinimum());

        Assert.assertEquals("array maximum different",
                            array_element_typedef.getMaximum(),
                            parsed_element_typedef.getMaximum());

        Assert.assertEquals("array multiple-of different",
                            array_element_typedef.getMultipleof(),
                            parsed_element_typedef.getMultipleof());

        Assert.assertEquals("array scale different",
                            array_element_typedef.getScale(),
                            parsed_element_typedef.getScale());

        Assert.assertEquals("array unit different",
                            array_element_typedef.getUnit(),
                            parsed_element_typedef.getUnit());
    }
}
