package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.*;
import org.rabbitcontrol.rcp.model.interfaces.*;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;
import org.rabbitcontrol.rcp.model.types.ArrayDefinition;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;

public abstract class Parameter implements IParameter, IParameterChild {

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

            // create ArrayDefinition
            final ArrayDefinition<?> array_def = ArrayDefinition.parse(_io);

            param = ParameterFactory.createArrayParameter(parameter_id, array_def);

            // !! type definition options already parsed

        } else if (datatype == Datatype.DYNAMIC_ARRAY) {

            // read mandatory sub-type
            param = null;

        } else {
            // implicitly create typeDefinition
            param = (Parameter)ParameterFactory.createParameter(parameter_id, datatype);

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
    private boolean labelChanged;

    protected String description;
    private boolean descriptionChanged;

    protected String tags;
    private boolean tagsChanged;

    protected Integer order;
    private boolean orderChanged;

    @Getter
    private GroupParameter parent;
    private boolean        parentChanged;

    // this should rather be a Parameter??
    // widget
    @Getter
    protected byte[] userdata;
    private boolean userdataChanged;

    @Getter
    private String userid;
    private boolean useridChanged;

    private IRcpModel model;

    //------------------------
    // change listener
    private final Set<LABEL_CHANGED> labelChangeListener = new ConcurrentSet<LABEL_CHANGED>();

    private final Set<DESCRIPTION_CHANGED>
            descriptionChangeListener
            = new ConcurrentSet<DESCRIPTION_CHANGED>();

    private final Set<ORDER_CHANGED> orderChangeListener = new ConcurrentSet<ORDER_CHANGED>();

    private final Set<USERDATA_CHANGED> userdataChangeListener = new ConcurrentSet<USERDATA_CHANGED>();

    private final Set<PARENT_CHANGED> parentChangeListener = new ConcurrentSet<PARENT_CHANGED>();


    //------------------------------------------------------------
    //------------------------------------------------------------
    public Parameter(final short _id, final TypeDefinition _typeDefinition) {

        id = _id;
        typeDefinition = _typeDefinition;
        typeDefinition.setParameter(this);
    }

    public IParameter cloneEmpty() {
        return ParameterFactory.createParameter(id, typeDefinition.getDatatype());
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
                    final TinyString tinyString = new TinyString(_io);
                    setLabel(tinyString.data());
                }
                    break;

                case DESCRIPTION: {
                    final ShortString shortString = new ShortString(_io);
                    setDescription(shortString.data());
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
                    ByteBuffer parent_id = ByteBuffer.wrap(new Id(_io).data());
                    if (model != null) {
                        IParameter parent = model.getParameter(parent_id);
                        try {
                            setParent((GroupParameter)parent);
                        } catch (ClassCastException _e) {
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


    @Override
    public void write(final OutputStream _outputStream, final boolean all) throws IOException {

        // write options

        //
        // label
        //
        if (label != null) {

            if (all || labelChanged) {

                _outputStream.write((int)ParameterOptions.LABEL.id());
                RCPParser.writeTinyString(label, _outputStream);

                // leave changed flag untouched, if writing all
                if (!all) {
                    labelChanged = false;
                }
            }


        } else if (labelChanged) {
            // send default value
            _outputStream.write((int)ParameterOptions.LABEL.id());
            RCPParser.writeTinyString("", _outputStream);

            labelChanged = false;
        }

        //
        // description
        //
        if (description != null) {

            if (all || descriptionChanged) {

                _outputStream.write((int)ParameterOptions.DESCRIPTION.id());
                RCPParser.writeShortString(description, _outputStream);

                if (!all) {
                    descriptionChanged = false;
                }
            }
        } else if (descriptionChanged) {

            _outputStream.write((int)ParameterOptions.DESCRIPTION.id());
            RCPParser.writeShortString("", _outputStream);

            descriptionChanged = false;
        }

        //
        // tags
        //
        if (tags != null) {

            if (all || tagsChanged) {

                _outputStream.write((int)ParameterOptions.TAGS.id());
                RCPParser.writeTinyString(tags, _outputStream);

                if (!all) {
                    tagsChanged = false;
                }
            }
        } else if (tagsChanged) {

            _outputStream.write((int)ParameterOptions.TAGS.id());
            RCPParser.writeTinyString("", _outputStream);

            tagsChanged = false;
        }

        //
        // order
        //
        if (order != null) {

            if (all || orderChanged) {

                _outputStream.write((int)ParameterOptions.ORDER.id());
                _outputStream.write(ByteBuffer.allocate(4).putInt(order).array());

                if (!all) {
                    orderChanged = false;
                }
            }
        } else if (orderChanged) {

            _outputStream.write((int)ParameterOptions.ORDER.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());

            orderChanged = false;
        }


        //
        // parentId
        //
        if (parent != null) {

            if (all || parentChanged) {

                _outputStream.write((int)ParameterOptions.PARENTID.id());
                writeId(parent.getId(), _outputStream);

                if (!all) {
                    parentChanged = false;
                }
            }
        } else if (parentChanged) {

            _outputStream.write((int)ParameterOptions.PARENTID.id());
            writeId((short)0, _outputStream);

            parentChanged = false;
        }

        // TODO: write widget


        //
        // userdata
        //
        if (userdata != null) {

            if (all || userdataChanged) {

                _outputStream.write((int)ParameterOptions.USERDATA.id());
                _outputStream.write(ByteBuffer.allocate(4).putInt(userdata.length).array());
                _outputStream.write(userdata);

                if (!all) {
                    userdataChanged = false;
                }
            }
        } else if (userdataChanged) {

            _outputStream.write((int)ParameterOptions.USERDATA.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(0).array());

            userdataChanged = false;
        }


        //
        // userid
        //
        if (userid != null) {

            if (all || useridChanged) {

                _outputStream.write((int)ParameterOptions.USERID.id());
                RCPParser.writeTinyString(userid, _outputStream);

                if (!all) {
                    useridChanged = false;
                }
            }
        } else if (useridChanged) {

            _outputStream.write((int)ParameterOptions.USERID.id());
            RCPParser.writeTinyString("", _outputStream);

            useridChanged = false;
        }
    }


    public void update(final IParameter _parameter) {

        // set fields directly, no change-flag ist set!

        if (_parameter.getLabel() != null) {
            label = _parameter.getLabel();
        }

        if (_parameter.getDescription() != null) {
            description = _parameter.getDescription();
        }

        if (_parameter.getTags() != null) {
            tags = _parameter.getTags();
        }

        if (_parameter.getOrder() != null) {
            order = _parameter.getOrder();
        }

        if (_parameter.getParent() != null) {
            parent = _parameter.getParent();

            // TODO: resolve parent parameter
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
    public String getDescription() {

        return description;
    }

    @Override
    public void setDescription(final String _description) {

        if ((description == _description) ||
            ((description != null) && description.equals(_description))) {
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
    public void setParent(final GroupParameter _parent) {

        if ((parent == _parent) || ((parent != null) && parent.equals(_parent))) {
            return;
        }

        if (parent != null) {
            parent.removeChild(this);
        }

        parent  = _parent;
        parentChanged = true;

        setDirty();
    }

//    @Override
//    public byte[] getUserdata() {
//
//        return userdata;
//    }

    @Override
    public void setUserid(final String _userid) {

        if ((userid == _userid) || ((userid != null) && userid.equals(_userid))) {
            return;
        }

        userid  = _userid;
        useridChanged = true;

        setDirty();
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
    public void setRcpModel(IRcpModel _model) {
        model = _model;
    }

    @Override
    public void setDirty() {
        if (model != null) {
            model.setDirtyParameter(this);
        }
    }
}
