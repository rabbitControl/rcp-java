package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.*;

public class EnumDefinition extends DefaultDefinition<Integer> {

    public static final int MAX_ENTRY_SIZE = (2^16)-1;

    //------------------------------------------------------------
    //------------------------------------------------------------
    private List<String> entries;
    private boolean entriesChanged;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public EnumDefinition() {
        super(Datatype.ENUM);
    }

    @Override
    public void writeValue(final Integer _value, final OutputStream _outputStream) throws
                                                                                   IOException {
        if (_value != null) {
            _outputStream.write(ByteBuffer.allocate(2).putShort(_value.shortValue()).array());
        } else {
            _outputStream.write(ByteBuffer.allocate(2).putShort((short)0).array());
        }
    }

    @Override
    public Integer readValue(final KaitaiStream _io) {

        return _io.readU2be();
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        final EnumOptions option = EnumOptions.byId(_propertyId);

        if (option == null) {
            return false;
        }


        switch (option) {
            case DEFAULT:
                setDefault(readValue(_io));
                return true;

            case ENTRIES:
            {
                final List<String> _entris = new ArrayList<String>();

                // read amount of entries
                final int enum_length = _io.readU2be();

                for (int i=0; i<enum_length; i++) {
                    final TinyString tinyString = new TinyString(_io);

                    // TODO: check tinyString.data() != null... empty... etc...??

                    _entris.add(tinyString.data());
                }

                setEntries(_entris);
                return true;
            }
        }

        return false;
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean all) throws IOException {

        // write mandatory fields and defaultValue
        _outputStream.write((int)getDatatype().id());

        //
        // default
        //
        if (getDefault() != null) {

            if (all || defaultValueChanged) {

                // use any of the default values id
                _outputStream.write((int)EnumOptions.DEFAULT.id());
                writeValue(getDefault(), _outputStream);

                if (!all) {
                    defaultValueChanged = false;
                }
            }
        } else if (defaultValueChanged) {

            _outputStream.write((int)EnumOptions.DEFAULT.id());
            writeValue(null, _outputStream);

            defaultValueChanged = false;
        }


        //
        // entries
        //
        if (entries != null) {

            if (all || entriesChanged) {

                // use any of the default values id
                _outputStream.write((int)EnumOptions.ENTRIES.id());

                // TODO: check if entries.size <= uint16!!

                // write length
                _outputStream.write(ByteBuffer.allocate(2).putShort((short)entries.size()).array());

                // write entries...
                for (final String entry : entries) {
                    RCPParser.writeTinyString(entry, _outputStream);
                }

                if (!all) {
                    entriesChanged = false;
                }
            }
        } else if (entriesChanged) {

            _outputStream.write((int)EnumOptions.ENTRIES.id());

            // write 0
            _outputStream.write(ByteBuffer.allocate(2).putShort((short)0).array());

            entriesChanged = false;
        }


        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }




    //------------------------------------------------------------
    //------------------------------------------------------------

    @Override
    public void setDefault(final Integer _default) {
        super.setDefault(conformValue(_default));
    }

    //------------------------------------------------------------


    // TODO: unmodifyable list?
    public List<String> getEntries() {
        return entries;
    }

    public void setEntries(final List<String> _entries) {

        // TODO: compare, only set if changed?
        // TODO: check size of incoming list

        entries = _entries;
        entriesChanged = true;
    }

    public boolean addEntry(final String _entry) {

        if (entries == null) {
            entries = new ArrayList<String>();
        }

        // TODO: should we limit here? or when we write it?
        if (entries.size() < MAX_ENTRY_SIZE) {
            entries.add(_entry);
            entriesChanged = true;
            return true;
        }

        // not added...
        return false;
    }

    public boolean removeEntry(final String _entry) {

        if (entries == null) {
            return false;
        }

        boolean removed = entries.remove(_entry);
        entriesChanged |= removed;

        return removed;
    }

    public int getEntrySize() {
        return entries.size();
    }

    public int conformValue(int _value) {

        if (_value < 0) {
            return 0;
        } else if ((entries != null) && (_value >= entries.size())) {
            return entries.size() - 1;
        } else {
            return _value;
        }
    }
}
