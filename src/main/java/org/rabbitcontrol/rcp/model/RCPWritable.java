package org.rabbitcontrol.rcp.model;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by inx on 13/06/17.
 */
public interface RCPWritable {

    void write(final OutputStream _outputStream, final boolean _all) throws IOException;
}
