package org.rabbitcontrol.rcp;

import org.junit.Assert;
import org.junit.Test;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.Float32Parameter;
import org.rabbitcontrol.rcp.transport.ServerTransporter;
import org.rabbitcontrol.rcp.transport.ServerTransporterListener;

public class UpdateValueTest {

    @Test
    public void testUpdateValue() throws Exception, RCPParameterException {

        RCPServer           server    = new RCPServer();

        server.addTransporter(new ServerTransporter() {

            @Override
            public void bind(final int port) throws RCPException {

            }

            @Override
            public void unbind() {

            }

            @Override
            public void sendToOne(final byte[] _data, final Object _id) {

            }

            @Override
            public void sendToAll(final byte[] _data, final Object _excludeId) {

            }

            @Override
            public int getConnectionCount() {

                return 0;
            }

            @Override
            public void addListener(final ServerTransporterListener _listener) {

            }

            @Override
            public void removeListener(final ServerTransporterListener _listener) {

            }
        });
        final Float32Parameter parameter = server.createFloatParameter("test");

        // flush changes
        server.update();
        Assert.assertTrue("wrong state", parameter.onlyValueChanged());

        // change label and expect false
        parameter.setLabel("new");
        Assert.assertFalse("wrong state", parameter.onlyValueChanged());

        // flush changes
        server.update();
        Assert.assertTrue("wrong state", parameter.onlyValueChanged());

        // set value, expect true
        parameter.setValue(3.33F);
        Assert.assertTrue("wrong state", parameter.onlyValueChanged());

        // change typedef and expect false
        parameter.getTypeDefinition().setDefault(1.0F);
        Assert.assertFalse("wrong state", parameter.onlyValueChanged());

        // flush changes
        server.update();
        Assert.assertTrue("wrong state", parameter.onlyValueChanged());

        // change typedef and expect false
        parameter.setValue(3.35F);
        parameter.getTypeDefinition().setMinimum(2.0F);
        Assert.assertFalse("wrong state", parameter.onlyValueChanged());
    }
}
