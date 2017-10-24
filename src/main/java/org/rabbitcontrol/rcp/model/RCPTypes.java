// This is a generated file! Please edit source .ksy file and use kaitai-struct-compiler to rebuild

package org.rabbitcontrol.rcp.model;

import io.kaitai.struct.KaitaiStream;
import io.kaitai.struct.KaitaiStruct;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class RCPTypes extends KaitaiStruct {
    public static RCPTypes fromFile(String fileName) throws IOException {
        return new RCPTypes(new KaitaiStream(fileName));
    }

    public enum Parameter {
        VALUE(32),
        LABEL(33),
        DESCRIPTION(34),
        ORDER(35),
        WIDGET(36),
        USERDATA(37);

        private final long id;
        Parameter(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Parameter> byId = new HashMap<Long, Parameter>(6);
        static {
            for (Parameter e : Parameter.values())
                byId.put(e.id(), e);
        }
        public static Parameter byId(long id) { return byId.get(id); }
    }

    public enum TypeNumber {
        DEFAULTVALUE(48),
        MIN(49),
        MAX(50),
        MULT(51),
        SCALE(52),
        UNIT(53);

        private final long id;
        TypeNumber(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, TypeNumber> byId = new HashMap<Long, TypeNumber>(6);
        static {
            for (TypeNumber e : TypeNumber.values())
                byId.put(e.id(), e);
        }
        public static TypeNumber byId(long id) { return byId.get(id); }
    }

    public enum TypeDefinition {
        DEFAULTVALUE(48);

        private final long id;
        TypeDefinition(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, TypeDefinition> byId = new HashMap<Long, TypeDefinition>(1);
        static {
            for (TypeDefinition e : TypeDefinition.values())
                byId.put(e.id(), e);
        }
        public static TypeDefinition byId(long id) { return byId.get(id); }
    }

    public enum Widget {
        TYPE(80),
        ENABLED(81),
        VISIBLE(82),
        LABEL_VISIBLE(83),
        VALUE_VISIBLE(84),
        LABE_POSITION(85);

        private final long id;
        Widget(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Widget> byId = new HashMap<Long, Widget>(6);
        static {
            for (Widget e : Widget.values())
                byId.put(e.id(), e);
        }
        public static Widget byId(long id) { return byId.get(id); }
    }

    public enum TypeEnum {
        DEFAULTVALUE(48),
        ENTRIES(49);

        private final long id;
        TypeEnum(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, TypeEnum> byId = new HashMap<Long, TypeEnum>(2);
        static {
            for (TypeEnum e : TypeEnum.values())
                byId.put(e.id(), e);
        }
        public static TypeEnum byId(long id) { return byId.get(id); }
    }

    public enum WidgetType {
        TEXTBOX(16),
        NUMBERBOX(17),
        BUTTON(18),
        CHECKBOX(19),
        RADIOBUTTON(20),
        SLIDER(21),
        DIAL(22),
        COLORBOX(23),
        TABLE(24),
        TREEVIEW(25),
        DROPDOWN(26),
        XYFIELD(31);

        private final long id;
        WidgetType(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, WidgetType> byId = new HashMap<Long, WidgetType>(12);
        static {
            for (WidgetType e : WidgetType.values())
                byId.put(e.id(), e);
        }
        public static WidgetType byId(long id) { return byId.get(id); }
    }

    public enum Command {
        VERSION(1),
        INIT(2),
        ADD(3),
        UPDATE(4),
        REMOVE(5);

        private final long id;
        Command(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Command> byId = new HashMap<Long, Command>(5);
        static {
            for (Command e : Command.values())
                byId.put(e.id(), e);
        }
        public static Command byId(long id) { return byId.get(id); }
    }

    public enum NumberScale {
        LIN(0),
        LOG(1),
        EXP2(2);

        private final long id;
        NumberScale(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, NumberScale> byId = new HashMap<Long, NumberScale>(3);
        static {
            for (NumberScale e : NumberScale.values())
                byId.put(e.id(), e);
        }
        public static NumberScale byId(long id) { return byId.get(id); }
    }

    public enum TypeVector {
        DEFAULTVALUE(48),
        MIN(49),
        MAX(50),
        MULT(51),
        SCALE(52),
        UNIT(53);

        private final long id;
        TypeVector(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, TypeVector> byId = new HashMap<Long, TypeVector>(6);
        static {
            for (TypeVector e : TypeVector.values())
                byId.put(e.id(), e);
        }
        public static TypeVector byId(long id) { return byId.get(id); }
    }

    public enum LabelPosition {
        LEFT(0),
        RIGHT(1),
        TOP(2),
        BOTTOM(3),
        CENTER(4);

        private final long id;
        LabelPosition(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, LabelPosition> byId = new HashMap<Long, LabelPosition>(5);
        static {
            for (LabelPosition e : LabelPosition.values())
                byId.put(e.id(), e);
        }
        public static LabelPosition byId(long id) { return byId.get(id); }
    }

    public enum Metadata {
        VERSION(26),
        CAPABILITIES(27),
        COMMANDS(28);

        private final long id;
        Metadata(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Metadata> byId = new HashMap<Long, Metadata>(3);
        static {
            for (Metadata e : Metadata.values())
                byId.put(e.id(), e);
        }
        public static Metadata byId(long id) { return byId.get(id); }
    }

    public enum Datatype {
        BOOL(16),
        INT8(17),
        UINT8(18),
        INT16(19),
        UINT16(20),
        INT32(21),
        UINT32(22),
        INT64(23),
        UINT64(24),
        FLOAT32(25),
        FLOAT64(26),
        VECTOR2F32(31),
        VECTOR3F32(37),
        VECTOR4F32(43),
        TSTR(45),
        SSTR(46),
        LSTR(47),
        RGB8(48),
        RGBA8(49),
        ARGB8(50),
        ENUM(54),
        ARRAY(55),
        IMAGE(57),
        BANG(58),
        TIME(59);

        private final long id;
        Datatype(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Datatype> byId = new HashMap<Long, Datatype>(25);
        static {
            for (Datatype e : Datatype.values())
                byId.put(e.id(), e);
        }
        public static Datatype byId(long id) { return byId.get(id); }
    }

    public enum TypeArray {
        DEFAULTVALUE(48),
        SUBTYPE(49);

        private final long id;
        TypeArray(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, TypeArray> byId = new HashMap<Long, TypeArray>(2);
        static {
            for (TypeArray e : TypeArray.values())
                byId.put(e.id(), e);
        }
        public static TypeArray byId(long id) { return byId.get(id); }
    }

    public enum Packet {
        TERMINATOR(0),
        ID(16),
        TIMESTAMP(17),
        DATA(18);

        private final long id;
        Packet(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Packet> byId = new HashMap<Long, Packet>(4);
        static {
            for (Packet e : Packet.values())
                byId.put(e.id(), e);
        }
        public static Packet byId(long id) { return byId.get(id); }
    }

    public RCPTypes(KaitaiStream _io) {
        super(_io);
        this._root = this;
        _read();
    }

    public RCPTypes(KaitaiStream _io, KaitaiStruct _parent) {
        super(_io);
        this._parent = _parent;
        this._root = this;
        _read();
    }

    public RCPTypes(KaitaiStream _io, KaitaiStruct _parent, RCPTypes _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root;
        _read();
    }
    private void _read() {
    }
    public static class TinyString extends KaitaiStruct {
        public static TinyString fromFile(String fileName) throws IOException {
            return new TinyString(new KaitaiStream(fileName));
        }

        public TinyString(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public TinyString(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public TinyString(KaitaiStream _io, KaitaiStruct _parent, RCPTypes _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.myLen = this._io.readU1();
            this.data = new String(this._io.readBytes(myLen()), Charset.forName("UTF-8"));
        }
        private int          myLen;
        private String       data;
        private RCPTypes     _root;
        private KaitaiStruct _parent;
        public int myLen() { return myLen; }
        public String data() { return data; }
        public RCPTypes _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class ShortString extends KaitaiStruct {
        public static ShortString fromFile(String fileName) throws IOException {
            return new ShortString(new KaitaiStream(fileName));
        }

        public ShortString(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public ShortString(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public ShortString(KaitaiStream _io, KaitaiStruct _parent, RCPTypes _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.myLen = this._io.readU2be();
            this.data = new String(this._io.readBytes(myLen()), Charset.forName("UTF-8"));
        }
        private int          myLen;
        private String       data;
        private RCPTypes     _root;
        private KaitaiStruct _parent;
        public int myLen() { return myLen; }
        public String data() { return data; }
        public RCPTypes _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class LongString extends KaitaiStruct {
        public static LongString fromFile(String fileName) throws IOException {
            return new LongString(new KaitaiStream(fileName));
        }

        public LongString(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public LongString(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public LongString(KaitaiStream _io, KaitaiStruct _parent, RCPTypes _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.myLen = this._io.readU4be();
            this.data = new String(this._io.readBytes(myLen()), Charset.forName("UTF-8"));
        }
        private long         myLen;
        private String       data;
        private RCPTypes     _root;
        private KaitaiStruct _parent;
        public long myLen() { return myLen; }
        public String data() { return data; }
        public RCPTypes _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class Userdata extends KaitaiStruct {
        public static Userdata fromFile(String fileName) throws IOException {
            return new Userdata(new KaitaiStream(fileName));
        }

        public Userdata(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public Userdata(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public Userdata(KaitaiStream _io, KaitaiStruct _parent, RCPTypes _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.myLen = this._io.readU4be();
            this.data = this._io.readBytes(myLen());
        }
        private long         myLen;
        private byte[]       data;
        private RCPTypes     _root;
        private KaitaiStruct _parent;
        public long myLen() { return myLen; }
        public byte[] data() { return data; }
        public RCPTypes _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    private RCPTypes     _root;
    private KaitaiStruct _parent;
    public RCPTypes _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
}
