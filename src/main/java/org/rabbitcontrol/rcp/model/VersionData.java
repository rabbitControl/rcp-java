package org.rabbitcontrol.rcp.model;

import java.io.IOException;
import java.io.OutputStream;

public class VersionData implements RCPWritable {

    public String version = "0.0.0";

    public VersionData(final String version) {
        this.version = version;
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        RCPParser.writeTinyString(version, _outputStream);
    }
}
