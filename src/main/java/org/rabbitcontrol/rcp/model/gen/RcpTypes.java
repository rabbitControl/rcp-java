// This is a generated file! Please edit source .ksy file and use kaitai-struct-compiler to rebuild

package org.rabbitcontrol.rcp.model.gen;

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

    public enum EnumOptions {
        DEFAULT(48),
        ENTRIES(49);

        private final long id;
        EnumOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, EnumOptions> byId = new HashMap<Long, EnumOptions>(2);
        static {
            for (EnumOptions e : EnumOptions.values())
                byId.put(e.id(), e);
        }
        public static EnumOptions byId(long id) { return byId.get(id); }
    }

    public enum WidgetOptions {
        TYPE(80),
        ENABLED(81),
        VISIBLE(82),
        LABEL_VISIBLE(83),
        VALUE_VISIBLE(84),
        LABEL_POSITION(85);

        private final long id;
        WidgetOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, WidgetOptions> byId = new HashMap<Long, WidgetOptions>(6);
        static {
            for (WidgetOptions e : WidgetOptions.values())
                byId.put(e.id(), e);
        }
        public static WidgetOptions byId(long id) { return byId.get(id); }
    }

    public enum ColorOptions {
        DEFAULT(48);

        private final long id;
        ColorOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, ColorOptions> byId = new HashMap<Long, ColorOptions>(1);
        static {
            for (ColorOptions e : ColorOptions.values())
                byId.put(e.id(), e);
        }
        public static ColorOptions byId(long id) { return byId.get(id); }
    }

    public enum ParameterOptions {
        VALUE(32),
        LABEL(33),
        DESCRIPTION(34),
        ORDER(35),
        PARENT(36),
        WIDGET(37),
        USERDATA(38);

        private final long id;
        ParameterOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, ParameterOptions> byId = new HashMap<Long, ParameterOptions>(7);
        static {
            for (ParameterOptions e : ParameterOptions.values())
                byId.put(e.id(), e);
        }
        public static ParameterOptions byId(long id) { return byId.get(id); }
    }

    public enum VectorOptions {
        DEFAULT(48),
        MINIMUM(49),
        MAXIMUM(50),
        MULTIPLEOF(51),
        SCALE(52),
        UNIT(53);

        private final long id;
        VectorOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, VectorOptions> byId = new HashMap<Long, VectorOptions>(6);
        static {
            for (VectorOptions e : VectorOptions.values())
                byId.put(e.id(), e);
        }
        public static VectorOptions byId(long id) { return byId.get(id); }
    }

    public enum CompoundOptions {
        DEFAULT(48);

        private final long id;
        CompoundOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, CompoundOptions> byId = new HashMap<Long, CompoundOptions>(1);
        static {
            for (CompoundOptions e : CompoundOptions.values())
                byId.put(e.id(), e);
        }
        public static CompoundOptions byId(long id) { return byId.get(id); }
    }

    public enum BooleanOptions {
        DEFAULT(48);

        private final long id;
        BooleanOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, BooleanOptions> byId = new HashMap<Long, BooleanOptions>(1);
        static {
            for (BooleanOptions e : BooleanOptions.values())
                byId.put(e.id(), e);
        }
        public static BooleanOptions byId(long id) { return byId.get(id); }
    }

    public enum Widgettype {
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
        Widgettype(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Widgettype> byId = new HashMap<Long, Widgettype>(12);
        static {
            for (Widgettype e : Widgettype.values())
                byId.put(e.id(), e);
        }
        public static Widgettype byId(long id) { return byId.get(id); }
    }

    public enum Command {
        INVALID(0),
        VERSION(1),
        INITIALIZE(2),
        ADD(3),
        UPDATE(4),
        REMOVE(5),
        UPDATEVALUE(6);

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
        LINEAR(0),
        LOGARITHMIC(1),
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

    public enum DynamicArrayOptions {
        DEFAULT(48);

        private final long id;
        DynamicArrayOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, DynamicArrayOptions> byId = new HashMap<Long, DynamicArrayOptions>(1);
        static {
            for (DynamicArrayOptions e : DynamicArrayOptions.values())
                byId.put(e.id(), e);
        }
        public static DynamicArrayOptions byId(long id) { return byId.get(id); }
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

    public enum StringOptions {
        DEFAULT(48);

        private final long id;
        StringOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, StringOptions> byId = new HashMap<Long, StringOptions>(1);
        static {
            for (StringOptions e : StringOptions.values())
                byId.put(e.id(), e);
        }
        public static StringOptions byId(long id) { return byId.get(id); }
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

    public enum NumberOptions {
        DEFAULT(48),
        MINIMUM(49),
        MAXIMUM(50),
        MULTIPLEOF(51),
        SCALE(52),
        UNIT(53);

        private final long id;
        NumberOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, NumberOptions> byId = new HashMap<Long, NumberOptions>(6);
        static {
            for (NumberOptions e : NumberOptions.values())
                byId.put(e.id(), e);
        }
        public static NumberOptions byId(long id) { return byId.get(id); }
    }

    public enum MetadataOptions {
        VERSION(26),
        CAPABILITIES(27),
        COMMANDS(28);

        private final long id;
        MetadataOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, MetadataOptions> byId = new HashMap<Long, MetadataOptions>(3);
        static {
            for (MetadataOptions e : MetadataOptions.values())
                byId.put(e.id(), e);
        }
        public static MetadataOptions byId(long id) { return byId.get(id); }
    }

    public enum PacketOptions {
        ID(16),
        TIMESTAMP(17),
        DATA(18);

        private final long id;
        PacketOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, PacketOptions> byId = new HashMap<Long, PacketOptions>(3);
        static {
            for (PacketOptions e : PacketOptions.values())
                byId.put(e.id(), e);
        }
        public static PacketOptions byId(long id) { return byId.get(id); }
    }

    public enum FixedArrayOptions {
        DEFAULT(48);

        private final long id;
        FixedArrayOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, FixedArrayOptions> byId = new HashMap<Long, FixedArrayOptions>(1);
        static {
            for (FixedArrayOptions e : FixedArrayOptions.values())
                byId.put(e.id(), e);
        }
        public static FixedArrayOptions byId(long id) { return byId.get(id); }
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
        private int myLen;
        private String data;
        private RcpTypes _root;
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
        private int myLen;
        private String data;
        private RcpTypes _root;
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
        private long myLen;
        private String data;
        private RcpTypes _root;
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
        private long myLen;
        private byte[] data;
        private RcpTypes _root;
        private KaitaiStruct _parent;
        public long myLen() { return myLen; }
        public byte[] data() { return data; }
        public RcpTypes _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    private RcpTypes _root;
    private KaitaiStruct _parent;
    public RcpTypes _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
}
