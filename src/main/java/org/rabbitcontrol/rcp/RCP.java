package org.rabbitcontrol.rcp;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

public class RCP {

    private static String RCP_JAVA_VERSION = "0.0.0";

    static {
        Enumeration<URL> resources = null;
        try {
            resources = RCP.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                try {

                    final URL url = resources.nextElement();
                    if (!url.toString().contains("rabbitcontrol/rcp")) {
                        continue;
                    }
                    final Manifest manifest = new Manifest(url.openStream());
                    final String   version  = manifest.getMainAttributes().getValue(
                            "Implementation-Version");
                    if ((version != null) && !version.isEmpty()) {
                        RCP_JAVA_VERSION = version;
                    }
                }
                catch (IOException _e) {
                    // nop
                }
            }
        }
        catch (IOException _e) {
            // nop
        }
    }

    public static String getVersion() {

        return RCP_JAVA_VERSION;
    }
}
