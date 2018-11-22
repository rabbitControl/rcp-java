package org.rabbitcontrol.rcp.model.parameter;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.types.BangDefinition;

import java.io.IOException;
import java.io.OutputStream;

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
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) throws
                                                                                  RCPDataErrorException {

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
