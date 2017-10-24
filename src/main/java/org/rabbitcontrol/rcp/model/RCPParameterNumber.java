package org.rabbitcontrol.rcp.model;

/**
 * Created by inx on 21/06/17.
 */
public class RCPParameterNumber<T extends Number> extends RCPParameter<T> {

    public RCPParameterNumber(final int _id, final RCPTypeDefinition<T> _type) {

        super(_id, _type);
    }
}
