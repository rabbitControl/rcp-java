package org.rabbitcontrol.rcp.model;

import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import org.rabbitcontrol.rcp.model.exceptions.RCPUnsupportedFeatureException;
import io.kaitai.struct.KaitaiStream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by inx on 13/06/17.
 */
public class RCPParameter<T> implements RCPWritable {

    //------------------------------------------------------------
    //
    public static RCPParameter<?> parse(final KaitaiStream _io) throws
                                                                RCPUnsupportedFeatureException,
                                                                RCPDataErrorException {

        // get mandatory id
        final int paramId = _io.readS4be();
        // get mandatory type
        final RCPTypeDefinition<?> type = RCPTypeDefinition.parse(_io);

        final RCPParameter<?> parameter = new RCPParameter<>(paramId, type);

        // get options from the stream
        while (true) {

            // get data-id
            int did = _io.readU1();

            if (did == RCPTypes.Packet.TERMINATOR.id()) {
                // terminator
                break;
            }

            final RCPTypes.Parameter dataid = RCPTypes.Parameter.byId(did);

            if (dataid == null) {
                break;
            }

            switch (dataid) {
                case VALUE:
                    System.out.println("we should set the value...");

                    RCPTypes.Datatype d = type.getTypeid();

                    switch (d) {

                        case BOOL:
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
                        case TSTR:
                            break;
                        case SSTR:
                            break;
                        case LSTR:
                            final RCPTypes.LongString longString = new RCPTypes.LongString(_io);
                            ((RCPParameter<String>)parameter).setValue(longString.data());
                            break;
                    }

                    break;

                case LABEL:
                    final RCPTypes.TinyString tinyString = new RCPTypes.TinyString(_io);
                    parameter.setLabel(tinyString.data());
                    break;

                case DESCRIPTION:
                    final RCPTypes.ShortString shortString = new RCPTypes.ShortString(_io);
                    parameter.setDescription(shortString.data());
                    break;

                case ORDER:
                    parameter.setOrder(_io.readS4be());
                    break;

                case WIDGET:
                    // skip...
                    throw new RCPUnsupportedFeatureException();

                case USERDATA:
                    final RCPTypes.Userdata ud = new RCPTypes.Userdata(_io);
                    parameter.setUserdata(ud.data());
                    break;
            }

        }

        return parameter;
    }

    //------------------------------------------------------------
    // mandatory
    private final long id;

    private final RCPTypeDefinition<T> type;

    // optional
    private T value;

    private String label;

    private String description;

    private Integer order;

    // widget

    private byte[] userdata;

    //------------------------------------------------------------
    //
    public RCPParameter(final int _id, final RCPTypeDefinition<T> _type) {

        id = _id;
        type = _type;
    }

    public RCPParameter<T> cloneEmpty() {

        return new RCPParameter<>((int)id, type.cloneEmpty());
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
            _outputStream.write((int)RCPTypes.Parameter.VALUE.id());
            type.writeValue(value, _outputStream);
        }

        if (label != null) {
            _outputStream.write((int)RCPTypes.Parameter.LABEL.id());
            RCPParser.writeTinyString(label, _outputStream);
        }

        if (description != null) {
            _outputStream.write((int)RCPTypes.Parameter.DESCRIPTION.id());
            RCPParser.writeShortString(description, _outputStream);
        }

        if (order != null) {
            _outputStream.write((int)RCPTypes.Parameter.ORDER.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(order).array());
        }

        if (userdata != null) {
            _outputStream.write((int)RCPTypes.Parameter.USERDATA.id());
            _outputStream.write(ByteBuffer.allocate(4).putInt(userdata.length).array());
            _outputStream.write(userdata);
        }

        // finalize parameter with terminator
        _outputStream.write((int)RCPTypes.Packet.TERMINATOR.id());
    }

    //------------------------------------------------------------
    //
    public void update(final RCPParameter<?> _other) {

        if (id != _other.getId()) {
            System.err.println("don't updated unmatching id");
            return;
        }

        if ((_other.getType() != null) && (type.getTypeid() != _other.getType().getTypeid())) {
            System.err.println("not updated unmatching types: " + type + " != " + _other.type );
            return;
        }

        // try our best to match the values
        if (_other.getValue() != null) {

            try {
                value = (T)value.getClass().cast(_other.getValue());
            }
            catch (final ClassCastException e) {

                if ((value instanceof Number) && (_other.getValue() instanceof Number)) {

                    if (value instanceof Integer) {
                        value = (T)new Integer(((Number)_other.getValue()).intValue());
                    }
                    else if (value instanceof Short) {
                        value = (T)new Short(((Number)_other.getValue()).shortValue());
                    }
                    else if (value instanceof Byte) {
                        value = (T)new Byte(((Number)_other.getValue()).byteValue());
                    }
                    else if (value instanceof Long) {
                        value = (T)new Long(((Number)_other.getValue()).longValue());
                    }
                    else if (value instanceof Float) {
                        value = (T)new Float(((Number)_other.getValue()).floatValue());
                    }
                    else if (value instanceof Double) {
                        value = (T)new Double(((Number)_other.getValue()).doubleValue());
                    }

                }
                else if (value instanceof Map) {

                    if (_other.getValue() instanceof Map) {

                        ((Map)value).clear();


                        ((Map)_other.getValue()).forEach((_o, _o2) -> {
                            ((Map)value).put(_o, _o2);
                        });

                        //                        System.out.println("updated map");

                        //                            value = (Map)_other.value;

                    } else {
                        System.err.println("other not of map");
                    }


                }
                else {
                    System.err.println("cannot updated of from type: " + value.getClass().getName
                            () + " other: " + _other.value.getClass().getName());
                }
            }
        }

//        final RCPParameter<T> other = (RCPParameter<T>)_other;

        if (_other.getLabel() != null) {
            label = _other.getLabel();
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

    public void dump() {

        System.out.println("--- " + id + " ---");
        System.out.println("type:\t\t\t" + type.getTypeid().name());
        System.out.println("value:\t\t\t" + value);
        System.out.println("label:\t\t\t" + label);
        System.out.println("description:\t" + description);
        System.out.println("userdata:\t\t" + userdata);
        System.out.println();

    }


    public T getValue() {

        if (value == null) {
            return type.getDefaultValue();
        }

        return value;
    }

    public void setValue(final T _value) {

        value = _value;
    }

    public String getLabel() {

        return label;
    }

    public void setLabel(final String _label) {

        label = _label;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(final String _description) {

        description = _description;
    }

    public Integer getOrder() {

        return order;
    }

    public void setOrder(final int _order) {

        order = _order;
    }

    public byte[] getUserdata() {

        return userdata;
    }

    public void setUserdata(final byte[] _userdata) {

        userdata = _userdata;
    }

    public long getId() {

        return id;
    }

    public RCPTypeDefinition<T> getType() {

        return type;
    }
}
