package org.rabbitcontrol.rcp.model.exceptions;

/**
 * Created by inx on 13/06/17.
 */
public class RCPException extends Exception {

    public RCPException() {

    }

    public RCPException(final String _s) {
        super(_s);
    }

    public RCPException(final Throwable _t) {
        super(_t);
    }

}
