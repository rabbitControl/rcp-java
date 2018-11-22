package org.rabbitcontrol.rcp;

import io.kaitai.struct.ByteBufferKaitaiStream;
import org.junit.Assert;
import org.junit.Test;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberboxFormat;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.model.types.Vector3;
import org.rabbitcontrol.rcp.model.widgets.NumberboxWidget;
import org.rabbitcontrol.rcp.model.widgets.TextboxWidget;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ParameterTest {

    //    public static void main(final String[] args) {
    //
    //
    //
    ////            List<List<Boolean>> b;
    ////
    ////            final List<Object> ll = new ArrayList<Object>();
    ////
    ////            ll.add(Array.newInstance(Boolean.class, 1, 2));
    ////
    ////
    ////            final Object      o  = Array.newInstance(Boolean.class, 1, 2);
    ////            final Boolean[][] oo = (Boolean[][])o;
    ////
    //////            List<Boolean[][]> lll = ll;
    //
    //
    //
    //
    //    }


    @Test
    public void testWidget() {

        final Int8Parameter ip = new Int8Parameter((short)1);

        final NumberboxWidget<Byte> widget = new NumberboxWidget<Byte>();
        widget.setPrecision(2);
        widget.setCyclic(true);
        widget.setFormat(NumberboxFormat.HEX);
        widget.setStepsize((byte)10);
        ip.setWidget(widget);

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

    @Test
    public void testString() {

        final StringParameter parameter         = new StringParameter((short)1);
        final Widget          str_widget = new TextboxWidget();
        parameter.setWidget(str_widget);

        final Parameter parsed_parameter = writeAndParse(parameter);
        Assert.assertNotEquals("could not parse parameter", parsed_parameter, null);
    }

    @Test
    public void testVec3F32() {

        Vector3Float32Parameter parameter = new Vector3Float32Parameter((short)1);
        parameter.setValue(new Vector3<Float>(1F, 2F, 3F));

        final Parameter parsed_parameter = writeAndParse(parameter);
        Assert.assertNotEquals("could not parse parameter", parsed_parameter, null);

        if (parsed_parameter instanceof Vector3Float32Parameter) {
            final Vector3<Float> vec        = ((Vector3Float32Parameter)parsed_parameter).getValue();
            final Vector3<Float> parsed_vec = ((Vector3Float32Parameter)parsed_parameter).getValue();

            System.out.println(vec.getX() + " : " + parsed_vec.getX());
            System.out.println(vec.getY() + " : " + parsed_vec.getY());
            System.out.println(vec.getZ() + " : " + parsed_vec.getZ());
        }

    }

    public static Parameter writeAndParse(final RCPWritable _writable) {

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {

            _writable.write(os, true);

            final byte[] the_bytes = os.toByteArray();
            System.out.println("bytes:\n" + bytesToHex(the_bytes));

            // parse
            try {
                return Parameter.parse(new ByteBufferKaitaiStream(the_bytes));
            }
            catch (final RCPUnsupportedFeatureException _e) {
                _e.printStackTrace();
            }
            catch (final RCPDataErrorException _e) {
                _e.printStackTrace();
            }

        }
        catch (final IOException _e) {
            _e.printStackTrace();
        }
        finally {
            try {
                os.close();
            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }
        }

        return null;
    }

    public static String bytesToHex(final byte[] in) {

        final StringBuilder builder = new StringBuilder();
        for (final byte b : in) {
            builder.append(String.format("0x%02x ", b));
        }
        return builder.toString();
    }
}
