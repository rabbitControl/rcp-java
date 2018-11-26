package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.exceptions.*;

import java.io.*;

public class ParserTest {

    public static void main(final String[] args) {

        if (args.length == 0) {

            System.err.println("please provide a file");

            return;
        }

        File file = new File(args[0]);

        if (!file.exists()) {
            System.err.println("file does not exist: " + file.getAbsolutePath());

            return;
        }

        try {

            // init array with file length
            byte[] bytes_from_file = new byte[(int) file.length()];

            FileInputStream fis = new FileInputStream(file);
            try {
                fis.read(bytes_from_file); //read file into bytes[]
            } finally {
                fis.close();
            }

            // parse
            final Packet packet = RCPParser.fromFile(file.getAbsolutePath());

            // serialize packet
            final byte[] the_bytes = Packet.serialize(packet, true);

            // compare length
            if (bytes_from_file.length != the_bytes.length) {
                System.err.println("length missmatch");

                System.err.println("origin: " + bytesToHex(bytes_from_file));
                System.err.println("parsed: " + bytesToHex(the_bytes));

                return;
            }

            // compare byte by byte
            for (int i=0; i<the_bytes.length; i++) {

                byte fb = bytes_from_file[i];
                byte b = the_bytes[i];

                if (fb != b) {
                    System.err.println("byte missmatch at index: " + i);

                    System.err.println("origin: " + bytesToHex(bytes_from_file));
                    System.err.println("parsed: " + bytesToHex(the_bytes));
                    return;
                }
            }

            // all good
            System.out.println("bytematch!");


            return;



//            System.out.println(packet.getCmd());
//
//            // create a packet
//            final Packet newP = new Packet(RcpTypes.Command.UPDATE);
//            newP.setTimestamp(1234);
//
//
//            final INumberParameter<Short> param = RCPFactory.createNumberParameter(12,
//                                                                                         Short.class);
//            param.getTypeDefinition().setDefault((short)33);
//            param.getTypeDefinition().setMin(10);
//            param.getTypeDefinition().setMax(100);
//
//
//            param.setLabel("a short value");
//            param.setDescription("longer description");
//            param.setOrder(-1);
//            param.setValue((short)55);
//
//            newP.setData(param);
//
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            try {
//                newP.write(os);
//
//                final byte[] the_bytes = os.toByteArray();
//
//                System.out.println("generate packet:\n" + bytesToHex(the_bytes));
//
//                try {
//                    final OutputStream fs = new FileOutputStream(file.getParent() +
//                                                                 File.separator +
//                                                                 "_generated.rcp");
//                    os.writeTo(fs);
//
//                    fs.close();
//
//                } catch (FileNotFoundException _e) {
//                    _e.printStackTrace();
//                }
//            } finally {
//                os.close();
//            }
//
        }
        catch (IOException _e) {
            _e.printStackTrace();
        }
        catch (RCPDataErrorException _e) {
            _e.printStackTrace();
        }
        catch (RCPUnsupportedFeatureException _e) {
            _e.printStackTrace();
        }
        catch (RCPException _e) {
            _e.printStackTrace();
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
