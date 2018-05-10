package org.rabbitcontrol.rcp.model.interfaces;

import org.rabbitcontrol.rcp.model.RCPWritable;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;

public interface IParameter extends RCPWritable {

    //--------------------------------
    // mandatory
    short getId();

    ITypeDefinition getTypeDefinition();

    //--------------------------------
    // optional
    String getLabel();

    void setLabel(final String _label);

    String getDescription();

    void setDescription(final String _description);

    String getTags();

    void setTags(final String _tags);

    Integer getOrder();

    void setOrder(final int _order);

    GroupParameter getParent();

    //    void setParent(GroupParameter _parent);

    void setWidgetType(short type);

    String getUserid();

    void setUserid(final String _userid);

    byte[] getUserdata();

    void setUserdata(final byte[] _userdata);

    void setDirty();

    //
    void dump();
}
