package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RcpTypes.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.*;

public class EnumDefinition extends DefaultDefinition<String> {

    public static final int MAX_ENTRY_SIZE = (2^16)-1;

    //------------------------------------------------------------
    //------------------------------------------------------------
    private List<String> entries;
    private boolean entriesChanged;

    private Boolean multiselect;
    private boolean multiselectChanged;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public EnumDefinition() {
        super(Datatype.ENUM);
    }

    @Override
    public void writeValue(final String _value, final OutputStream _outputStream) throws
                                                                                   IOException {
        if (_value != null) {
            RCPParser.writeTinyString(_value, _outputStream);
        } else {
            RCPParser.writeTinyString("", _outputStream);
        }
    }

    @Override
    public String readValue(final KaitaiStream _io) {

        return new TinyString(_io).data();
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


                while (true) {
                    final TinyString tinyString = new TinyString(_io);
                    if (tinyString.data().isEmpty()) {
                        break;
                    }

                    _entris.add(tinyString.data());
                }

                setEntries(_entris);
                return true;
            }

            case MULTISELECT:
            {
                setMultiselect(_io.readS1() != 0);
                return true;
            }
        }

        return false;
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

        // write mandatory fields and defaultValue
        _outputStream.write((int)getDatatype().id());

        //
        // default
        //
        if (getDefault() != null) {

            if (_all || defaultValueChanged || initialWrite) {

                // use any of the default values id
                _outputStream.write((int)EnumOptions.DEFAULT.id());
                writeValue(getDefault(), _outputStream);

                if (!_all) {
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

            if (_all || entriesChanged || initialWrite) {

                // use any of the default values id
                _outputStream.write((int)EnumOptions.ENTRIES.id());

                // write entries...
                for (final String entry : entries) {
                    RCPParser.writeTinyString(entry, _outputStream);
                }
                _outputStream.write(RCPParser.TERMINATOR);

                if (!_all) {
                    entriesChanged = false;
                }
            }
        } else if (entriesChanged) {

            _outputStream.write((int)EnumOptions.ENTRIES.id());

            // write 0
            _outputStream.write(ByteBuffer.allocate(2).putShort((short)0).array());

            entriesChanged = false;
        }

        //
        // multiselect
        //
        if (multiselect != null) {

            if (_all || multiselectChanged || initialWrite) {

                // use any of the default values id
                _outputStream.write((int)EnumOptions.MULTISELECT.id());
                _outputStream.write(multiselect ? 1 : 0);

                if (!_all) {
                    multiselectChanged = false;
                }
            }
        } else if (multiselectChanged) {

            _outputStream.write((int)EnumOptions.MULTISELECT.id());

            _outputStream.write(multiselect ? 1 : 0);

            entriesChanged = false;
        }

        if (!_all) {
            initialWrite = false;
        }

        // finalize with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }




    //------------------------------------------------------------
    //------------------------------------------------------------

    @Override
    public void setDefault(final String _default) {
        super.setDefault(_default);
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

        if (parameter != null) {
            parameter.setDirty();
        }
    }

    public boolean addEntry(final String _entry) {

        if (entries == null) {
            entries = new ArrayList<String>();
        }

        // TODO: should we limit here? or when we write it?
        if (entries.size() < MAX_ENTRY_SIZE) {
            entries.add(_entry);
            entriesChanged = true;

            if (parameter != null) {
                parameter.setDirty();
            }

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
        if (removed) {
            entriesChanged = true;

            if (parameter != null) {
                parameter.setDirty();
            }
        }


        return removed;
    }

    public int getEntrySize() {
        if (entries == null) {
            return 0;
        }

        return entries.size();
    }

    public boolean containsValue(final String _value) {

        if (entries == null) {
            return true;
        }

        return entries.contains(_value);
    }


    public boolean isMultiselect() {
        return multiselect;
    }

    public void setMultiselect(final boolean _multiselect) {

        if ((multiselect == _multiselect) || ((multiselect != null) && multiselect.equals(_multiselect))) {
            return;
        }

        multiselect = _multiselect;
        multiselectChanged = true;

        if (parameter != null) {
            parameter.setDirty();
        }
    }
}
