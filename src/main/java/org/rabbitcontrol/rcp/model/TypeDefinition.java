package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.interfaces.ITypeDefinition;

public abstract class TypeDefinition implements ITypeDefinition {

    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    private final Datatype datatype;

    protected IParameter parameter;

    protected boolean initialWrite = true; // one-time-flag

    //------------------------------------------------------------
    //------------------------------------------------------------
    public TypeDefinition(final Datatype _datatype) {

        datatype = _datatype;
    }

    void setInitialWrite(final boolean _initialWrite) {
        initialWrite = _initialWrite;
    }

    protected abstract boolean handleOption(final int _propertyId, final KaitaiStream _io);

    public final void parseOptions(final KaitaiStream _io) throws RCPDataErrorException {

        // get options from the stream
        while (true) {

            // get data-id
            int property_id = _io.readU1();

            if (property_id == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            // handle option in specific implementation
            if (!handleOption(property_id, _io)) {
                throw new RCPDataErrorException();
            }
        }

    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public Datatype getDatatype() {

        return datatype;
    }

    public void setParameter(IParameter _parameter) {
        parameter = _parameter;
    }

    public void dump() {
        System.out.println("datatype: " + datatype.name());
    }
}
