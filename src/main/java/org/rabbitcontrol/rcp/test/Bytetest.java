package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.ParameterFactory;
import org.rabbitcontrol.rcp.model.interfaces.INumberParameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by inx on 29/10/17.
 */
public class Bytetest {

    public static void main(String[] args) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        long id = Long.MAX_VALUE;//;(long)(Math.pow(2, 32) - 1);
        final INumberParameter<Integer> parameter = ParameterFactory.createNumberParameter(12,
                                                                                           Integer.class);


        try {
            //os.write(ByteBuffer.allocate(4).putInt((int)id).array());

            parameter.write(os);

            byte[] the_bytes = os.toByteArray();

            System.out.println("generate packet:\n" + bytesToHex(the_bytes));

        }
        catch (IOException _e) {
            _e.printStackTrace();
        }
        finally {
            try {
                os.close();
            }
            catch (IOException _e) {
                _e.printStackTrace();
            }
        }
    }


    public static String bytesToHex(byte[] in) {

        final StringBuilder builder = new StringBuilder();
        for (byte b : in) {
            builder.append(String.format("0x%02x ", b));
        }
        return builder.toString();
    }
}
