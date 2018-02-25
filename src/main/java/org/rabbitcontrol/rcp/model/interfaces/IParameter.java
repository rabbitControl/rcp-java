package org.rabbitcontrol.rcp.model.interfaces;

import org.rabbitcontrol.rcp.model.RCPWritable;

import java.nio.ByteBuffer;

public interface IParameter extends RCPWritable {

    //--------------------------------
    // mandatory
    ByteBuffer getId();

    ITypeDefinition getTypeDefinition();

    //--------------------------------
    // optional
    String getLabel();

    void setLabel(final String _label);

    String getDescription();

    void setDescription(final String _description);

    Integer getOrder();

    void setOrder(final int _order);

    Integer getParentId();

    void setParentId(final int _parentId);

    byte[] getUserdata();

    void setUserdata(final byte[] _userdata);

    //
    void dump();
}
