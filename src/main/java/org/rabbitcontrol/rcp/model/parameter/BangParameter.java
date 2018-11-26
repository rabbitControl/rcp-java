package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.types.BangDefinition;

public class BangParameter extends Parameter {

    //------------------------------------------------------------
    //------------------------------------------------------------
    Runnable func;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public BangParameter(final short _id) {

        super(_id, new BangDefinition());
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        return false;
    }

    @Override
    public String getStringValue() {
        return "bang";
    }

    @Override
    public void setStringValue(final String _value) {
        doBang();
    }


    @Override
    public void update(final IParameter _parameter) throws RCPException {

        // we got updated!
        // execute function
        if (func != null) {
            func.run();
        }

        super.update(_parameter);
    }

    public void doBang() {
        setDirty();
    }

    public void setFunction(final Runnable _func) {
        func = _func;
    }

}
