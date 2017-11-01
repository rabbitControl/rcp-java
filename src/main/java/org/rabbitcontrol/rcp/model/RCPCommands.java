package org.rabbitcontrol.rcp.model;

import org.rabbitcontrol.rcp.model.interfaces.IParameter;

/**
 * Created by inx on 30/11/16.
 */
public interface RCPCommands {

    //------------------------------------------------------------
    //
    interface Add {
        void added(IParameter _value);
    }

    interface Remove {
        void removed(IParameter _value);
    }

    interface Update {
        void updated(IParameter _value);
    }

    interface Init {
        void init();
    }
}
