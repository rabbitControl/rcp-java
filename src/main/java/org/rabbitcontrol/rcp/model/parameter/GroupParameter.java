package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.interfaces.*;
import org.rabbitcontrol.rcp.model.types.GroupDefinition;

import java.io.IOException;
import java.io.OutputStream;
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

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        // write mandatory id
        writeId(id, _outputStream);

        // write mandatory typeDefinition
        typeDefinition.write(_outputStream, _all);

        // write other options
        super.write(_outputStream, _all);

        // finalize parameter with terminator
        _outputStream.write(RCPParser.TERMINATOR);
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
            try {
                ((IParameterChild)_parameter).setParent(this);
                children.add(_parameter);
            } catch (ClassCastException _e) {
                System.err.println("parameter not a IParameterChild??");
            }
        } else {
            System.err.println("already a child: " + _parameter.getId());
        }

    }

    public void removeChild(final IParameter _parameter) {

        if (children.contains(_parameter)) {
            try {
                ((IParameterChild)_parameter).setParent(null);
                children.remove(_parameter);
            } catch (ClassCastException _e) {
                System.err.println("parameter not a IParameterChild??");
            }
        } else {
            System.err.println("not a child: " + _parameter.getId());
        }

    }

    @Override
    public String getStringValue() {

        return "group:"+getId();
    }

    @Override
    public void setStringValue(final String _value) {

    }
}
