package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import io.netty.util.internal.ConcurrentSet;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.*;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.interfaces.ITypeDefinition;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;

public abstract class Parameter implements IParameter {

    //------------------------------------------------------------
    //------------------------------------------------------------
    public static Parameter parse(final KaitaiStream _io) throws
                                                          RCPUnsupportedFeatureException,
                                                          RCPDataErrorException {

        // get mandatory id
        // read as signed int, this is correct
        final int parameter_id = _io.readS4be();
        // read mandatory typeDefinition
        final Datatype datatype = Datatype.byId(_io.readU1());

        if (datatype == null) {
            throw new RCPDataErrorException();
        }

        final Parameter param = (Parameter)ParameterFactory.createParameter(parameter_id, datatype);

        if (param != null) {
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

        void parentChanged(final long newValue);
    }

    public interface USERDATA_CHANGED {
        void userdataChanged(final byte[] newValue);

    }


    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    protected final long id;

    protected final TypeDefinition typeDefinition;

    // optional
    protected String label;

    protected String description;

    protected Integer order;

    // TODO:
    // this should rather be a Parameter??
    protected Long parentId;

    protected Parameter parent;

    // widget

    protected byte[] userdata;

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
    public Parameter(final int _id, final TypeDefinition _typeDefinition) {

        id = _id;
        typeDefinition = _typeDefinition;
    }

    public IParameter cloneEmpty() {
        return ParameterFactory.createParameter((int)id, typeDefinition.getDatatype());
    }

    protected abstract boolean handleOption(final int _propertyId, final KaitaiStream _io);

    private void parseOptions(final KaitaiStream _io) throws
                                                      RCPDataErrorException,
                                                      RCPUnsupportedFeatureException {

        //!! first parse typedefinition options
        typeDefinition.parseOptions(_io);

        // get options from the stream
        while (true) {

            // get data-id
            int property_id = _io.readU1();

            if (property_id == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            final ParameterOptions option = ParameterOptions.byId(property_id);

            if (option == null) {
                break;
            }

            switch (option) {

                case LABEL:
                    final TinyString tinyString = new TinyString(_io);
                    setLabel(tinyString.data());
                    break;

                case DESCRIPTION:
                    final ShortString shortString = new ShortString(_io);
                    setDescription(shortString.data());
                    break;

                case ORDER:
                    setOrder(_io.readS4be());
                    break;

                case PARENT:
                    // read as signed int, this is correct
                    setParentId(_io.readS4be());
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

    @Override
    public void write(final OutputStream _outputStream) throws IOException {

        // write options

        if (label != null) {
            _outputStream.write((int)ParameterOptions.LABEL.id());
            RCPParser.writeTinyString(label, _outputStream);
        }

        if (description != null) {
            _outputStream.write((int)ParameterOptions.DESCRIPTION.id());
            RCPParser.writeShortString(description, _outputStream);
        }

        if (order != null) {
            _outputStream.write((int)ParameterOptions.ORDER.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(order).array());
        }

        if (parentId != null) {
            _outputStream.write((int)ParameterOptions.PARENT.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(parentId.intValue()).array());
        }

        // TODO: write widget

        if (userdata != null) {
            _outputStream.write((int)ParameterOptions.USERDATA.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(userdata.length).array());
            _outputStream.write(userdata);
        }
    }


    public void update(IParameter _parameter) {

    }

    @Override
    public void dump() {
        System.out.println("--- " + id + " ---");
        System.out.println("type:\t\t\t" + typeDefinition.getDatatype().name());
        System.out.println("label:\t\t\t" + label);
        System.out.println("description:\t" + description);
        System.out.println("order:\t" + order);
        System.out.println("parent:\t" + parentId);
        System.out.println("userdata:\t\t" + userdata);
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
    public int getId() {

        return (int)id;
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

        label = _label;

        for (final LABEL_CHANGED label_changed : labelChangeListener) {
            label_changed.labelChanged(label);
        }
    }

    @Override
    public String getDescription() {

        return description;
    }

    @Override
    public void setDescription(final String _description) {

        description = _description;

        for (final DESCRIPTION_CHANGED description_changed : descriptionChangeListener) {
            description_changed.descriptionChanged(description);
        }
    }

    @Override
    public Integer getOrder() {

        return order;
    }

    @Override
    public void setOrder(final int _order) {

        order = _order;

        for (final ORDER_CHANGED order_changed : orderChangeListener) {
            order_changed.orderChanged(order);
        }
    }

    @Override
    public Integer getParentId() {

        return (int)(long)parentId;
    }

    @Override
    public void setParentId(final int _parentId) {

        parentId = (long)_parentId;

        // TODO resolve Parent Parameter

        for (final PARENT_CHANGED parent_changed : parentChangeListener) {
            parent_changed.parentChanged(parentId);
        }
    }

    @Override
    public byte[] getUserdata() {

        return userdata;
    }

    @Override
    public void setUserdata(final byte[] _userdata) {

        userdata = _userdata;

        for (final USERDATA_CHANGED userdata_changed : userdataChangeListener) {
            userdata_changed.userdataChanged(userdata);
        }
    }
}
