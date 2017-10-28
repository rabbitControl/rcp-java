package org.rabbitcontrol.rcp.model.gen;// This is a generated file! Please edit source .ksy file and use kaitai-struct-compiler to rebuild

import io.kaitai.struct.KaitaiStream;
import io.kaitai.struct.KaitaiStruct;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class RcpTypes extends KaitaiStruct {
    public static RcpTypes fromFile(String fileName) throws IOException {
        return new RcpTypes(new KaitaiStream(fileName));
    }

    public enum StringProperty {
        DEFAULTVALUE(48);

        private final long id;
        StringProperty(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, StringProperty> byId = new HashMap<Long, StringProperty>(1);
        static {
            for (StringProperty e : StringProperty.values())
                byId.put(e.id(), e);
        }
        public static StringProperty byId(long id) { return byId.get(id); }
    }

    public enum Parameter {
        VALUE(32),
        LABEL(33),
        DESCRIPTION(34),
        ORDER(35),
        PARENT(36),
        WIDGET(37),
        USERDATA(38);

        private final long id;
        Parameter(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Parameter> byId = new HashMap<Long, Parameter>(7);
        static {
            for (Parameter e : Parameter.values())
                byId.put(e.id(), e);
        }
        public static Parameter byId(long id) { return byId.get(id); }
    }

    public enum DynamicArrayProperty {
        DEFAULTVALUE(48);

        private final long id;
        DynamicArrayProperty(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, DynamicArrayProperty> byId = new HashMap<Long, DynamicArrayProperty>(1);
        static {
            for (DynamicArrayProperty e : DynamicArrayProperty.values())
                byId.put(e.id(), e);
        }
        public static DynamicArrayProperty byId(long id) { return byId.get(id); }
    }

    public enum BooleanProperty {
        DEFAULTVALUE(48);

        private final long id;
        BooleanProperty(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, BooleanProperty> byId = new HashMap<Long, BooleanProperty>(1);
        static {
            for (BooleanProperty e : BooleanProperty.values())
                byId.put(e.id(), e);
        }
        public static BooleanProperty byId(long id) { return byId.get(id); }
    }

    public enum EnumProperty {
        DEFAULTVALUE(48),
        ENTRIES(49);

        private final long id;
        EnumProperty(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, EnumProperty> byId = new HashMap<Long, EnumProperty>(2);
        static {
            for (EnumProperty e : EnumProperty.values())
                byId.put(e.id(), e);
        }
        public static EnumProperty byId(long id) { return byId.get(id); }
    }

    public enum Widget {
        TYPE(80),
        ENABLED(81),
        VISIBLE(82),
        LABEL_VISIBLE(83),
        VALUE_VISIBLE(84),
        LABEL_POSITION(85);

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

    public enum CompoundProperty {
        DEFAULTVALUE(48);

        private final long id;
        CompoundProperty(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, CompoundProperty> byId = new HashMap<Long, CompoundProperty>(1);
        static {
            for (CompoundProperty e : CompoundProperty.values())
                byId.put(e.id(), e);
        }
        public static CompoundProperty byId(long id) { return byId.get(id); }
    }

    public enum Command {
        INVALID(0),
        VERSION(1),
        INIT(2),
        ADD(3),
        UPDATE(4),
        REMOVE(5),
        UPDATE_VALUE(6);

        private final long id;
        Command(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Command> byId = new HashMap<Long, Command>(7);
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

    public enum FixedArrayProperty {
        DEFAULTVALUE(48);

        private final long id;
        FixedArrayProperty(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, FixedArrayProperty> byId = new HashMap<Long, FixedArrayProperty>(1);
        static {
            for (FixedArrayProperty e : FixedArrayProperty.values())
                byId.put(e.id(), e);
        }
        public static FixedArrayProperty byId(long id) { return byId.get(id); }
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

    public enum VectorProperty {
        DEFAULTVALUE(48),
        MIN(49),
        MAX(50),
        MULT(51),
        SCALE(52),
        UNIT(53);

        private final long id;
        VectorProperty(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, VectorProperty> byId = new HashMap<Long, VectorProperty>(6);
        static {
            for (VectorProperty e : VectorProperty.values())
                byId.put(e.id(), e);
        }
        public static VectorProperty byId(long id) { return byId.get(id); }
    }

    public enum ColorProperty {
        DEFAULTVALUE(48);

        private final long id;
        ColorProperty(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, ColorProperty> byId = new HashMap<Long, ColorProperty>(1);
        static {
            for (ColorProperty e : ColorProperty.values())
                byId.put(e.id(), e);
        }
        public static ColorProperty byId(long id) { return byId.get(id); }
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
        BOOLEAN(16),
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
        VECTOR2I8(27),
        VECTOR2I16(28),
        VECTOR2I32(29),
        VECTOR2I64(30),
        VECTOR2F32(31),
        VECTOR2F64(32),
        VECTOR3I8(33),
        VECTOR3I16(34),
        VECTOR3I32(35),
        VECTOR3I64(36),
        VECTOR3F32(37),
        VECTOR3F64(38),
        VECTOR4I8(39),
        VECTOR4I16(40),
        VECTOR4I32(41),
        VECTOR4I64(42),
        VECTOR4F32(43),
        VECTOR4F64(44),
        TINY_STRING(45),
        SHORT_STRING(46),
        STRING(47),
        RGB(48),
        RGBA(49),
        ENUM(50),
        FIXED_ARRAY(51),
        DYNAMIC_ARRAY(52),
        IMAGE(54),
        BANG(55),
        TIME(56),
        GROUP(57),
        COMPOUND(58);

        private final long id;
        Datatype(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Datatype> byId = new HashMap<Long, Datatype>(42);
        static {
            for (Datatype e : Datatype.values())
                byId.put(e.id(), e);
        }
        public static Datatype byId(long id) { return byId.get(id); }
    }

    public enum Packet {
        ID(16),
        TIMESTAMP(17),
        DATA(18);

        private final long id;
        Packet(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Packet> byId = new HashMap<Long, Packet>(3);
        static {
            for (Packet e : Packet.values())
                byId.put(e.id(), e);
        }
        public static Packet byId(long id) { return byId.get(id); }
    }

    public enum NumberProperty {
        DEFAULTVALUE(48),
        MIN(49),
        MAX(50),
        MULT(51),
        SCALE(52),
        UNIT(53);

        private final long id;
        NumberProperty(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, NumberProperty> byId = new HashMap<Long, NumberProperty>(6);
        static {
            for (NumberProperty e : NumberProperty.values())
                byId.put(e.id(), e);
        }
        public static NumberProperty byId(long id) { return byId.get(id); }
    }

    public RcpTypes(KaitaiStream _io) {
        super(_io);
        this._root = this;
        _read();
    }

    public RcpTypes(KaitaiStream _io, KaitaiStruct _parent) {
        super(_io);
        this._parent = _parent;
        this._root = this;
        _read();
    }

    public RcpTypes(KaitaiStream _io, KaitaiStruct _parent, RcpTypes _root) {
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

        public TinyString(KaitaiStream _io, KaitaiStruct _parent, RcpTypes _root) {
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
        private RcpTypes     _root;
        private KaitaiStruct _parent;
        public int myLen() { return myLen; }
        public String data() { return data; }
        public RcpTypes _root() { return _root; }
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

        public ShortString(KaitaiStream _io, KaitaiStruct _parent, RcpTypes _root) {
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
        private RcpTypes     _root;
        private KaitaiStruct _parent;
        public int myLen() { return myLen; }
        public String data() { return data; }
        public RcpTypes _root() { return _root; }
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

        public LongString(KaitaiStream _io, KaitaiStruct _parent, RcpTypes _root) {
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
        private RcpTypes     _root;
        private KaitaiStruct _parent;
        public long myLen() { return myLen; }
        public String data() { return data; }
        public RcpTypes _root() { return _root; }
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

        public Userdata(KaitaiStream _io, KaitaiStruct _parent, RcpTypes _root) {
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
        private RcpTypes     _root;
        private KaitaiStruct _parent;
        public long myLen() { return myLen; }
        public byte[] data() { return data; }
        public RcpTypes _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    private RcpTypes     _root;
    private KaitaiStruct _parent;
    public RcpTypes _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
}
