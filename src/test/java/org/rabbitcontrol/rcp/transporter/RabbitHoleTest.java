package org.rabbitcontrol.rcp.transporter;

import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.transport.websocket.server.RabbitHoleWsServerTransporterNetty;

import java.net.URI;
import java.net.URISyntaxException;

public class RabbitHoleTest {

    public static void main(final String[] args)
    {
        try
        {
            final URI uri = new URI("wss://rabbithole.rabbitcontrol" +
                                    ".cc/public/rcpserver/connect?key=rcp_java_test");
            final RabbitHoleWsServerTransporterNetty transporter = new RabbitHoleWsServerTransporterNetty(uri);
            transporter.bind(0);
        }
        catch (final RCPException _e) {
            _e.printStackTrace();
        }
        catch (final URISyntaxException _e) {
            _e.printStackTrace();
        }

    }
}
