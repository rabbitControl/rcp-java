package org.rabbitcontrol.rcp.model.types;

import org.rabbitcontrol.rcp.model.RCPTypeDefinition;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.RCPTypes.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPDataErrorException;
import io.kaitai.struct.KaitaiStream;

import java.io.IOException;
import java.io.OutputStream;

public abstract class RCPTypeNumber<T extends Number> extends RCPTypeDefinition<T> {

    public static void parseOption(
            final RCPTypeNumber<?> _typedef,
            final TypeNumber _dataid,
            final KaitaiStream _io) throws RCPDataErrorException {

        switch (_dataid) {

            case SCALE:
                _typedef.setScale(NumberScale.byId(_io.readU1()));
                break;

            case UNIT:
                final TinyString tinyString = new TinyString(_io);
                _typedef.setUnit(tinyString.data());
                break;

            default:
                // not a number data id!!
                throw new RCPDataErrorException();
        }
    }

    //----------------------------------------------------
    private T min;

    private T max;

    private T multipleof;

    private NumberScale scale;

    private String unit;

    //----------------------------------------------------
    public RCPTypeNumber(final Datatype _typeid) {

        super(_typeid);
    }

    public RCPTypeNumber(
            final Datatype _typeid, final T _min, final T _max, final T _multipleof) {

        super(_typeid);
        min = _min;
        max = _max;
        multipleof = _multipleof;
    }

    @Override
    public void write(final OutputStream _outputStream) throws IOException {

        super.write(_outputStream);

        if (getMin() != null) {
            _outputStream.write((int)TypeNumber.MIN.id());
            writeValue(min, _outputStream);
        }

        if (getMax() != null) {
            _outputStream.write((int)TypeNumber.MAX.id());
            writeValue(max, _outputStream);
        }

        if (getMultipleof() != null) {
            _outputStream.write((int)TypeNumber.MULT.id());
            writeValue(multipleof, _outputStream);
        }

        if (scale != null) {
            _outputStream.write((int)TypeNumber.SCALE.id());
            _outputStream.write((int)scale.id());
        }

        if (unit != null) {
            _outputStream.write((int)TypeNumber.UNIT.id());
            RCPParser.writeTinyString(unit, _outputStream);
        }

        // finalize typedefinition with terminator
        _outputStream.write((int)Packet.TERMINATOR.id());
    }

    //----------------------------------------------------
    public T getMin() {

        return min;
    }

    public void setMin(final T _min) {

        min = _min;
    }

    public T getMax() {

        return max;
    }

    public void setMax(final T _max) {

        max = _max;
    }

    public T getMultipleof() {

        return multipleof;
    }

    public void setMultipleof(final T _multipleof) {

        multipleof = _multipleof;
    }

    public NumberScale getScale() {

        return scale;
    }

    public void setScale(final NumberScale _scale) {

        scale = _scale;
    }

    public String getUnit() {

        return unit;
    }

    public void setUnit(final String _unit) {

        unit = _unit;
    }

}
