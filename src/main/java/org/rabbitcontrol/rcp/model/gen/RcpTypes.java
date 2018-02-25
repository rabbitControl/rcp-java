package org.rabbitcontrol.rcp.model.gen;

// This is a generated file! Please edit source .ksy file and use kaitai-struct-compiler to rebuild

import io.kaitai.struct.ByteBufferKaitaiStream;
import io.kaitai.struct.KaitaiStruct;
import io.kaitai.struct.KaitaiStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.nio.charset.Charset;

public class RcpTypes extends KaitaiStruct {
    public static RcpTypes fromFile(String fileName) throws IOException {
        return new RcpTypes(new ByteBufferKaitaiStream(fileName));
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

    public enum ClientStatus {
        DISCONNECTED(0),
        CONNECTED(1),
        VERSION_MISSMATCH(2),
        OK(3);

        private final long id;
        ClientStatus(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, ClientStatus> byId = new HashMap<Long, ClientStatus>(4);
        static {
            for (ClientStatus e : ClientStatus.values())
                byId.put(e.id(), e);
        }
        public static ClientStatus byId(long id) { return byId.get(id); }
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
        VECTOR2I32(27),
        VECTOR2F32(28),
        VECTOR3I32(29),
        VECTOR3F32(30),
        VECTOR4I32(31),
        VECTOR4F32(32),
        STRING(33),
        RGB(34),
        RGBA(35),
        ENUM(36),
        FIXED_ARRAY(37),
        DYNAMIC_ARRAY(38),
        BANG(39),
        GROUP(40),
        COMPOUND(41),
        URI(42),
        IPV4(43),
        IPV6(44);

        private final long id;
        Datatype(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Datatype> byId = new HashMap<Long, Datatype>(29);
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
        TIMESTAMP(17),
        DATA(18);

        private final long id;
        PacketOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, PacketOptions> byId = new HashMap<Long, PacketOptions>(2);
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
        this(_io, null, null);
    }

    public RcpTypes(KaitaiStream _io, KaitaiStruct _parent) {
        this(_io, _parent, null);
    }

    public RcpTypes(KaitaiStream _io, KaitaiStruct _parent, RcpTypes _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root == null ? this : _root;
        _read();
    }
    private void _read() {
    }
    public static class ShortString extends KaitaiStruct {
        public static ShortString fromFile(String fileName) throws IOException {
            return new ShortString(new ByteBufferKaitaiStream(fileName));
        }

        public ShortString(KaitaiStream _io) {
            this(_io, null, null);
        }

        public ShortString(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
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
    public static class Userdata extends KaitaiStruct {
        public static Userdata fromFile(String fileName) throws IOException {
            return new Userdata(new ByteBufferKaitaiStream(fileName));
        }

        public Userdata(KaitaiStream _io) {
            this(_io, null, null);
        }

        public Userdata(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
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
    public static class LongString extends KaitaiStruct {
        public static LongString fromFile(String fileName) throws IOException {
            return new LongString(new ByteBufferKaitaiStream(fileName));
        }

        public LongString(KaitaiStream _io) {
            this(_io, null, null);
        }

        public LongString(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
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
    public static class Id extends KaitaiStruct {
        public static Id fromFile(String fileName) throws IOException {
            return new Id(new ByteBufferKaitaiStream(fileName));
        }

        public Id(KaitaiStream _io) {
            this(_io, null, null);
        }

        public Id(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
        }

        public Id(KaitaiStream _io, KaitaiStruct _parent, RcpTypes _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.myLen = this._io.readU1();
            this.data = this._io.readBytes(myLen());
        }
        private int myLen;
        private byte[] data;
        private RcpTypes _root;
        private KaitaiStruct _parent;
        public int myLen() { return myLen; }
        public byte[] data() { return data; }
        public RcpTypes _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class TinyString extends KaitaiStruct {
        public static TinyString fromFile(String fileName) throws IOException {
            return new TinyString(new ByteBufferKaitaiStream(fileName));
        }

        public TinyString(KaitaiStream _io) {
            this(_io, null, null);
        }

        public TinyString(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
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
    private RcpTypes _root;
    private KaitaiStruct _parent;
    public RcpTypes _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
}
