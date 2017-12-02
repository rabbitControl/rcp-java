package org.rabbitcontrol.rcp.test;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.model.parameter.EnumParameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by inx on 29/10/17.
 */
public class Bytetest {

    public static void main(String[] args) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

//        long id = Long.MAX_VALUE;//;(long)(Math.pow(2, 32) - 1);
//        final INumberParameter<Integer> parameter = ParameterFactory.createNumberParameter(12,
//                                                                                           Integer.class);


        final EnumParameter parameter = new EnumParameter(111);
        parameter.getEnumTypeDefinition().addEntry("uno");
        parameter.getEnumTypeDefinition().addEntry("dos");
        parameter.getEnumTypeDefinition().addEntry("tres");
        parameter.getEnumTypeDefinition().addEntry("quattro");
        parameter.getEnumTypeDefinition().setDefault(3);
        parameter.setValue(2);
        parameter.setLabel("a enum");
        parameter.setDescription("enum description");


        try {
            //os.write(ByteBuffer.allocate(4).putInt((int)id).array());

            parameter.write(os, true);

            byte[] the_bytes = os.toByteArray();

            System.out.println("generate packet:\n" + bytesToHex(the_bytes));




            // parse
            try {
                final Parameter param = Parameter.parse(new KaitaiStream(the_bytes));
                param.dump();
            }
            catch (RCPUnsupportedFeatureException _e) {
                _e.printStackTrace();
            }
            catch (RCPDataErrorException _e) {
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
