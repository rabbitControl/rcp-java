package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.*;
import org.rabbitcontrol.rcp.model.exceptions.*;
import org.rabbitcontrol.rcp.model.interfaces.*;
import org.rabbitcontrol.rcp.model.parameter.ArrayParameter;
import org.rabbitcontrol.rcp.model.parameter.CustomParameter;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;
import org.rabbitcontrol.rcp.model.types.ArrayDefinition;
import org.rabbitcontrol.rcp.model.widgets.WidgetImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.*;

public abstract class Parameter implements IParameter {

    private static final String LANGUAGE_ANY_STR = "any";

    private static final byte[] LANGUAGE_ANY     = LANGUAGE_ANY_STR.getBytes();

    //------------------------------------------------------------
    //------------------------------------------------------------
    public static Parameter parse(final KaitaiStream _io, final IParameterManager _manager) throws
                                                                                            RCPUnsupportedFeatureException,
                                                                                            RCPDataErrorException {

        // TODO: precheck if we have enough data
        // minimum packet: 4,18,0,9,39,0,0,0
        // min bytes to read: 6

        // create parameter from stream
        final Parameter param = createParameterFromStream(_io);
        param.setManager(_manager);

        // read options from stream
        param.parseTypeOptions(_io);
        param.parseOptions(_io);
        return param;
    }

    public static Parameter parseValueUpdate(final KaitaiStream _io) throws
                                                                     RCPUnsupportedFeatureException,
                                                                     RCPDataErrorException {

        // create parameter from stream
        final Parameter param = createParameterFromStream(_io);

        // read value from stream
        param.handleOption((int)ParameterOptions.VALUE.id(), _io);
        return param;
    }

    private static Parameter createParameterFromStream(final KaitaiStream _io) throws
                                                                     RCPDataErrorException,
                                                                     RCPUnsupportedFeatureException {

        // get mandatory id
        final short parameter_id = _io.readS2be();

        // read mandatory typeDefinition
        final Datatype datatype = Datatype.byId(_io.readU1());

        if (datatype == null) {
            throw new RCPDataErrorException("wrong datatype");
        }

        // handle certain datatypes...
        if (datatype == Datatype.RANGE) {

            // read element-type
            final Datatype element_datatype = Datatype.byId(_io.readU1());
            return RCPFactory.createRangeParameter(parameter_id, element_datatype);
        }

        if (datatype == Datatype.ARRAY) {

            // create ArrayDefinition
            final ArrayDefinition<?, ?> array_def = ArrayDefinition.parse(_io);
            return ArrayParameter.createFixed(parameter_id, array_def, array_def.getElementType());
        }

        if (datatype == Datatype.LIST) {

            // read mandatory subtype

        }
        else if (datatype == Datatype.CUSTOMTYPE) {
            // read mandatory
            long size = _io.readU4be();
            return new CustomParameter(parameter_id, size);
        }

        // implicitly create typeDefinition
        return (Parameter)RCPFactory.createParameter(parameter_id, datatype);
    }

    //------------------------------------------------------------
    public interface PARAMETER_UPDATED {

        void updated(final IParameter _parameter);
    }

    public interface PARAMETER_VALUE_UPDATED {

        void valueUpdated(final IParameter _parameter);
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    protected final short id;

    protected final TypeDefinition typeDefinition;

    // optional
    protected String label;

    protected Map<String, String> languageLabels = new HashMap<String, String>();

    private   boolean             labelChanged;

    protected String description;

    protected Map<String, String> languageDescriptions = new HashMap<String, String>();

    private   boolean             descriptionChanged;

    protected String tags;

    private boolean tagsChanged;

    protected Integer order;

    private boolean orderChanged;

    private GroupParameter parent;

    private ArrayList<Short> pendingParentIds = new ArrayList<Short>();

    private boolean parentChanged;

    // widget
    private Widget widget;

    private boolean widgetChanged;

    //
    protected byte[] userdata;

    private boolean userdataChanged;

    private String userid;

    private boolean useridChanged;

    private Boolean readonly;

    private boolean readonlyChanged;

    private IParameterManager parameterManager;

    protected boolean initialWrite = true; // one-time-flag

    private Map<ParameterOptions, Boolean> dirtyOptionsMap = new HashMap<ParameterOptions,
            Boolean>();

    //------------------------
    // change listener
    private final Set<PARAMETER_UPDATED> parameterUpdateListener = new HashSet<PARAMETER_UPDATED>();

    private final Set<PARAMETER_VALUE_UPDATED> valueUpdateListener = new HashSet<PARAMETER_VALUE_UPDATED>();

    //------------------------------------------------------------
    //------------------------------------------------------------
    public Parameter(final short _id, final TypeDefinition _typeDefinition) {

        id = _id;
        typeDefinition = _typeDefinition;
        typeDefinition.setParameter(this);
    }

    public IParameter cloneEmpty() {

        return RCPFactory.createParameter(id, typeDefinition.getDatatype());
    }

    @Override
    public boolean onlyValueChanged()
    {
        return dirtyOptionsMap.isEmpty() && !typeDefinition.didChange();
    }

    protected abstract boolean handleOption(final int _propertyId, final KaitaiStream _io) throws
                                                                                           RCPDataErrorException;

    protected void parseTypeOptions(final KaitaiStream _io) throws RCPDataErrorException {

        typeDefinition.parseOptions(_io);
    }

    private void parseOptions(final KaitaiStream _io) throws
                                                      RCPDataErrorException,
                                                      RCPUnsupportedFeatureException {

        // !! attention: this only parses parameter options
        // !! parse typedefinition options before

        // get options from the stream
        while (true) {

            if (_io.isEof()) {
                // buffer underflow
                throw new RCPDataErrorException("Buffer underrun");
            }

            // get data-id
            final int property_id = _io.readU1();

            if (property_id == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            final ParameterOptions option = ParameterOptions.byId(property_id);

            if (option == null) {
                break;
            }

            switch (option) {

                case LABEL: {

                    int  current = _io.pos();
                    byte ppeekk  = _io.readS1();

                    while (ppeekk != 0) {

                        // rewind one
                        _io.seek(current);

                        final String lang_code = new String(_io.readBytes(LANGUAGE_ANY.length));
                        final String label     = new TinyString(_io).data();

                        if (LANGUAGE_ANY_STR.equals(lang_code)) {
                            setLabel(label);
                        }
                        else {
                            setLanguageLabel(lang_code, label);
                        }

                        current = _io.pos();
                        ppeekk = _io.readS1();
                    }

                    //                    final TinyString tinyString = new TinyString(_io);
                    //                    setLabel(tinyString.data());
                }
                break;

                case DESCRIPTION:

                    int  current = _io.pos();
                    byte ppeekk  = _io.readS1();

                    while (ppeekk != 0) {

                        // rewind one
                        _io.seek(current);

                        final String lang_code = new String(_io.readBytes(LANGUAGE_ANY.length));
                        final String label     = new ShortString(_io).data();

                        if (LANGUAGE_ANY_STR.equals(lang_code)) {
                            setDescription(label);
                        }
                        else {
                            setLanguageDescription(lang_code, label);
                        }

                        current = _io.pos();
                        ppeekk = _io.readS1();
                    }

                    //                    final ShortString shortString = new ShortString(_io);
                    //                    setDescription(shortString.data());
                    break;

                case TAGS: {
                    final TinyString tinyString = new TinyString(_io);
                    setTags(tinyString.data());
                }
                break;

                case ORDER:
                    setOrder(_io.readS4be());
                    break;

                case PARENTID:
                    // read as id, this is correct
                    final short parent_id = _io.readS2be();
                    if ((parent_id != 0)) {
                        if ((parameterManager != null)) {
                            final IParameter parent = parameterManager.getParameter(parent_id);

                            if (parent instanceof GroupParameter) {
                                setParent((GroupParameter)parent);
                            } else {
                                System.err.println("parameter not a GroupParameter!");
                            }
                        } else {
                            // no parameter manager, but parent_id != 0
                            // store this id
                            pendingParentIds.add(parent_id);
                        }
                    }
                    break;

                case USERID:
                    final TinyString tinyString = new TinyString(_io);
                    setUserid(tinyString.data());
                    break;

                case USERDATA:
                    final Userdata ud = new Userdata(_io);
                    setUserdata(ud.data());
                    break;

                case READONLY:
                    setReadonly(_io.readS1() > 0);
                    break;

                // handle in specific implementations
                case VALUE:
                default:
                    if (!handleOption(property_id, _io)) {
                        // widget may be handled by NumberParameter
                        // if not then we handle it here!
                        if (option == ParameterOptions.WIDGET) {
                            setWidget(WidgetImpl.parse(_io));
                        } else {
                            throw new RCPDataErrorException();
                        }
                    }
            }

        }

    }

    protected void writeId(final short _id, final OutputStream _outputStream) throws IOException {

        _outputStream.write(ByteBuffer.allocate(2).putShort(_id).array());

    }

    private void writeLabel(final OutputStream _outputStream) throws IOException {

        _outputStream.write((int)ParameterOptions.LABEL.id());

        // concat label and all language-labels
        if (label != null) {
            _outputStream.write(LANGUAGE_ANY);
            RCPParser.writeTinyString(label, _outputStream);
        }

        if (languageLabels != null) {

            for (final String key : languageLabels.keySet()) {

                if (key.length() < 3) {
                    continue;
                }

                final String value = languageLabels.get(key);
                _outputStream.write(Arrays.copyOf(key.getBytes(), 3));
                RCPParser.writeTinyString(value, _outputStream);
            }
        }

        _outputStream.write(RCPParser.TERMINATOR);
    }

    private void writeDescription(final OutputStream _outputStream) throws IOException {

        _outputStream.write((int)ParameterOptions.DESCRIPTION.id());

        // concat label and all language-labels
        if (description != null) {
            _outputStream.write(LANGUAGE_ANY);
            RCPParser.writeShortString(description, _outputStream);
        }

        if (languageDescriptions != null) {

            for (final String key : languageDescriptions.keySet()) {

                if (key.length() < 3) {
                    continue;
                }

                final String value = languageDescriptions.get(key);
                _outputStream.write(Arrays.copyOf(key.getBytes(), 3));
                RCPParser.writeShortString(value, _outputStream);
            }
        }

        _outputStream.write(RCPParser.TERMINATOR);
    }

    public void writeUpdateValue(final OutputStream _outputStream) throws IOException,
                                                                          RCPException {

        writeId(id, _outputStream);

        // write mandatory typeDefinition
        _outputStream.write((int)typeDefinition.getDatatype().id());

        // write mandatory
        typeDefinition.writeMandatory(_outputStream);
    }

    public void writeOptions(final OutputStream _outputStream, final boolean _all) throws
                                                                                   IOException,
                                                                                   RCPException {

        // write options

        //
        // label
        //
        if ((label != null) || !languageLabels.isEmpty()) {

            if (_all || labelChanged || initialWrite) {


                writeLabel(_outputStream);

                // clear flag
                if (!_all) {
                    labelChanged = false;
                }
            }
        }
        else if (labelChanged) {
            // send default value
            _outputStream.write((int)ParameterOptions.LABEL.id());
            _outputStream.write(RCPParser.TERMINATOR);

            labelChanged = false;
        }

        //
        // description
        //
        if ((description != null) || !languageDescriptions.isEmpty()) {

            if (_all || descriptionChanged || initialWrite) {

                writeDescription(_outputStream);

                if (!_all) {
                    descriptionChanged = false;
                }
            }
        }
        else if (descriptionChanged) {

            _outputStream.write((int)ParameterOptions.DESCRIPTION.id());
            _outputStream.write(RCPParser.TERMINATOR);

            descriptionChanged = false;
        }

        //
        // tags
        //
        if (tags != null) {

            if (_all || tagsChanged || initialWrite) {

                _outputStream.write((int)ParameterOptions.TAGS.id());
                RCPParser.writeTinyString(tags, _outputStream);

                if (!_all) {
                    tagsChanged = false;
                }
            }
        }
        else if (tagsChanged) {

            _outputStream.write((int)ParameterOptions.TAGS.id());
            RCPParser.writeTinyString("", _outputStream);

            tagsChanged = false;
        }

        //
        // order
        //
        if (order != null) {

            if (_all || orderChanged || initialWrite) {

                _outputStream.write((int)ParameterOptions.ORDER.id());
                _outputStream.write(ByteBuffer.allocate(4).putInt(order).array());

                if (!_all) {
                    orderChanged = false;
                }
            }
        }
        else if (orderChanged) {

            _outputStream.write((int)ParameterOptions.ORDER.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());

            orderChanged = false;
        }

        //
        // parentId
        //
        if (parent != null) {

            if (_all || parentChanged || initialWrite) {

                _outputStream.write((int)ParameterOptions.PARENTID.id());
                writeId(parent.getId(), _outputStream);

                if (!_all) {
                    parentChanged = false;
                }
            }
        }
        else if (parentChanged) {

            _outputStream.write((int)ParameterOptions.PARENTID.id());
            writeId((short)0, _outputStream);

            parentChanged = false;
        }

        //
        // widget
        //
        if (widget != null) {

            if (_all || widgetChanged || initialWrite) {

                _outputStream.write((int)ParameterOptions.WIDGET.id());
                widget.write(_outputStream, _all);

                if (!_all) {
                    widgetChanged = false;
                }
            }
        }
        else if (widgetChanged) {

            _outputStream.write((int)ParameterOptions.WIDGET.id());
            _outputStream.write(RCPParser.TERMINATOR);

            widgetChanged = false;
        }

        //
        // userdata
        //
        if (userdata != null) {

            if (_all || userdataChanged || initialWrite) {

                _outputStream.write((int)ParameterOptions.USERDATA.id());
                _outputStream.write(ByteBuffer.allocate(4).putInt(userdata.length).array());
                _outputStream.write(userdata);

                if (!_all) {
                    userdataChanged = false;
                }
            }
        }
        else if (userdataChanged) {

            _outputStream.write((int)ParameterOptions.USERDATA.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());

            userdataChanged = false;
        }

        //
        // userid
        //
        if (userid != null) {

            if (_all || useridChanged || initialWrite) {

                _outputStream.write((int)ParameterOptions.USERID.id());
                RCPParser.writeTinyString(userid, _outputStream);

                if (!_all) {
                    useridChanged = false;
                }
            }
        }
        else if (useridChanged) {

            _outputStream.write((int)ParameterOptions.USERID.id());
            RCPParser.writeTinyString("", _outputStream);

            useridChanged = false;
        }

        //
        // readonly
        //
        if (readonly != null) {

            if (_all || readonlyChanged || initialWrite) {

                _outputStream.write((int)ParameterOptions.READONLY.id());
                _outputStream.write(readonly ? 1 : 0);

                if (!_all) {
                    readonlyChanged = false;
                }
            }
        }
        else if (readonlyChanged) {

            _outputStream.write((int)ParameterOptions.READONLY.id());
            _outputStream.write(0);

            readonlyChanged = false;
        }

        dirtyOptionsMap.clear();
    }

    @Override
    public final void write(final OutputStream _outputStream, final boolean _all) throws
                                                                            IOException,
                                                                            RCPException {

        // write mandatory id
        writeId(id, _outputStream);

        // write mandatory typeDefinition
        typeDefinition.write(_outputStream, _all);

        writeOptions(_outputStream, _all);

        // finalize parameter with terminator
        _outputStream.write(RCPParser.TERMINATOR);

        if (!_all) {
            initialWrite = false;
        }
    }

    public void update(final IParameter _parameter) throws RCPException {

        boolean changed = false;

        // check id
        if (_parameter.getId() != id) {
            throw new RCPException("id missmatch");
        }

        // set fields directly, no change-flag ist set!

        if (_parameter.getLabel() != null) {
            label = _parameter.getLabel();
            changed = true;
        }

        final Set<String> label_keys = _parameter.getLabelLanguages();
        if ((label_keys != null) &&
            ((languageLabels.size() != label_keys.size()) ||
             !languageLabels.isEmpty()))
        {
            languageLabels.clear();
            for (final String key : label_keys) {
                languageLabels.put(key, _parameter.getLanguageLabel(key));
            }
            changed = true;
        }

        if (_parameter.getDescription() != null) {
            description = _parameter.getDescription();
            changed = true;
        }

        final Set<String> desc_keys = _parameter.getDescriptionLanguages();
        if ((desc_keys != null) &&
            ((languageDescriptions.size() != desc_keys.size()) ||
             !languageDescriptions.isEmpty()))
        {
            languageDescriptions.clear();
            for (final String key : desc_keys) {
                languageDescriptions.put(key, _parameter.getLanguageDescription(key));
            }
            changed = true;
        }

        if (_parameter.getTags() != null) {
            tags = _parameter.getTags();
            changed = true;
        }

        if (_parameter.getOrder() != null) {
            order = _parameter.getOrder();
            changed = true;
        }


        if (_parameter.getParent() != null) {
            parent = _parameter.getParent();
            changed = true;
            // TODO: test
        }

        // TODO: widget

        if (_parameter.getUserdata() != null) {
            userdata = _parameter.getUserdata();
            changed = true;
        }

        if (_parameter.getUserid() != null) {
            userid = _parameter.getUserid();
            changed = true;
        }

        if (changed) {
            callUpdateListener();
        }
    }

    @Override
    public void dump() {

        System.out.println("--- " + id + " ---");
        System.out.println("type:\t\t\t" + typeDefinition.getDatatype().name());
        System.out.println("label:\t\t\t" + label);
        System.out.println("description:\t" + description);
        System.out.println("tags:\t\t\t" + tags);
        System.out.println("order:\t\t\t" + order);
        System.out.println("parent:\t\t\t" + (parent != null ? parent.getId() : "-"));
        System.out.println("userdata:\t\t" + userdata);
        System.out.println("userid:\t\t\t" + userid);

        if (widget != null) {
            widget.dump();
        }

        typeDefinition.dump();
    }

    //------------------------------------------------------------
    // update listener
    @Override
    public void addUpdateListener(final PARAMETER_UPDATED _listener) {

        parameterUpdateListener.add(_listener);
    }

    @Override
    public void removeUpdateListener(final PARAMETER_UPDATED _listener) {

        parameterUpdateListener.remove(_listener);
    }

    @Override
    public void clearUpdateListener() {

        parameterUpdateListener.clear();
    }

    private void callUpdateListener() {

        for (final PARAMETER_UPDATED listener : parameterUpdateListener) {
            listener.updated(this);
        }
    }

    // value update listener
    @Override
    public void addValueUpdateListener(final PARAMETER_VALUE_UPDATED _listener) {

        valueUpdateListener.add(_listener);
    }

    @Override
    public void removeValueUpdateListener(final PARAMETER_VALUE_UPDATED _listener) {

        valueUpdateListener.remove(_listener);
    }

    @Override
    public void clearValueUpdateListener() {

        valueUpdateListener.clear();
    }

    protected void callValueUpdateListener() {

        for (final PARAMETER_VALUE_UPDATED listener : valueUpdateListener) {
            listener.valueUpdated(this);
        }
    }


    //------------------------------------------------------------
    //------------------------------------------------------------

    @Override
    public short getId() {

        return id;
    }

    @Override
    public ITypeDefinition getTypeDefinition() {

        return typeDefinition;
    }

    @Override
    public String getLabel() {

        return label;
    }

    @Override
    public void setLabel(final String _label) {

        if ((label == _label) || ((label != null) && label.equals(_label))) {
            return;
        }

        label = _label;
        labelChanged = true;
        dirtyOptionsMap.put(ParameterOptions.LABEL, true);

        setDirty();
    }

    @Override
    public Set<String> getLabelLanguages() {
        return languageLabels.keySet();
    }

    @Override
    public String getLanguageLabel(final String _code) {
        return languageLabels.get(_code);
    }

    @Override
    public void clearLanguageLabel() {

        languageLabels.clear();
        labelChanged = true;
        dirtyOptionsMap.put(ParameterOptions.LABEL, true);

        setDirty();
    }

    @Override
    public void setLanguageLabel(final String _code, final String _label) {

        languageLabels.put(_code, _label);
        labelChanged = true;
        dirtyOptionsMap.put(ParameterOptions.LABEL, true);

        setDirty();
    }

    @Override
    public void removeLanguageLabel(final String _code) {

        languageLabels.remove(_code);
        labelChanged = true;
        dirtyOptionsMap.put(ParameterOptions.LABEL, true);

        setDirty();
    }

    @Override
    public String getDescription() {

        return description;
    }

    @Override
    public void setDescription(final String _description) {

        if ((description == _description) || ((description != null) && description.equals(
                _description))) {
            return;
        }

        description = _description;
        descriptionChanged = true;
        dirtyOptionsMap.put(ParameterOptions.DESCRIPTION, true);

        setDirty();
    }

    @Override
    public Set<String> getDescriptionLanguages() {
        return languageDescriptions.keySet();
    }

    @Override
    public String getLanguageDescription(final String _code) {
        return languageDescriptions.get(_code);
    }

    @Override
    public void clearLangaugeDescription() {
        languageDescriptions.clear();
        descriptionChanged = true;
        dirtyOptionsMap.put(ParameterOptions.DESCRIPTION, true);

        setDirty();
    }

    @Override
    public void setLanguageDescription(final String _code, final String _label) {

        languageDescriptions.put(_code, _label);
        descriptionChanged = true;
        dirtyOptionsMap.put(ParameterOptions.DESCRIPTION, true);

        setDirty();
    }

    @Override
    public void removeLanguageDescription(final String _code) {

        languageDescriptions.remove(_code);
        descriptionChanged = true;
        dirtyOptionsMap.put(ParameterOptions.DESCRIPTION, true);

        setDirty();
    }

    @Override
    public String getTags() {

        return tags;
    }

    @Override
    public void setTags(final String _tags) {

        if ((tags != null) && (tags == _tags)) {
            return;
        }

        tags = _tags;
        tagsChanged = true;
        dirtyOptionsMap.put(ParameterOptions.TAGS, true);

        setDirty();
    }

    @Override
    public Integer getOrder() {

        return order;
    }

    @Override
    public void setOrder(final int _order) {

        if ((order != null) && (order == _order)) {
            return;
        }

        order = _order;
        orderChanged = true;
        dirtyOptionsMap.put(ParameterOptions.ORDER, true);

        setDirty();
    }

    @Override
    public GroupParameter getParent()
    {
        return parent;
    }

    @Override
    public void setParent(final GroupParameter _parent)
    {
        if ((parent == _parent) || ((parent != null) && parent.equals(_parent)))
        {
            return;
        }

        synchronized (this)
        {
            if (parent != null)
            {
                parent.removeChild(this);
            }

            parent = _parent;
            parentChanged = true;
            dirtyOptionsMap.put(ParameterOptions.PARENTID, true);

            if (parent != null)
            {
                parent.addChild(this);
            }
            else
            {
                parameterManager.getRootGroup().addChild(this);
            }

            setDirty();
        }
    }

    /**
     * try to resolve all unresolved parents
     */
    public void resolvePendingParents() {

        if (parameterManager == null) {
            return;
        }

        ArrayList<Short> resolved_ids = new ArrayList<Short>();

        for (final Short unresolvedParentId : pendingParentIds) {
            final IParameter group = parameterManager.getParameter(unresolvedParentId);
            if (group instanceof GroupParameter) {
                setParent((GroupParameter)group);
                resolved_ids.add(unresolvedParentId);
            }
        }

        pendingParentIds.removeAll(resolved_ids);
    }

    public boolean hasPendingParents() {
        return !pendingParentIds.isEmpty();
    }

    @Override
    public Widget getWidget() {
        return widget;
    }

    @Override
    public void setWidget(final Widget _widget) {

        if (widget != null) {
            widget.setParameter(null);
        }

        widget = _widget;
        widgetChanged = true;
        dirtyOptionsMap.put(ParameterOptions.WIDGET, true);

        widget.setParameter(this);

        setDirty();
    }

    @Override
    public String getUserid() {

        return userid;
    }

    @Override
    public void setUserid(final String _userid) {

        if ((userid == _userid) || ((userid != null) && userid.equals(_userid))) {
            return;
        }

        userid = _userid;
        useridChanged = true;
        dirtyOptionsMap.put(ParameterOptions.USERID, true);

        setDirty();
    }

    @Override
    public byte[] getUserdata() {

        return userdata;
    }

    @Override
    public void setUserdata(final byte[] _userdata) {

        if ((userdata == _userdata)) {
            return;
        }

        userdata = _userdata;
        userdataChanged = true;
        dirtyOptionsMap.put(ParameterOptions.USERDATA, true);

        setDirty();
    }

    @Override
    public boolean getReadonly() {
        return (readonly != null) && readonly.booleanValue();
    }

    @Override
    public void setReadonly(final boolean _readonly) {

        if (((readonly != null) && (readonly.booleanValue() == (_readonly)))) {
            return;
        }

        readonly = _readonly;
        readonlyChanged = true;
        dirtyOptionsMap.put(ParameterOptions.READONLY, true);

        setDirty();
    }

    @Override
    public void setManager(final IParameterManager _model) {

        parameterManager = _model;
    }

    @Override
    public void setDirty() {

        if (parameterManager != null) {
            parameterManager.setParameterDirty(this);
        }
    }
}
