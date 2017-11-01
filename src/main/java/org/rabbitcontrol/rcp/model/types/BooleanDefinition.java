package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.DefaultDefinition;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.BooleanOptions;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;

import java.io.IOException;
import java.io.OutputStream;

public class BooleanDefinition extends DefaultDefinition<Boolean> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public static BooleanDefinition parse(final KaitaiStream _io) throws RCPDataErrorException {

        final BooleanDefinition type = new BooleanDefinition();

        // parse optionals
        while (true) {

            int          did    = _io.readU1();

            if (did == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            final BooleanOptions option = BooleanOptions.byId(did);

            if (option == null) {
                throw new RCPDataErrorException();
            }

            switch (option) {

                case DEFAULT:
                    type.setDefault((_io.readU1() > 0));
                    break;

                default:
                    throw new RCPDataErrorException();
            }

        }

        return type;
    }


    //------------------------------------------------------------
    //------------------------------------------------------------
    public BooleanDefinition() {

        super(Datatype.BOOLEAN);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        BooleanOptions option = BooleanOptions.byId(_propertyId);

        if (option == null) {
            return false;
        }


        switch (option) {
            case DEFAULT:
                setDefault(_io.readS1() != 0);
                return true;
        }

        return false;
    }

    @Override
    public void writeValue(final Boolean _value, final OutputStream _outputStream) throws
                                                                                   IOException {
        _outputStream.write(_value ? 1 : 0);
    }

    @Override
    public void write(final OutputStream _outputStream) throws IOException {

        // write mandatory fields and defaultValue
        _outputStream.write((int)getDatatype().id());

        if (getDefault() != null) {
            // use any of the default values id
            _outputStream.write((int)BooleanOptions.DEFAULT.id());
            writeValue(getDefault(), _outputStream);
        }

        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }
}
