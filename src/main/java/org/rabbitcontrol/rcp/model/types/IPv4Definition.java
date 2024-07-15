package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.Ipv4Options;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class IPv4Definition extends DefaultDefinition<Inet4Address> {

    public IPv4Definition() {
        super(Datatype.IPV4);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {
        final Ipv4Options option = Ipv4Options.byId(_propertyId);

        if (null == option) {
            return false;
        }

        switch (option) {
            case DEFAULT: {
                setDefault(readValue(_io));
            }
            return true;
        }

        return false;
    }

    @Override
    public void writeOptions(final OutputStream _outputStream, final boolean _all) throws IOException, RCPException {

        //
        // default
        //
        if (getDefault() != null) {
            if (_all || defaultValueChanged || initialWrite) {

                // use any of the default values id
                _outputStream.write((int) RcpTypes.StringOptions.DEFAULT.id());
                writeValue(getDefault(), _outputStream);

                if (!_all) {
                    defaultValueChanged = false;
                }
            }
        } else if (defaultValueChanged) {

            _outputStream.write((int) RcpTypes.StringOptions.DEFAULT.id());
            writeValue(null, _outputStream);

            defaultValueChanged = false;
        }
    }

    @Override
    public void writeValue(final Inet4Address _value, final OutputStream _outputStream) throws IOException {
        if (_value != null) {
            // NOTE: both network byte order!
            _outputStream.write(_value.getAddress());
        } else if (defaultValue != null) {
            // NOTE: both network byte order!
            _outputStream.write(defaultValue.getAddress());
        } else {
            _outputStream.write(new byte[] {0, 0, 0, 0});
        }
    }

    @Override
    public Inet4Address readValue(final KaitaiStream _io) {
        try {
            // NOTE: both network byte order!
            return (Inet4Address) Inet4Address.getByAddress(_io.readBytes(4));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
