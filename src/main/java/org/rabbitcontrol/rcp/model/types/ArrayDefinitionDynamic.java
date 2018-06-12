package org.rabbitcontrol.rcp.model.types;

import io.kaitai.struct.KaitaiStream;
import org.rabbitcontrol.rcp.model.RcpTypes;

import java.io.IOException;
import java.io.OutputStream;

public class ArrayDefinitionDynamic<T, E> extends DefaultDefinition<T> {

    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    final DefaultDefinition<E> subtype;

    final int dimensions;

    final int[] dimSizes;


    public ArrayDefinitionDynamic(final DefaultDefinition<E> _subtype) {
        this(_subtype, -1);
    }

    public ArrayDefinitionDynamic(final DefaultDefinition<E> _subtype, final int... _dimSizes) {

        super(RcpTypes.Datatype.LIST);

        subtype = _subtype;
        dimSizes = _dimSizes;
        dimensions = _dimSizes.length;
    }

    @Override
    public void writeValue(final T _value, final OutputStream _outputStream) throws IOException {

    }

    @Override
    public T readValue(final KaitaiStream _io) {

        return null;
    }

    @Override
    protected boolean handleOption(final int _propertyId, final KaitaiStream _io) {

        return false;
    }

    @Override
    public void write(final OutputStream _outputStream, final boolean _all) throws IOException {

    }
}
