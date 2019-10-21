// This is a generated file! Please edit source .ksy file and use kaitai-struct-compiler to rebuild

package org.rabbitcontrol.rcp.model;

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
        ENTRIES(49),
        MULTISELECT(50);

        private final long id;
        EnumOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, EnumOptions> byId = new HashMap<Long, EnumOptions>(3);
        static {
            for (EnumOptions e : EnumOptions.values())
                byId.put(e.id(), e);
        }
        public static EnumOptions byId(long id) { return byId.get(id); }
    }

    public enum NumberboxOptions {
        PRECISION(86),
        FORMAT(87),
        STEPSIZE(88),
        CYCLIC(89);

        private final long id;
        NumberboxOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, NumberboxOptions> byId = new HashMap<Long, NumberboxOptions>(4);
        static {
            for (NumberboxOptions e : NumberboxOptions.values())
                byId.put(e.id(), e);
        }
        public static NumberboxOptions byId(long id) { return byId.get(id); }
    }

    public enum CustomtypeOptions {
        DEFAULT(48),
        UUID(49),
        CONFIG(50);

        private final long id;
        CustomtypeOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, CustomtypeOptions> byId = new HashMap<Long, CustomtypeOptions>(3);
        static {
            for (CustomtypeOptions e : CustomtypeOptions.values())
                byId.put(e.id(), e);
        }
        public static CustomtypeOptions byId(long id) { return byId.get(id); }
    }

    public enum WidgetOptions {
        ENABLED(80),
        LABEL_VISIBLE(81),
        VALUE_VISIBLE(82),
        NEEDS_CONFIRMATION(83);

        private final long id;
        WidgetOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, WidgetOptions> byId = new HashMap<Long, WidgetOptions>(4);
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
        TAGS(35),
        ORDER(36),
        PARENTID(37),
        WIDGET(38),
        USERDATA(39),
        USERID(40),
        READONLY(41);

        private final long id;
        ParameterOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, ParameterOptions> byId = new HashMap<Long, ParameterOptions>(10);
        static {
            for (ParameterOptions e : ParameterOptions.values())
                byId.put(e.id(), e);
        }
        public static ParameterOptions byId(long id) { return byId.get(id); }
    }

    public enum Ipv4Options {
        DEFAULT(48);

        private final long id;
        Ipv4Options(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Ipv4Options> byId = new HashMap<Long, Ipv4Options>(1);
        static {
            for (Ipv4Options e : Ipv4Options.values())
                byId.put(e.id(), e);
        }
        public static Ipv4Options byId(long id) { return byId.get(id); }
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
        DEFAULT(1),
        CUSTOM(2),
        INFO(16),
        TEXTBOX(17),
        BANG(18),
        PRESS(19),
        TOGGLE(20),
        NUMBERBOX(21),
        DIAL(22),
        SLIDER(23),
        SLIDER2D(24),
        RANGE(25),
        DROPDOWN(26),
        RADIOBUTTON(27),
        COLORBOX(28),
        TABLE(29),
        FILECHOOSER(30),
        DIRECTORYCHOOSER(31),
        IP(32),
        LIST(32768),
        LISTPAGE(32769),
        TABS(32770);

        private final long id;
        Widgettype(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Widgettype> byId = new HashMap<Long, Widgettype>(22);
        static {
            for (Widgettype e : Widgettype.values())
                byId.put(e.id(), e);
        }
        public static Widgettype byId(long id) { return byId.get(id); }
    }

    public enum Command {
        INVALID(0),
        INFO(1),
        INITIALIZE(2),
        DISCOVER(3),
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

    public enum DialOptions {
        CYCLIC(86);

        private final long id;
        DialOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, DialOptions> byId = new HashMap<Long, DialOptions>(1);
        static {
            for (DialOptions e : DialOptions.values())
                byId.put(e.id(), e);
        }
        public static DialOptions byId(long id) { return byId.get(id); }
    }

    public enum RangeOptions {
        DEFAULT(48);

        private final long id;
        RangeOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, RangeOptions> byId = new HashMap<Long, RangeOptions>(1);
        static {
            for (RangeOptions e : RangeOptions.values())
                byId.put(e.id(), e);
        }
        public static RangeOptions byId(long id) { return byId.get(id); }
    }

    public enum LabelPosition {
        LEFT(1),
        RIGHT(2),
        TOP(3),
        BOTTOM(4),
        CENTER(5);

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

    public enum UriOptions {
        DEFAULT(48),
        FILTER(49),
        SCHEMA(50);

        private final long id;
        UriOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, UriOptions> byId = new HashMap<Long, UriOptions>(3);
        static {
            for (UriOptions e : UriOptions.values())
                byId.put(e.id(), e);
        }
        public static UriOptions byId(long id) { return byId.get(id); }
    }

    public enum SliderOptions {
        HORIZONTAL(86);

        private final long id;
        SliderOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, SliderOptions> byId = new HashMap<Long, SliderOptions>(1);
        static {
            for (SliderOptions e : SliderOptions.values())
                byId.put(e.id(), e);
        }
        public static SliderOptions byId(long id) { return byId.get(id); }
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
        DEFAULT(48),
        REGULAR_EXPRESSION(49);

        private final long id;
        StringOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, StringOptions> byId = new HashMap<Long, StringOptions>(2);
        static {
            for (StringOptions e : StringOptions.values())
                byId.put(e.id(), e);
        }
        public static StringOptions byId(long id) { return byId.get(id); }
    }

    public enum InfodataOptions {
        APPLICATIONID(26);

        private final long id;
        InfodataOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, InfodataOptions> byId = new HashMap<Long, InfodataOptions>(1);
        static {
            for (InfodataOptions e : InfodataOptions.values())
                byId.put(e.id(), e);
        }
        public static InfodataOptions byId(long id) { return byId.get(id); }
    }

    public enum ArrayOptions {
        DEFAULT(48);

        private final long id;
        ArrayOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, ArrayOptions> byId = new HashMap<Long, ArrayOptions>(1);
        static {
            for (ArrayOptions e : ArrayOptions.values())
                byId.put(e.id(), e);
        }
        public static ArrayOptions byId(long id) { return byId.get(id); }
    }

    public enum NumberboxFormat {
        DEC(1),
        HEX(2),
        BIN(3);

        private final long id;
        NumberboxFormat(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, NumberboxFormat> byId = new HashMap<Long, NumberboxFormat>(3);
        static {
            for (NumberboxFormat e : NumberboxFormat.values())
                byId.put(e.id(), e);
        }
        public static NumberboxFormat byId(long id) { return byId.get(id); }
    }

    public enum Datatype {
        CUSTOMTYPE(1),
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
        ARRAY(37),
        LIST(38),
        BANG(39),
        GROUP(40),
        URI(42),
        IPV4(43),
        IPV6(44),
        RANGE(45),
        IMAGE(46);

        private final long id;
        Datatype(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Datatype> byId = new HashMap<Long, Datatype>(31);
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

    public enum Ipv6Options {
        DEFAULT(48);

        private final long id;
        Ipv6Options(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Ipv6Options> byId = new HashMap<Long, Ipv6Options>(1);
        static {
            for (Ipv6Options e : Ipv6Options.values())
                byId.put(e.id(), e);
        }
        public static Ipv6Options byId(long id) { return byId.get(id); }
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

    public enum TextboxOptions {
        MULTILINE(86),
        WORDWRAP(87),
        PASSWORD(88);

        private final long id;
        TextboxOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, TextboxOptions> byId = new HashMap<Long, TextboxOptions>(3);
        static {
            for (TextboxOptions e : TextboxOptions.values())
                byId.put(e.id(), e);
        }
        public static TextboxOptions byId(long id) { return byId.get(id); }
    }

    public enum ListOptions {
        DEFAULT(48),
        MINIMUM(49),
        MAXIMUM(50);

        private final long id;
        ListOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, ListOptions> byId = new HashMap<Long, ListOptions>(3);
        static {
            for (ListOptions e : ListOptions.values())
                byId.put(e.id(), e);
        }
        public static ListOptions byId(long id) { return byId.get(id); }
    }

    public enum CustomwidgetOptions {
        UUID(86),
        CONFIG(87);

        private final long id;
        CustomwidgetOptions(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, CustomwidgetOptions> byId = new HashMap<Long, CustomwidgetOptions>(2);
        static {
            for (CustomwidgetOptions e : CustomwidgetOptions.values())
                byId.put(e.id(), e);
        }
        public static CustomwidgetOptions byId(long id) { return byId.get(id); }
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
    private RcpTypes _root;
    private KaitaiStruct _parent;
    public RcpTypes _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
}
