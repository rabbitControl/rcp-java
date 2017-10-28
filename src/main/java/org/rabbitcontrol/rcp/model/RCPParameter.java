package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import io.netty.util.internal.ConcurrentSet;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import org.rabbitcontrol.rcp.model.gen.RcpTypes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by inx on 13/06/17.
 */
public class RCPParameter<T> implements RCPWritable {

    // TODO:
    // update listener

    //------------------------------------------------------------
    //
    public static RCPParameter<?> parse(final KaitaiStream _io) throws
                                                                RCPUnsupportedFeatureException,
                                                                RCPDataErrorException {

        // get mandatory id
        final int parameter_id = _io.readS4be();
        // get mandatory type
        final RCPTypeDefinition<?> type_definition = RCPTypeDefinition.parse(_io);

        final RCPParameter<?> parameter = new RCPParameter(parameter_id, type_definition);

        // get options from the stream
        while (true) {

            // get data-id
            int property_id = _io.readU1();

            if (property_id == RCPParser.TERMINATOR) {
                // terminator
                break;
            }

            final RcpTypes.Parameter property = RcpTypes.Parameter.byId(property_id);

            if (property == null) {
                break;
            }

            switch (property) {
                case VALUE:
                    System.out.println("we should set the value...");

                    RcpTypes.Datatype d = type_definition.getTypeid();

                    switch (d) {

                        case BOOLEAN:
                            ((RCPParameter<Boolean>)parameter).setValue(_io.readS1() > 0);
                            break;
                        case INT8:
                            ((RCPParameter<Byte>)parameter).setValue(_io.readS1());
                            break;
                        case UINT8:
                            ((RCPParameter<Short>)parameter).setValue((short)_io.readU1());
                            break;
                        case INT16:
                            ((RCPParameter<Short>)parameter).setValue(_io.readS2be());
                            break;
                        case UINT16:
                            ((RCPParameter<Integer>)parameter).setValue(_io.readU2be());
                            break;
                        case INT32:
                            ((RCPParameter<Integer>)parameter).setValue(_io.readS4be());
                            break;
                        case UINT32:
                            ((RCPParameter<Long>)parameter).setValue(_io.readU4be());
                            break;
                        case INT64:
                            ((RCPParameter<Long>)parameter).setValue(_io.readS8be());
                            break;
                        case UINT64:
                            ((RCPParameter<Long>)parameter).setValue(_io.readU8be());
                            break;
                        case FLOAT32:
                            ((RCPParameter<Float>)parameter).setValue(_io.readF4be());
                            break;
                        case FLOAT64:
                            ((RCPParameter<Double>)parameter).setValue(_io.readF8be());
                            break;
                        case TINY_STRING:
                            break;
                        case SHORT_STRING:
                            break;
                        case STRING:
                            final RcpTypes.LongString longString = new RcpTypes.LongString(_io);
                            ((RCPParameter<String>)parameter).setValue(longString.data());
                            break;
                    }

                    break;

                case LABEL:
                    final RcpTypes.TinyString tinyString = new RcpTypes.TinyString(_io);
                    parameter.setLabel(tinyString.data());
                    break;

                case DESCRIPTION:
                    final RcpTypes.ShortString shortString = new RcpTypes.ShortString(_io);
                    parameter.setDescription(shortString.data());
                    break;

                case ORDER:
                    parameter.setOrder(_io.readS4be());
                    break;

                case PARENT:
                    parameter.setParentId(_io.readU4be());
                    break;

                case WIDGET:
                    // skip...
                    throw new RCPUnsupportedFeatureException();

                case USERDATA:
                    final RcpTypes.Userdata ud = new RcpTypes.Userdata(_io);
                    parameter.setUserdata(ud.data());
                    break;
            }

        }

        return parameter;
    }

    //------------------------------------------------------------
    // interfaces
    public interface VALUE_CHANGED<T> {

        void valueChanged(final T newValue, final T oldValue);
    }

    public interface LABEL_CHANGED {

        void labelChanged(final String newValue, final String oldValue);
    }

    public interface DESCRIPTION_CHANGED {

        void descriptionChanged(final String newValue, final String oldValue);
    }

    public interface ORDER_CHANGED {

        void orderChanged(final int newValue, final int oldValue);
    }

    public interface USERDATA_CHANGED {

        void userdataChanged(final byte[] newValue, final byte[] oldValue);
    }

    public interface PARENT_CHANGED {

        void parentChanged(final long newValue, final long oldValue);
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    private final long id;

    private final RCPTypeDefinition<T> type;

    // optional
    private T value;

    private String label;

    private String description;

    private Integer order;

    // TODO:
    // this should rather be a Parameter??
    private Long                  parentId;

    private RCPParameter<?>       parent;

    private List<RCPParameter<?>> children;

    // widget

    private byte[] userdata;

    //------------------------
    // change listener

    private Set<VALUE_CHANGED<T>> valueChangeListener = new ConcurrentSet<VALUE_CHANGED<T>>();

    private Set<LABEL_CHANGED> labelChangeListener = new ConcurrentSet<LABEL_CHANGED>();

    private Set<DESCRIPTION_CHANGED>
            descriptionChangeListener
            = new ConcurrentSet<DESCRIPTION_CHANGED>();

    private Set<ORDER_CHANGED> orderChangeListener = new ConcurrentSet<ORDER_CHANGED>();

    private Set<USERDATA_CHANGED> userdataChangeListener = new ConcurrentSet<USERDATA_CHANGED>();

    private Set<PARENT_CHANGED> parentChangeListener = new ConcurrentSet<PARENT_CHANGED>();

    //------------------------------------------------------------
    //
    public RCPParameter(final int _id, final RCPTypeDefinition<T> _type) {

        id = _id;
        type = _type;
    }

    public RCPParameter<T> cloneEmpty() {

        return new RCPParameter<T>((int)id, type.cloneEmpty());
    }

    //------------------------------------------------------------
    // listener

    /*
        VALUE_CHANGED listener
     */
    public void addValueChangeListener(final VALUE_CHANGED<T> _listener) {

        if (!valueChangeListener.contains(_listener)) {
            valueChangeListener.add(_listener);
        }
    }

    public void removeValueChangeListener(final VALUE_CHANGED<T> _listener) {

        if (valueChangeListener.contains(_listener)) {
            valueChangeListener.remove(_listener);
        }
    }

    public void clearValueChangeListener() {

        valueChangeListener.clear();
    }

    /*
        LABEL_CHANGED listener
     */
    public void addLabelChangeListener(final LABEL_CHANGED _listener) {

        if (!valueChangeListener.contains(_listener)) {
            labelChangeListener.add(_listener);
        }
    }

    public void removeLabelChangeListener(final LABEL_CHANGED _listener) {

        if (valueChangeListener.contains(_listener)) {
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
    //
    @Override
    public void write(OutputStream _outputStream) throws IOException {

        // write mandatory id
        _outputStream.write(ByteBuffer.allocate(4).putInt((int)id).array());

        // write mandatory type
        type.write(_outputStream);

        // write all optionals
        if (value != null) {
            _outputStream.write((int)RcpTypes.Parameter.VALUE.id());
            type.writeValue(value, _outputStream);
        }

        if (label != null) {
            _outputStream.write((int)RcpTypes.Parameter.LABEL.id());
            RCPParser.writeTinyString(label, _outputStream);
        }

        if (description != null) {
            _outputStream.write((int)RcpTypes.Parameter.DESCRIPTION.id());
            RCPParser.writeShortString(description, _outputStream);
        }

        if (order != null) {
            _outputStream.write((int)RcpTypes.Parameter.ORDER.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(order).array());
        }

        if (userdata != null) {
            _outputStream.write((int)RcpTypes.Parameter.USERDATA.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(userdata.length).array());
            _outputStream.write(userdata);
        }

        // finalize parameter with terminator
        _outputStream.write(RCPParser.TERMINATOR);
    }

    //------------------------------------------------------------
    //
    public void update(final RCPParameter<?> _other) {

        if (id != _other.getId()) {
            System.err.println("don't updated unmatching id");
            return;
        }

        if ((_other.getType() != null) && (type.getTypeid() != _other.getType().getTypeid())) {
            System.err.println("not updated unmatching types: " + type + " != " + _other.type);
            return;
        }

        // try our best to match the values
        if (_other.getValue() != null) {

            T newValue = value;

            try {
                newValue = (T)value.getClass().cast(_other.getValue());
            }
            catch (final ClassCastException e) {

                if ((value instanceof Number) && (_other.getValue() instanceof Number)) {

                    if (value instanceof Integer) {
                        newValue = (T)Integer.valueOf(((Number)_other.getValue()).intValue());
                    }
                    else if (value instanceof Short) {
                        newValue = (T)Short.valueOf(((Number)_other.getValue()).shortValue());
                    }
                    else if (value instanceof Byte) {
                        newValue = (T)Byte.valueOf(((Number)_other.getValue()).byteValue());
                    }
                    else if (value instanceof Long) {
                        newValue = (T)Long.valueOf(((Number)_other.getValue()).longValue());
                    }
                    else if (value instanceof Float) {
                        newValue = (T)new Float(((Number)_other.getValue()).floatValue());
                    }
                    else if (value instanceof Double) {
                        newValue = (T)new Double(((Number)_other.getValue()).doubleValue());
                    }

                }
                else if (value instanceof Map) {

                    if (_other.getValue() instanceof Map) {

                        ((Map)newValue).clear();

                        for (final Object k : ((Map)_other.getValue()).keySet()) {
                            ((Map)newValue).put(k, ((Map)_other.getValue()).get(k));
                        }

                        //                        System.out.println("updated map");

                        //                            value = (Map)_other.value;

                    }
                    else {
                        System.err.println("other not of map");
                    }

                }
                else {
                    System.err.println("cannot updated of from type: " +
                                       value.getClass().getName() +
                                       " other: " +
                                       _other.value.getClass().getName());
                }
            }

            setValue(newValue);
        }

        if (_other.getLabel() != null) {
            setLabel(_other.getLabel());
        }

        if (_other.getDescription() != null) {
            description = _other.getDescription();
        }

        if (_other.getOrder() != null) {
            order = _other.getOrder();
        }

        if (_other.getUserdata() != null) {
            userdata = _other.getUserdata();
        }
    }

    public T getValue() {

        if (value == null) {
            return type.getDefaultValue();
        }

        return value;
    }

    public void setValue(final T _value) {

        if ((value != null) && value.equals(_value)) {
            return;
        }

        T oldValue = value;
        value = _value;

        // update listener
        if (!valueChangeListener.isEmpty()) {
            for (final VALUE_CHANGED<T> listener : valueChangeListener) {
                listener.valueChanged(value, oldValue);
            }
        }
    }

    public String getLabel() {

        return label;
    }

    public void setLabel(final String _label) {

        String oldLabel = label;
        label = _label;

        // update label-changed listener
        if (!labelChangeListener.isEmpty()) {
            for (final LABEL_CHANGED listener : labelChangeListener) {
                listener.labelChanged(label, oldLabel);
            }
        }
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(final String _description) {

        final String oldValue = description;
        description = _description;

        // update description-changed listener
        if (!descriptionChangeListener.isEmpty()) {
            for (final DESCRIPTION_CHANGED listener : descriptionChangeListener) {
                listener.descriptionChanged(description, oldValue);
            }
        }
    }

    public Integer getOrder() {

        return order;
    }

    public void setOrder(final int _order) {

        final int oldValue = order;
        order = _order;

        if (!orderChangeListener.isEmpty()) {
            for (final ORDER_CHANGED listener : orderChangeListener) {
                listener.orderChanged(order, oldValue);
            }
        }
    }

    public Long getParentId() {

        return parentId;
    }

    public void setParentId(final long _parentId) {

        final long oldValue = parentId;
        parentId = _parentId;

        // TODO
        // get RCPParameter for that id
        // if parameter does not exist, install a listener so we get called when that parameter
        // gets added

        if (!parentChangeListener.isEmpty()) {
            for (final PARENT_CHANGED listener : parentChangeListener) {
                listener.parentChanged(parentId, oldValue);
            }
        }
    }

    public byte[] getUserdata() {

        return userdata;
    }

    public void setUserdata(final byte[] _userdata) {

        final byte[] oldValue = userdata;
        userdata = _userdata;

        if (!userdataChangeListener.isEmpty()) {
            for (final USERDATA_CHANGED listener : userdataChangeListener) {
                listener.userdataChanged(userdata, oldValue);
            }
        }
    }

    public long getId() {

        return id;
    }

    public RCPTypeDefinition<T> getType() {

        return type;
    }

    public void dump() {

        System.out.println("--- " + id + " ---");
        System.out.println("type:\t\t\t" + type.getTypeid().name());
        System.out.println("value:\t\t\t" + value);
        System.out.println("label:\t\t\t" + label);
        System.out.println("description:\t" + description);
        System.out.println("userdata:\t\t" + userdata);
        System.out.println();

    }
}
