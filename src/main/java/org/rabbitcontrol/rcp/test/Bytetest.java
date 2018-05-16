package org.rabbitcontrol.rcp.test;

import io.kaitai.struct.ByteBufferKaitaiStream;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.RcpTypes;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.model.parameter.ArrayParameter;
import org.rabbitcontrol.rcp.model.types.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by inx on 29/10/17.
 */
public class Bytetest {

    public static void main(final String[] args) throws
                                                 IllegalAccessException,
                                                 InstantiationException {

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {


            List<List<Boolean>> b;

            final List<Object> ll = new ArrayList<Object>();

            ll.add(Array.newInstance(Boolean.class, 1, 2));


            final Object      o  = Array.newInstance(Boolean.class, 1, 2);
            final Boolean[][] oo = (Boolean[][])o;

//            List<Boolean[][]> lll = ll;


            final Byte[][][] bla = { { { 1, 2 }, { 3, 4 } },
                                     { { 11, 12 }, { 13, 14 } },
                                     { { 21, 22 }, { 23, 24 } } };


            final ArrayParameter<Byte[][][], Byte> arr = ArrayParameter.create(
                    (short)1,
                    Byte.class,
                    bla,
                    3, 2, 2);


            arr.write(os, true);

            final byte[] the_bytes = os.toByteArray();
            System.out.println("bytes:\n" + bytesToHex(the_bytes));

            // parse
            try {
                final Parameter param = Parameter.parse(new ByteBufferKaitaiStream(the_bytes));
                param.dump();

                if (param.getTypeDefinition().getDatatype() == RcpTypes.Datatype.FIXED_ARRAY) {

                    final ArrayParameter p     = (ArrayParameter)param;
                    final Byte[][][]     blubb = (Byte[][][])p.getValue();
                    System.out.println("same: " + Arrays.deepEquals(bla, blubb));
                }
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
    }

    public static String bytesToHex(final byte[] in) {

        final StringBuilder builder = new StringBuilder();
        for (final byte b : in) {
            builder.append(String.format("0x%02x ", b));
        }
        return builder.toString();
    }
}
