package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.interfaces.ITypeDefinition;

import java.io.IOException;
import java.io.OutputStream;

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

    void readMadatory(final KaitaiStream _io) {
        // read mandatory data after typeid!
    }

    protected abstract boolean handleOption(final int _propertyId, final KaitaiStream _io);

    public void parseOptions(final KaitaiStream _io) throws RCPDataErrorException {

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

    // override to write mandatory data after datatype and before options
    public void writeMandatory(final OutputStream _outputStream) throws RCPException, IOException {
    }

    public abstract void writeOptions(final OutputStream _outputStream, final boolean _all) throws
                                                                                            IOException,
                                                                                            RCPException;


    @Override
    public final void write(final OutputStream _outputStream, final boolean _all) throws
                                                                                  RCPException,
                                                                                  IOException {

        // write mandatory fields and defaultValue
        _outputStream.write((int)getDatatype().id());

        writeMandatory(_outputStream);

        writeOptions(_outputStream, _all);

        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);

        if (!_all) {
            initialWrite = false;
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
