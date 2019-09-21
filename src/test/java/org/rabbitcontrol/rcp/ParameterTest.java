package org.rabbitcontrol.rcp;

import io.kaitai.struct.ByteBufferKaitaiStream;
import org.junit.Assert;
import org.junit.Test;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberScale;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberboxFormat;
import org.rabbitcontrol.rcp.model.exceptions.*;
import org.rabbitcontrol.rcp.model.interfaces.IParameterManager;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.model.types.*;
import org.rabbitcontrol.rcp.model.widgets.NumberboxWidget;
import org.rabbitcontrol.rcp.model.widgets.TextboxWidget;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ParameterTest {

    @Test
    public void testParameter() throws Exception {

        final BooleanParameter parameter = new BooleanParameter((short)1);

        parameter.setLabel("testlabel");
        parameter.setDescription("test description to describe this parameter");
        parameter.setTags("tag,tag2,tag3");
        parameter.setOrder(10);
        parameter.setUserdata("userdata".getBytes());
        parameter.setUserid("user ID 123");
        parameter.setReadonly(true);

        //--------------------------------
        final Parameter parsed_parameter = writeAndParse(parameter);

        Assert.assertNotEquals("could not parse parameter", parsed_parameter, null);

        Assert.assertEquals("label mismatch", parameter.getLabel(), parsed_parameter.getLabel());
        Assert.assertEquals("description mismatch", parameter.getDescription(),
                            parsed_parameter.getDescription());
        Assert.assertEquals("tags mismatch", parameter.getTags(), parsed_parameter.getTags());
        Assert.assertEquals("order mismatch", parameter.getOrder(), parsed_parameter.getOrder());
        Assert.assertArrayEquals("userdata mismatch", parameter.getUserdata(),
                            parsed_parameter.getUserdata());
        Assert.assertEquals("userid mismatch", parameter.getUserid(),
                            parsed_parameter.getUserid());
        Assert.assertEquals("readonly mismatch", parameter.getReadonly(),
                            parsed_parameter.getReadonly());
    }

    @Test
    public void testBooleanParameter() throws Exception {

        final BooleanParameter parameter = new BooleanParameter((short)1);
        parameter.setValue(true);
        parameter.getTypeDefinition().setDefault(true);

        //--------------------------------
        final Parameter parsed_parameter = writeAndParse(parameter);

        Assert.assertNotEquals("could not parse parameter", parsed_parameter, null);

        Assert.assertTrue("wrong parameter type", parsed_parameter instanceof BooleanParameter);

        final BooleanParameter pp_bool = (BooleanParameter)parsed_parameter;

        Assert.assertEquals("value missmatch", parameter.getValue(), pp_bool.getValue());
        Assert.assertEquals("default missmatch", parameter.getTypeDefinition().getDefault(),
                            pp_bool.getTypeDefinition().getDefault());
    }

    @Test
    public void testInt32Parameter() throws Exception {

        final Int32Parameter parameter = new Int32Parameter((short)1);
        parameter.setValue(10499);

        parameter.setDefault(100);
        parameter.getTypeDefinition().setMinimum(-100);
        parameter.getTypeDefinition().setMaximum(100000);
        parameter.getTypeDefinition().setMultipleof(3);
        parameter.getTypeDefinition().setScale(NumberScale.EXP2);
        parameter.getTypeDefinition().setUnit("test unit");

        //--------------------------------
        final Parameter parsed_parameter = writeAndParse(parameter);

        Assert.assertNotEquals("could not parse parameter", parsed_parameter, null);

        Assert.assertTrue("wrong parameter type", parsed_parameter instanceof Int32Parameter);

        final Int32Parameter pp_bool = (Int32Parameter)parsed_parameter;

        Assert.assertEquals("value missmatch", parameter.getValue(), pp_bool.getValue());

        Assert.assertEquals("default missmatch", parameter.getDefault(), pp_bool.getDefault());
        Assert.assertEquals("minimum missmatch", parameter.getTypeDefinition().getMinimum(),
                            pp_bool.getTypeDefinition().getMinimum());
        Assert.assertEquals("maximum missmatch", parameter.getTypeDefinition().getMaximum(),
                            pp_bool.getTypeDefinition().getMaximum());
        Assert.assertEquals("multipleof missmatch", parameter.getTypeDefinition().getMultipleof(),
                            pp_bool.getTypeDefinition().getMultipleof());
        Assert.assertEquals("scale missmatch", parameter.getTypeDefinition().getScale(),
                            pp_bool.getTypeDefinition().getScale());
        Assert.assertEquals("unit missmatch", parameter.getTypeDefinition().getUnit(),
                            pp_bool.getTypeDefinition().getUnit());
    }

    @Test
    public void testVec2F32() throws Exception {

        Vector2Float32Parameter parameter = new Vector2Float32Parameter((short)1);
        parameter.setValue(new Vector2<Float>(1F, 2F));

        parameter.setDefault(new Vector2<Float>(-1F, -1F));

        //--------------------------------
        final Parameter parsed_parameter = writeAndParse(parameter);
        Assert.assertNotEquals("could not parse parameter", parsed_parameter, null);

        Assert.assertTrue("wrong parameter type", parsed_parameter instanceof Vector2Float32Parameter);

        Assert.assertEquals("value missmatch", parameter.getValue(), ((Vector2Float32Parameter)parsed_parameter).getValue());

        Assert.assertEquals("default missmatch", parameter.getDefault(),
                            ((Vector2Float32Parameter)parsed_parameter).getDefault());
    }

    @Test
    public void testVec3F32() throws Exception {

        Vector3Float32Parameter parameter = new Vector3Float32Parameter((short)1);
        parameter.setValue(new Vector3<Float>(1F, 2F, 3F));

        parameter.setDefault(new Vector3<Float>(1F, 1F, 1F));

        //--------------------------------
        final Parameter parsed_parameter = writeAndParse(parameter);
        Assert.assertNotEquals("could not parse parameter", parsed_parameter, null);

        Assert.assertTrue("wrong parameter type", parsed_parameter instanceof Vector3Float32Parameter);

        Assert.assertEquals("value missmatch", parameter.getValue(), ((Vector3Float32Parameter)parsed_parameter).getValue());

        Assert.assertEquals("default missmatch", parameter.getDefault(),
                            ((Vector3Float32Parameter)parsed_parameter).getDefault());
    }

    @Test
    public void testVec4F32() throws Exception {

        Vector4Float32Parameter parameter = new Vector4Float32Parameter((short)1);
        parameter.setValue(new Vector4<Float>(1F, 2F, 3F, 4F));

        parameter.setDefault(new Vector4<Float>(-1F, -1F, -1F, -1F));

        //--------------------------------
        final Parameter parsed_parameter = writeAndParse(parameter);
        Assert.assertNotEquals("could not parse parameter", parsed_parameter, null);

        Assert.assertTrue("wrong parameter type", parsed_parameter instanceof Vector4Float32Parameter);

        Assert.assertEquals("value missmatch", parameter.getValue(),
                            ((Vector4Float32Parameter)parsed_parameter).getValue());

        Assert.assertEquals("default missmatch", parameter.getDefault(),
                            ((Vector4Float32Parameter)parsed_parameter).getDefault());
    }

    @Test
    public void testStringParameter() throws Exception {

        final StringParameter parameter  = new StringParameter((short)1);
        final Widget          str_widget = new TextboxWidget();
        parameter.setWidget(str_widget);

        parameter.setValue("test VALUE");
        parameter.getTypeDefinition().setDefault("default value");

        //--------------------------------
        final Parameter parsed_parameter = writeAndParse(parameter);

        Assert.assertNotEquals("could not parse parameter", parsed_parameter, null);

        Assert.assertTrue("wrong parameter type", parsed_parameter instanceof StringParameter);

        Assert.assertEquals("value missmatch", parameter.getValue(),
                            ((StringParameter)parsed_parameter).getValue());

        Assert.assertEquals("default missmatch", parameter.getTypeDefinition().getDefault(),
                            ((StringParameter)parsed_parameter).getTypeDefinition().getDefault());
    }

    @Test
    public void testWidget() throws Exception {

        final Int8Parameter ip = new Int8Parameter((short)1);

        final NumberboxWidget<Byte> widget = new NumberboxWidget<Byte>();
        widget.setPrecision(2);
        widget.setCyclic(true);
        widget.setFormat(NumberboxFormat.HEX);
        widget.setStepsize((byte)10);
        ip.setWidget(widget);

        //--------------------------------
        // write and parse
        final Parameter parsed_parameter = writeAndParse(ip);

        Assert.assertNotEquals("could not parse parameter", parsed_parameter, null);

        final NumberboxWidget<Byte>
                parsed_widget
                = (NumberboxWidget<Byte>)parsed_parameter.getWidget();

        Assert.assertEquals("wrong precision", parsed_widget.getPrecision(), new Byte((byte)2));
        Assert.assertEquals("wrong cyclic", parsed_widget.isCyclic(), true);
        Assert.assertEquals("wrong format", parsed_widget.getFormat(), NumberboxFormat.HEX);
        Assert.assertEquals("wrong stepsize", parsed_widget.getStepsize(), new Byte((byte)10));

    }

    //--------------------------------
    //--------------------------------
    public static Parameter writeAndParse(final RCPWritable _writable) throws Exception {

        return writeAndParse(_writable, null);
    }

    public static Parameter writeAndParse(final RCPWritable _writable,
                                          final IParameterManager _manager) throws Exception {

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {

            _writable.write(os, true);

            final byte[] the_bytes = os.toByteArray();
            System.out.println("bytes:\n" + bytesToHex(the_bytes));

            // parse
            try {
                return Parameter.parse(new ByteBufferKaitaiStream(the_bytes), _manager);
            }
            catch (final RCPUnsupportedFeatureException _e) {
                throw new Exception(_e);
            }
            catch (final RCPDataErrorException _e) {
                throw new Exception(_e);
            }

        }
        catch (final IOException _e) {
            throw new Exception(_e);
        }
        catch (RCPException _e) {
            throw new Exception(_e);
        }
        finally {
            try {
                os.close();
            }
            catch (final IOException _e) {
                throw new Exception(_e);
            }
        }
    }

    public static String bytesToHex(final byte[] in) {

        final StringBuilder builder = new StringBuilder();
        for (final byte b : in) {
            builder.append(String.format("0x%02x ", b));
        }
        return builder.toString();
    }
}
