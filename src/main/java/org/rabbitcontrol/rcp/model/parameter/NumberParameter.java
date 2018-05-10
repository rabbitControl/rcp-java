package org.rabbitcontrol.rcp.model.parameter;

import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.RcpTypes.NumberScale;
import org.rabbitcontrol.rcp.model.interfaces.INumberDefinition;
import org.rabbitcontrol.rcp.model.interfaces.INumberParameter;
import org.rabbitcontrol.rcp.model.types.DefaultDefinition;
import org.rabbitcontrol.rcp.model.types.NumberDefinition;

public class NumberParameter<T extends Number> extends ValueParameter<T> implements
                                                                         INumberParameter<T>,
                                                                         INumberDefinition<T>{

    //------------------------------------------------------------
    //------------------------------------------------------------
    // mandatory
    private final INumberDefinition<T> typeDefinition;

    //------------------------------------------------------------
    //------------------------------------------------------------
    public NumberParameter(final short _id, Datatype _datatype) {

        // create correct
        super(_id, (DefaultDefinition<T>)NumberDefinition.create(_datatype));

        typeDefinition = (INumberDefinition<T>)super.getTypeDefinition();
    }

    //------------------------------------------------------------
    //------------------------------------------------------------
    @Override
    public INumberDefinition<T> getTypeDefinition() {

        return typeDefinition;
    }

    @Override
    public T getMinimum() {

        return typeDefinition.getMinimum();
    }

    @Override
    public void setMinimum(final T _minimum) {
        typeDefinition.setMinimum(_minimum);
    }

    @Override
    public void setMin(final Number _minimum) {
        typeDefinition.setMin(_minimum);
    }

    @Override
    public T getMaximum() {

        return typeDefinition.getMaximum();
    }

    @Override
    public void setMaximum(final T _maximum) {
        typeDefinition.setMaximum(_maximum);
    }

    @Override
    public void setMax(final Number _maximum) {
        typeDefinition.setMax(_maximum);
    }

    @Override
    public T getMultipleof() {

        return typeDefinition.getMultipleof();
    }

    @Override
    public void setMultipleof(final T _multipleof) {
        typeDefinition.setMultipleof(_multipleof);
    }

    @Override
    public void setMult(final Number _multipleof) {
        typeDefinition.setMult(_multipleof);
    }

    @Override
    public NumberScale getScale() {

        return typeDefinition.getScale();
    }

    @Override
    public void setScale(final NumberScale _scale) {
        typeDefinition.setScale(_scale);
    }

    @Override
    public String getUnit() {

        return typeDefinition.getUnit();
    }

    @Override
    public void setUnit(final String _unit) {
        typeDefinition.setUnit(_unit);
    }

    @Override
    public T getDefault() {

        return typeDefinition.getDefault();
    }

    @Override
    public void setDefault(final T _default) {
        typeDefinition.setDefault(_default);
    }

    @Override
    public Datatype getDatatype() {

        return typeDefinition.getDatatype();
    }
}
