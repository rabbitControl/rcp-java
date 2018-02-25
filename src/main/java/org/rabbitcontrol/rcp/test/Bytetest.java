package org.rabbitcontrol.rcp.test;

import io.kaitai.struct.ByteBufferKaitaiStream;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.ParameterFactory;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.model.parameter.ArrayParameter;
import org.rabbitcontrol.rcp.model.types.ArrayDefinition;
import org.rabbitcontrol.rcp.model.types.UInt8Definition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by inx on 29/10/17.
 */
public class Bytetest {

    public static void main(final String[] args) {

        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        //        long id = Long.MAX_VALUE;//;(long)(Math.pow(2, 32) - 1);
        //        final INumberParameter<Integer> parameter = ParameterFactory
        // .createNumberParameter(12,
        //
        //    Integer.class);

        final List<Byte> l = new ArrayList<Byte>();
        l.add((byte)5);
        l.add((byte)6);
        l.add((byte)7);

        final List<List<Byte>> def = new ArrayList<List<Byte>>();
        def.add(l);
        def.add(l);
        def.add(l);

        final UInt8Definition u8def = new UInt8Definition();
        u8def.setDefault((byte)10);
        final ArrayDefinition<Byte> arrDef = new ArrayDefinition<Byte>(u8def, 3);

        final ArrayDefinition<List<Byte>> arr2Def = new ArrayDefinition<List<Byte>>(arrDef, 3);

        arr2Def.setDefault(def);

        final ArrayParameter<List<Byte>>
                parameter
                = ParameterFactory.createArrayParameter(new byte[] { 3 }, arr2Def);
        parameter.setValue(def);

        //parameter.setValue(l);

        //        final EnumParameter parameter = new EnumParameter(111);
        //        parameter.getEnumTypeDefinition().addEntry("uno");
        //        parameter.getEnumTypeDefinition().addEntry("dos");
        //        parameter.getEnumTypeDefinition().addEntry("tres");
        //        parameter.getEnumTypeDefinition().addEntry("quattro");
        //        parameter.getEnumTypeDefinition().setDefault(3);
        //        parameter.setValue(2);
        //        parameter.setLabel("a enum");
        //        parameter.setDescription("enum description");

        try {
            //os.write(ByteBuffer.allocate(4).putInt((int)id).array());

            parameter.write(os, true);

            final byte[] the_bytes = os.toByteArray();

            System.out.println("generate packet:\n" + bytesToHex(the_bytes));

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
