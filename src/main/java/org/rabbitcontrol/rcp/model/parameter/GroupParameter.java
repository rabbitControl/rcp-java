package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.interfaces.*;
import org.rabbitcontrol.rcp.model.types.GroupDefinition;

import java.util.ArrayList;
import java.util.List;

public class GroupParameter extends Parameter {

    private List<IParameter> children = new ArrayList<IParameter>();

    //------------------------------------------------------------
    //------------------------------------------------------------
    public GroupParameter(final short _id) {

        super(_id, new GroupDefinition());
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public ITypeDefinition getTypeDefinition() {

        return typeDefinition;
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        // throw error...
        return false;
    }

    public List<IParameter> getChildren() {
        return children;
    }

    public void addChildren(final IParameter ... _parameters) {

        for (final IParameter _parameter : _parameters) {
            addChild(_parameter);
        }
    }

    public void addChild(final IParameter _parameter) {

        if (!children.contains(_parameter)) {
            children.add(_parameter);
            _parameter.setParent(this);
        }
    }

    public void removeChild(final IParameter _parameter) {

        if (children.contains(_parameter)) {
            children.remove(_parameter);
            _parameter.setParent(null);
        }
    }

    public void removeAllChildren() {

        final List<IParameter> ch = children;
        children.clear();

        for (final IParameter child : ch) {
            child.setParent(null);
        }
    }

    @Override
    public String getStringValue() {
        return String.format("group(%d)", id);
    }

    @Override
    public void setStringValue(final String _value) {

    }
}
