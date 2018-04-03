package org.rabbitcontrol.rcp.test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;

public class Test {

    public static void main(String[] args) {

        Map<ByteBuffer, String> m = new HashMap<ByteBuffer, String>();

        final UUID u = UUID.randomUUID();
        u.toString();
        byte[] b = {1, 2, 3, 0, -1};
        byte[] b1 = {1, 2, 3, 0, -1};


        m.put(ByteBuffer.wrap(b), "test");



        if (m.containsKey(ByteBuffer.wrap(b1))){
            System.out.println("YES");
        } else {
            System.out.println("NOOO");
        }

        String s1 = new String(b, Charset.forName("UTF-8"));
        String s2 = new String(b1, Charset.forName("UTF-8"));

        if (Arrays.equals(b, b1)) {

            System.out.println("same: " + b + " : " + b1);

        } else {
            System.out.println("not same");
        }

    }
}
