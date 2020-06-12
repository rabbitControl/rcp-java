package org.rabbitcontrol.rcp;

import io.kaitai.struct.ByteBufferKaitaiStream;
import org.junit.Assert;
import org.junit.Test;
import org.rabbitcontrol.rcp.model.*;
import org.rabbitcontrol.rcp.model.RcpTypes.Command;
import org.rabbitcontrol.rcp.model.exceptions.*;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.StringParameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.rabbitcontrol.rcp.RCP.bytesToHex;

public class PacketTest {

    @Test
    public void testInitPacket() throws Exception {

        final Packet packet = new Packet(Command.INITIALIZE);
        packet.setTimestamp(1234);

        //--------------------------------
        final Packet parsed_packet = writeAndParse(packet);

        Assert.assertNotNull("could not parse packet", parsed_packet);

        Assert.assertEquals("command missmatch", packet.getCmd(), parsed_packet.getCmd());
        Assert.assertEquals("timestamp missmatch",
                            packet.getTimestamp(),
                            parsed_packet.getTimestamp());
    }

    @Test
    public void testInfoPacket() throws Exception {

        final Packet packet = new Packet(Command.INFO);
        packet.setTimestamp(1234);
        packet.setData(new InfoData("1.2.3", "test"));

        //--------------------------------
        final Packet parsed_packet = writeAndParse(packet);

        Assert.assertNotNull("could not parse packet", parsed_packet);

        Assert.assertEquals("command missmatch", packet.getCmd(), parsed_packet.getCmd());
        Assert.assertEquals("timestamp missmatch",
                            packet.getTimestamp(),
                            parsed_packet.getTimestamp());
        Assert.assertEquals("version missmatch",
                            packet.getDataAsInfoData().getVersion(),
                            parsed_packet.getDataAsInfoData().getVersion());
        Assert.assertEquals("applicationI missmatch",
                            packet.getDataAsInfoData().getApplicationId(),
                            parsed_packet.getDataAsInfoData().getApplicationId());
    }

    @Test
    public void testIdInitPacket() throws Exception {

        final Packet packet = new Packet(Command.INITIALIZE);
        packet.setTimestamp(1234);
        packet.setData(new IdData((short)1));

        //--------------------------------
        final Packet parsed_packet = writeAndParse(packet);

        Assert.assertNotNull("could not parse packet", parsed_packet);

        Assert.assertEquals("command missmatch", packet.getCmd(), parsed_packet.getCmd());
        Assert.assertEquals("timestamp missmatch",
                            packet.getTimestamp(),
                            parsed_packet.getTimestamp());
        Assert.assertEquals("version missmatch",
                            packet.getDataAsIdData().getId(),
                            parsed_packet.getDataAsIdData().getId());
    }

    @Test
    public void testIdDiscoverPacket() throws Exception {

        final Packet packet = new Packet(Command.DISCOVER);
        packet.setTimestamp(1234);
        packet.setData(new IdData((short)1));

        //--------------------------------
        final Packet parsed_packet = writeAndParse(packet);

        Assert.assertNotNull("could not parse packet", parsed_packet);

        Assert.assertEquals("command missmatch", packet.getCmd(), parsed_packet.getCmd());
        Assert.assertEquals("timestamp missmatch",
                            packet.getTimestamp(),
                            parsed_packet.getTimestamp());
        Assert.assertEquals("id missmatch",
                            packet.getDataAsIdData().getId(),
                            parsed_packet.getDataAsIdData().getId());
    }

    @Test
    public void testParameterUpdatePacket() throws Exception {

        final Packet packet = new Packet(Command.UPDATE);
        packet.setTimestamp(1234);

        final StringParameter parameter = new StringParameter((short)123);
        parameter.setValue("string value");

        packet.setData(parameter);

        //--------------------------------
        final Packet parsed_packet = writeAndParse(packet);

        Assert.assertNotNull("could not parse packet", parsed_packet);

        Assert.assertEquals("command missmatch", packet.getCmd(), parsed_packet.getCmd());
        Assert.assertEquals("timestamp missmatch",
                            packet.getTimestamp(),
                            parsed_packet.getTimestamp());

        IParameter parsed_parameter = parsed_packet.getDataAsParameter();

        Assert.assertEquals("parameter id missmatch", parameter.getId(), parsed_parameter.getId());
        Assert.assertEquals("parameter value missmatch",
                            parameter.getValue(),
                            parsed_parameter.getStringValue());
    }

    @Test
    public void testParameterRemovePacket() throws Exception {

        final Packet packet = new Packet(Command.REMOVE);
        packet.setTimestamp(1234);

        final StringParameter parameter = new StringParameter((short)123);
        parameter.setValue("string value");

        packet.setData(new IdData(parameter.getId()));

        //--------------------------------
        final Packet parsed_packet = writeAndParse(packet);

        Assert.assertNotNull("could not parse packet", parsed_packet);

        Assert.assertEquals("command missmatch", packet.getCmd(), parsed_packet.getCmd());
        Assert.assertEquals("timestamp missmatch",
                            packet.getTimestamp(),
                            parsed_packet.getTimestamp());

        final IdData parsed_id_data = parsed_packet.getDataAsIdData();

        Assert.assertEquals("parameter id missmatch", parameter.getId(), parsed_id_data.getId());
    }

    //--------------------------------
    //--------------------------------
    private Packet writeAndParse(final RCPWritable _writable) throws Exception {

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {

            _writable.write(os, true);

            final byte[] the_bytes = os.toByteArray();
            System.out.println("bytes:\n" + bytesToHex(the_bytes));

            // parse
            try {
                return Packet.parse(new ByteBufferKaitaiStream(the_bytes));
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
        catch (final RCPException _e) {
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
}
