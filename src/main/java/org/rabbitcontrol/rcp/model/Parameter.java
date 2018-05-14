package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.model.interfaces.*;
import org.rabbitcontrol.rcp.model.parameter.ArrayParameter;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;
import org.rabbitcontrol.rcp.model.types.ArrayDefinitionFixed;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.*;

public abstract class Parameter implements IParameter, IParameterChild {

    private static final String LANGUAGE_ANY_STR = "any";

    private static final byte[] LANGUAGE_ANY     = LANGUAGE_ANY_STR.getBytes();

    //------------------------------------------------------------
    //------------------------------------------------------------
    public static Parameter parse(final KaitaiStream _io) throws
                                                          RCPUnsupportedFeatureException,
                                                          RCPDataErrorException {

        // get mandatory id
        final short parameter_id = _io.readS2be();

        // read mandatory typeDefinition
        final Datatype datatype = Datatype.byId(_io.readU1());

        if (datatype == null) {
            throw new RCPDataErrorException();
        }

        final Parameter param;

        // handle certain datatypes...
        if (datatype == Datatype.FIXED_ARRAY) {

            // create ArrayDefinitionFixed
            final ArrayDefinitionFixed<?, ?> array_def = ArrayDefinitionFixed.parse(_io);

            param = ArrayParameter.createFixed(parameter_id, array_def, array_def.getSubtype());
            // !! type definition options already parsed
        }
        else if (datatype == Datatype.DYNAMIC_ARRAY) {

            // read mandatory sub-type
            param = null;

        }
        else {
            // implicitly create typeDefinition
            param = (Parameter)RCPFactory.createParameter(parameter_id, datatype);

            // parse type options and
            if (param != null) {
                param.parseTypeOptions(_io);
            }
        }

        if (param != null) {
            // !! parse only parameter options
            param.parseOptions(_io);
            return param;
        }

        throw new RCPUnsupportedFeatureException("no such feature: " + datatype);
    }

    //------------------------------------------------------------
    public interface LABEL_CHANGED {

        void labelChanged(final String newValue);
    }

    public interface DESCRIPTION_CHANGED {

        void descriptionChanged(final String newValue);
    }

    public interface ORDER_CHANGED {

        void orderChanged(final int newValue);
    }

    public interface PARENT_CHANGED {

        void parentChanged(final ByteBuffer newValue);
    }

    public interface USERDATA_CHANGED {

        void userdataChanged(final byte[] newValue);

    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    protected final short id;

    protected final TypeDefinition typeDefinition;

    // optional
    protected String label;

    protected Map<String, String> languageLabels = new HashMap<String, String>();

    private   boolean             labelChanged   = false;

    protected String description;

    protected Map<String, String> languageDescriptions = new HashMap<String, String>();

    private   boolean             descriptionChanged   = false;

    protected String tags;

    private boolean tagsChanged = false;

    protected Integer order;

    private boolean orderChanged = false;

    private GroupParameter parent;

    private boolean parentChanged = false;

    // widget
    private Short widgetType;

    private boolean widgetTypeChanged = false;

    protected byte[] userdata;

    private boolean userdataChanged = false;

    private String userid;

    private boolean useridChanged = false;

    private IParameterManager parameterManager;

    protected boolean initialWrite = true; // one-time-flag

    //------------------------
    // change listener
    private final Set<LABEL_CHANGED> labelChangeListener = new HashSet<LABEL_CHANGED>();

    private final Set<DESCRIPTION_CHANGED>
            descriptionChangeListener
            = new HashSet<DESCRIPTION_CHANGED>();

    private final Set<ORDER_CHANGED> orderChangeListener = new HashSet<ORDER_CHANGED>();

    private final Set<USERDATA_CHANGED> userdataChangeListener = new HashSet<USERDATA_CHANGED>();

    private final Set<PARENT_CHANGED> parentChangeListener = new HashSet<PARENT_CHANGED>();

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

    protected abstract boolean handleOption(final int _propertyId, final KaitaiStream _io);

    private void parseTypeOptions(final KaitaiStream _io) throws RCPDataErrorException {

        typeDefinition.parseOptions(_io);
    }

    private void parseOptions(final KaitaiStream _io) throws
                                                      RCPDataErrorException,
                                                      RCPUnsupportedFeatureException {

        // !! attention: this only parses parameter options
        // !! parse typedefinition options before

        // get options from the stream
        while (true) {

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
                            System.out.println("any language label: " + label);
                            setLabel(label);
                        }
                        else {
                            System.out.println("setting language label " +
                                               lang_code +
                                               " : " +
                                               label);
                            setLanguageLabel(lang_code, label);
                        }

                        current = _io.pos();
                        ppeekk = _io.readS1();
                    }

                    //                    final TinyString tinyString = new TinyString(_io);
                    //                    setLabel(tinyString.data());
                }
                break;

                case DESCRIPTION: {

                    int  current = _io.pos();
                    byte ppeekk  = _io.readS1();

                    while (ppeekk != 0) {

                        // rewind one
                        _io.seek(current);

                        final String lang_code = new String(_io.readBytes(LANGUAGE_ANY.length));
                        final String label     = new ShortString(_io).data();

                        if (LANGUAGE_ANY_STR.equals(lang_code)) {
                            System.out.println("any language description: " + label);
                            setDescription(label);
                        }
                        else {
                            System.out.println("setting language description " +
                                               lang_code +
                                               " :" +
                                               " " +
                                               label);
                            setLanguageDescription(lang_code, label);
                        }

                        current = _io.pos();
                        ppeekk = _io.readS1();
                    }

                    //                    final ShortString shortString = new ShortString(_io);
                    //                    setDescription(shortString.data());
                }
                break;

                case TAGS: {
                    final TinyString tinyString = new TinyString(_io);
                    setTags(tinyString.data());
                }
                break;

                case ORDER:
                    setOrder(_io.readS4be());
                    break;

                case PARENTID: {
                    // read as id, this is correct
                    final short parent_id = _io.readS2be();
                    if (parameterManager != null) {
                        final IParameter parent = parameterManager.getParameter(parent_id);
                        try {
                            setParent((GroupParameter)parent);
                        }
                        catch (final ClassCastException _e) {
                            System.err.println("parameter not a GroupParameter!");
                        }
                    }
                }
                break;

                case USERID: {
                    final TinyString tinyString = new TinyString(_io);
                    setUserid(tinyString.data());
                }
                break;

                case WIDGET:
                    // skip...
                    throw new RCPUnsupportedFeatureException();

                case USERDATA:
                    final Userdata ud = new Userdata(_io);
                    setUserdata(ud.data());
                    break;

                // handle in specific implementations
                case VALUE:
                default:
                    if (!handleOption(property_id, _io)) {
                        throw new RCPDataErrorException();
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

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

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

        // TODO: write widget -widgetType
        if (widgetType != null) {

            if (_all || widgetTypeChanged || initialWrite) {

                _outputStream.write((int)ParameterOptions.WIDGET.id());
                _outputStream.write(ByteBuffer.allocate(2).putShort(widgetType).array());

                if (!_all) {
                    widgetTypeChanged = false;
                }
            }
        }
        else if (widgetTypeChanged) {

            _outputStream.write((int)ParameterOptions.WIDGET.id());
            _outputStream.write(ByteBuffer.allocate(2).putShort(widgetType).array());

            widgetTypeChanged = false;
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

        if (!_all) {
            initialWrite = false;
        }
    }

    public void update(final IParameter _parameter) {

        // check id
        if (_parameter.getId() != id) {
            return;
        }

        // set fields directly, no change-flag ist set!

        if (_parameter.getLabel() != null) {
            label = _parameter.getLabel();
        }

        final Set<String> label_keys = _parameter.getLabelLanguages();
        if (label_keys != null) {
            clearLanguageLabel();
            for (final String key : label_keys) {
                setLanguageLabel(key, _parameter.getLanguageLabel(key));
            }
        }

        if (_parameter.getDescription() != null) {
            description = _parameter.getDescription();
        }

        final Set<String> desc_keys = _parameter.getDescriptionLanguages();
        if (desc_keys != null) {
            clearLangaugeDescription();
            for (final String desc_key : desc_keys) {
                setLanguageDescription(desc_key, _parameter.getLanguageDescription(desc_key));
            }
        }

        if (_parameter.getTags() != null) {
            tags = _parameter.getTags();
        }

        if (_parameter.getOrder() != null) {
            order = _parameter.getOrder();
        }


        if (_parameter.getParent() != null) {
            parent = _parameter.getParent();

            // TODO: test
        }

        // TODO: widget

        if (_parameter.getUserdata() != null) {
            userdata = _parameter.getUserdata();
        }

        if (_parameter.getUserid() != null) {
            userid = _parameter.getUserid();
        }
    }

    @Override
    public void dump() {

        System.out.println("--- " + id + " ---");
        System.out.println("type:\t\t\t" + typeDefinition.getDatatype().name());
        System.out.println("label:\t\t\t" + label);
        System.out.println("description:\t" + description);
        System.out.println("tags:\t" + tags);
        System.out.println("order:\t" + order);
        System.out.println("parent:\t" + (parent != null ? parent.getId() : "-"));
        System.out.println("userdata:\t\t" + userdata);
        System.out.println("userid:\t\t" + userid);
    }

    //------------------------------------------------------------
    // listener
    /*
        LABEL_CHANGED listener
     */
    public void addLabelChangeListener(final LABEL_CHANGED _listener) {

        if (!labelChangeListener.contains(_listener)) {
            labelChangeListener.add(_listener);
        }
    }

    public void removeLabelChangeListener(final LABEL_CHANGED _listener) {

        if (labelChangeListener.contains(_listener)) {
            labelChangeListener.remove(_listener);
        }
    }

    public void clearLabelChangeListener() {

        labelChangeListener.clear();
    }

    /*
        DESCRIPTION_CHANGED listener
     */
    public void addDescriptionChangedListener(final DESCRIPTION_CHANGED _listener) {

        if (!descriptionChangeListener.contains(_listener)) {
            descriptionChangeListener.add(_listener);
        }
    }

    public void removeDescriptionChangedListener(final DESCRIPTION_CHANGED _listener) {

        if (descriptionChangeListener.contains(_listener)) {
            descriptionChangeListener.remove(_listener);
        }
    }

    public void clearDescriptionChangedListener() {

        descriptionChangeListener.clear();
    }

    /*
        ORDER_CHANGED listener
     */
    public void addOrderChangedListener(final ORDER_CHANGED _listener) {

        if (!orderChangeListener.contains(_listener)) {
            orderChangeListener.add(_listener);
        }
    }

    public void removeOrderChangedListener(final ORDER_CHANGED _listener) {

        if (orderChangeListener.contains(_listener)) {
            orderChangeListener.remove(_listener);
        }
    }

    public void clearOrderChangedListener() {

        orderChangeListener.clear();
    }

    /*
        USERDATA_CHANGED listener
     */
    public void addUserdataChangedListener(final USERDATA_CHANGED _listener) {

        if (!userdataChangeListener.contains(_listener)) {
            userdataChangeListener.add(_listener);
        }
    }

    public void removeUserdataChangedListener(final USERDATA_CHANGED _listener) {

        if (userdataChangeListener.contains(_listener)) {
            userdataChangeListener.remove(_listener);
        }
    }

    public void clearUserdataChangedListener() {

        userdataChangeListener.clear();
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

        for (final LABEL_CHANGED label_changed : labelChangeListener) {
            label_changed.labelChanged(label);
        }

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
    }

    @Override
    public void setLanguageLabel(final String _code, final String _label) {

        languageLabels.put(_code, _label);
        labelChanged = true;

        setDirty();
    }

    @Override
    public void removeLanguageLabel(final String _code) {

        languageLabels.remove(_code);
        labelChanged = true;

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

        for (final DESCRIPTION_CHANGED description_changed : descriptionChangeListener) {
            description_changed.descriptionChanged(description);
        }

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
    }

    @Override
    public void setLanguageDescription(final String _code, final String _label) {

        languageDescriptions.put(_code, _label);
        descriptionChanged = true;

        setDirty();
    }

    @Override
    public void removeLanguageDescription(final String _code) {

        languageDescriptions.remove(_code);
        descriptionChanged = true;

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

        for (final ORDER_CHANGED order_changed : orderChangeListener) {
            order_changed.orderChanged(order);
        }

        setDirty();
    }

    @Override
    public GroupParameter getParent() {

        return parent;
    }

    @Override
    public void setParent(final GroupParameter _parent) {

        if ((parent == _parent) || ((parent != null) && parent.equals(_parent))) {
            return;
        }

        final GroupParameter p = parent;

        if (parent != null) {
            parent = null;
            p.removeChild(this);
        }

        parent = _parent;
        parentChanged = true;

        setDirty();
    }

    @Override
    public void setWidgetType(final short type) {

        widgetType = type;
        widgetTypeChanged = true;
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

        setDirty();
    }

    @Override
    public byte[] getUserdata() {

        return userdata;
    }

    @Override
    public void setUserdata(final byte[] _userdata) {

        if ((userdata == _userdata) || ((userdata != null) && userdata.equals(_userdata))) {
            return;
        }

        userdata = _userdata;
        userdataChanged = true;

        for (final USERDATA_CHANGED userdata_changed : userdataChangeListener) {
            userdata_changed.userdataChanged(userdata);
        }

        setDirty();
    }

    @Override
    public void setRcpModel(final IParameterManager _model) {

        parameterManager = _model;
    }

    @Override
    public void setDirty() {

        if (parameterManager != null) {
            parameterManager.setParameterDirty(this);
        }
    }
}
