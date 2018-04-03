package org.rabbitcontrol.rcp.model;

import org.rabbitcontrol.rcp.model.interfaces.IParameter;

/**
 * Created by inx on 30/11/16.
 */
public interface RCPCommands {

    enum Status {
        DISCONNECTED,
        CONNECTED, // network connected
        VERSION_MISSMATCH,
        OK // connected and version ok
    }


    //------------------------------------------------------------
    //
    interface Add {
        void parameterAdded(IParameter param);
    }

    interface Remove {
        void parameterRemoved(IParameter param);
    }

    interface Update {
        void parameterUpdated(IParameter param);
    }

    interface ValueUpdate {
        void parameterValueUpdated(IParameter param);
    }

    interface Init {
        void init();
    }

    interface Error {
        void onError(Exception _e);
    }

    interface StatusChange {

        void statusChanged(Status status, String message);
    }
}
