package org.rabbitcontrol.rcp.model;

/**
 * Created by inx on 30/11/16.
 */
public interface RCPCommands {

    //------------------------------------------------------------
    //
    String INIT = "init";

    String VERSION = "version";

    String ADD = "add";

    String REMOVE = "remove";

    String UPDATE = "update";

    //------------------------------------------------------------
    //
    interface Add {
        void added(RCPParameter<?> _value);
    }

    interface Remove {
        void removed(RCPParameter<?> _value);
    }

    interface Update {
        void updated(RCPParameter<?> _value);
    }

    interface Init {
        void init();
    }
}
