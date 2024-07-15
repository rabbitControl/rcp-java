package org.rabbitcontrol.rcp;

import org.junit.Test;

import java.util.UUID;

import static org.rabbitcontrol.rcp.model.types.CustomDefinition.convertUUIDToBytes;

public class CustomParameterTest {



    @Test
    public void TestUuid() throws Exception {
        final UUID u = UUID.randomUUID();

        byte [] b = convertUUIDToBytes(u);

        System.out.println("uuuu: "+ u);
        System.out.println("size: "+ b.length);
     }
}
