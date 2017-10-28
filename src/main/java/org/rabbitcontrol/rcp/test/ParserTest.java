package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.model.gen.RcpTypes;
import org.rabbitcontrol.rcp.model.types.RCPTypeINT16;

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

            final RCPPacket packet = RCPParser.fromFile(file.getAbsolutePath());

            System.out.println(packet.getCmd());

            // create a packet
            final RCPPacket newP = new RCPPacket(RcpTypes.Command.UPDATE);
            newP.setTimestamp(1234);

            final RCPParameter<Short> param = new RCPParameter<>(12,
                                                                 new RCPTypeINT16((short)33,
                                                                                  (short)10,
                                                                                  (short)100));

            param.setLabel("a short value");
            param.setDescription("longer description");
            param.setOrder(-1);
            param.setValue((short)55);

            newP.setData(param);

            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

                newP.write(os);

                final byte[] the_bytes = os.toByteArray();

                System.out.println("generate packet:\n" + bytesToHex(the_bytes));

                try (final OutputStream fs = new FileOutputStream(file.getParent() + File
                        .separator + "_generated.rcp")) {

                    os.writeTo(fs);
                }
            }

        }
        catch (IOException | RCPDataErrorException | RCPUnsupportedFeatureException _e) {
            _e.printStackTrace();
        }

    }




    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("0x%02x ", b));
        }
        return builder.toString();
    }



}
