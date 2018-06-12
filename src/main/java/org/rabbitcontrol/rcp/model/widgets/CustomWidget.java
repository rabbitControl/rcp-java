package org.rabbitcontrol.rcp.model.widgets;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.CustomwidgetOptions;
import org.rabbitcontrol.rcp.model.RcpTypes.Widgettype;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

public class CustomWidget extends WidgetImpl{

    UUID uuid;
    boolean uuidChanged;

    byte[] config;
    boolean configChanged;

    //

    public CustomWidget() {

        super(Widgettype.CUSTOMWIDGET);
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        CustomwidgetOptions option = CustomwidgetOptions.byId(_propertyId);

        switch (option) {

            case UUID:
                byte[] uid = _io.readBytes(16);
                setUuid(UUID.nameUUIDFromBytes(uid));
                break;

            case CONFIG:
                long size = _io.readU4be();
                setConfig(_io.readBytes(size));
                break;
        }

        return false;
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        super.write(_outputStream, _all);

        //
        // format
        //
        if (uuid != null) {

            if (_all || uuidChanged || initialWrite) {

                _outputStream.write((int)CustomwidgetOptions.UUID.id());
                _outputStream.write(ByteBuffer.allocate(8).putLong(uuid.getMostSignificantBits()).array());
                _outputStream.write(ByteBuffer.allocate(8).putLong(uuid.getLeastSignificantBits()).array());

                if (!_all) {
                    uuidChanged = false;
                }
            }
        }
        else if (uuidChanged) {

            _outputStream.write((int)CustomwidgetOptions.UUID.id());
            _outputStream.write(ByteBuffer.allocate(8).putLong(0).array());
            _outputStream.write(ByteBuffer.allocate(8).putLong(0).array());

            uuidChanged = false;
        }

        //
        // config
        //
        if (config != null) {

            if (_all || configChanged || initialWrite) {

                _outputStream.write((int)CustomwidgetOptions.CONFIG.id());
                _outputStream.write(ByteBuffer.allocate(4).putInt(config.length).array());
                _outputStream.write(config);

                if (!_all) {
                    configChanged = false;
                }
            }
        }
        else if (configChanged) {

            _outputStream.write((int)CustomwidgetOptions.CONFIG.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());

            uuidChanged = false;
        }

        // write terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    //
    private UUID getUuid() {

        return uuid;
    }

    private void setUuid(final UUID _uuid) {

        if ((uuid == _uuid) || ((uuid != null) && uuid.equals(_uuid))) {
            return;
        }

        uuid = _uuid;
        uuidChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    private byte[] getConfig() {

        return config;
    }

    private void setConfig(final byte[] _config) {

        config = _config;
        configChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    @Override
    public void dump() {

        super.dump();

        System.out.println("uuid: " + uuid);
        System.out.println("config: " + new String(config));
    }
}
