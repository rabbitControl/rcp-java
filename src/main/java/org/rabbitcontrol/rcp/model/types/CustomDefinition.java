package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.CustomtypeOptions;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

public class CustomDefinition extends DefaultDefinition<byte[]> {

    public static byte[] convertUUIDToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID convertBytesToUUID(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        return new UUID(high, low);
    }

    // mandatory
    long size;


    // optional
    private UUID uuid;
    private boolean uuidChanged;

    private byte[] config;
    boolean configChanged;

    public CustomDefinition(final long size) {
        super(Datatype.CUSTOMTYPE);
        this.size = size;
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final CustomtypeOptions option = CustomtypeOptions.byId(_propertyId);

        if (null == option) {
            return false;
        }

        switch (option)
        {
            case DEFAULT:
                setDefault(readValue(_io));
                return true;

            case UUID:
                uuid = convertBytesToUUID(_io.readBytes(16));
                return true;

            case CONFIG:
            {
                long size = _io.readU4be();
                config = _io.readBytes(size);
                return true;
            }
        }

        return false;
    }

    @Override
    public void writeOptions(final OutputStream _outputStream, final boolean _all) throws IOException, RCPException {

        // write mandatory
        _outputStream.write(ByteBuffer.allocate(4).putInt((int)size).array());


        // write options

        //
        // default
        //
        if ((getDefault() != null) && (getDefault().length == size))
        {
            if (_all || defaultValueChanged || initialWrite) {

                _outputStream.write((int)CustomtypeOptions.DEFAULT.id());
                writeValue(getDefault(), _outputStream);

                if (!_all) {
                    defaultValueChanged = false;
                }
            }
        }
        else if (defaultValueChanged) {

            _outputStream.write((int)CustomtypeOptions.DEFAULT.id());
            _outputStream.write(ByteBuffer.allocate((int)size).putInt(0).array());

            defaultValueChanged = false;
        }


        //
        // uuid
        //
        if (uuid != null) {

            if (_all || uuidChanged || initialWrite) {

                _outputStream.write((int) CustomtypeOptions.UUID.id());
                _outputStream.write(convertUUIDToBytes(uuid));

                if (!_all) {
                    uuidChanged = false;
                }
            }
        }
        else if (uuidChanged) {

            // delete uuid?
            _outputStream.write((int)CustomtypeOptions.UUID.id());
            _outputStream.write(ByteBuffer.allocate(16).putInt(0).array());

            uuidChanged = false;
        }

        //
        // config
        //
        if ((config != null) && (config.length > 0)) {

            if (_all || configChanged || initialWrite) {

                _outputStream.write((int)CustomtypeOptions.CONFIG.id());
                _outputStream.write(ByteBuffer.allocate(4).putInt(config.length).array());
                _outputStream.write(config);

                if (!_all) {
                    configChanged = false;
                }
            }
        }
        else if (configChanged) {

            _outputStream.write((int)CustomtypeOptions.CONFIG.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());

            configChanged = false;
        }
    }

    @Override
    public void writeValue(final byte[] _value, final OutputStream _outputStream) throws IOException {

        if (_value.length != size)
        {
            throw new IOException("invalid value");
        }

        _outputStream.write(_value);
    }

    @Override
    public byte[] readValue(final KaitaiStream _io) {
        return _io.readBytes(size);
    }


    //------------------------------------------------------------
    //------------------------------------------------------------

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID _uuid)
    {
        if ((uuid == _uuid) || ((uuid != null) && uuid.compareTo(_uuid) == 0)) {
            return;
        }

        this.uuid = _uuid;
        uuidChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }


    public byte[] getConfig() {
        return config;
    }

    public void setConfig(byte[] _config) {

        if (Arrays.equals(config, _config))
        {
            return;
        }

        config = _config;
        configChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }
}
