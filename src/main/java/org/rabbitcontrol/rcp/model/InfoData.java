package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.InfodataOptions;
import org.rabbitcontrol.rcp.model.RcpTypes.TinyString;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;

import java.io.IOException;
import java.io.OutputStream;

public class InfoData implements RCPWritable {

    public static InfoData parse(final KaitaiStream _io) throws RCPDataErrorException {

        // read mandatory semver tinystring
        final String semver_string        = new TinyString(_io).data();
        String applicationid_string = "";

        // read version options
        while (!_io.isEof()) {

            final int property_id = _io.readU1();

            if (property_id == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            final InfodataOptions option = InfodataOptions.byId(property_id);

            if (option == null) {
                // wrong data id... skip whole packet?
                throw new RCPDataErrorException();
            }

            if (option == InfodataOptions.APPLICATIONID) {
                applicationid_string = new TinyString(_io).data();
            }
        }

        return new InfoData(semver_string, applicationid_string);
    }

    private String version;

    private String applicationId = "";

    public InfoData(final String version) {
        this.version = version;
    }

    public InfoData(final String version, final String applicationId) {
        this.version = version;
        this.applicationId = applicationId;
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        // write mandatory data
        RCPParser.writeTinyString(version, _outputStream);

        // write options
        if (!applicationId.isEmpty()) {
            _outputStream.write((int)InfodataOptions.APPLICATIONID.id());
            RCPParser.writeTinyString(applicationId, _outputStream);
        }

        // finalize packet with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    public String getVersion() {

        return version;
    }

    public String getApplicationId() {

        return applicationId;
    }
}
