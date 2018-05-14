package org.rabbitcontrol.rcp.test;

import io.kaitai.struct.ByteBufferKaitaiStream;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.model.parameter.ArrayParameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by inx on 29/10/17.
 */
public class Bytetest {

    public static void main(final String[] args) {

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {

            ArrayParameter<List<List<Byte>>, Byte> arr = ArrayParameter.create((short)1, Byte
                    .class, 3, 3);

            List<List<Byte>> v = new ArrayList<List<Byte>>();
            v.add(Arrays.asList((byte)1, (byte)2, (byte)3));
            v.add(Arrays.asList((byte)4, (byte)5, (byte)6));
            v.add(Arrays.asList((byte)7, (byte)8, (byte)9));

            arr.setValue(v);

            arr.write(os, true);

            final byte[] the_bytes = os.toByteArray();
            System.out.println("bytes:\n" + bytesToHex(the_bytes));

            // parse
            try {
                final Parameter param = Parameter.parse(new ByteBufferKaitaiStream(the_bytes));
                param.dump();
            }
            catch (final RCPUnsupportedFeatureException _e) {
                _e.printStackTrace();
            }
            catch (final RCPDataErrorException _e) {
                _e.printStackTrace();
            }

        }
        catch (IOException _e) {
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
