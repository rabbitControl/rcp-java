package org.rabbitcontrol.rcp.model.interfaces;

import org.rabbitcontrol.rcp.model.RcpTypes.NumberScale;

public interface INumberDefinition<T extends Number> extends IDefaultDefinition<T> {

    T getMinimum();

    void setMinimum(final T _minimum);
//    void setMinimum(final long _minimum);

    void setMin(final Number _minimum);

    T getMaximum();

    void setMaximum(final T _maximum);
//    void setMaximum(final int _maximum);

    void setMax(final Number _maximum);

    T getMultipleof();

    void setMultipleof(final T _multipleof);
//    void setMultipleof(final int _multipleof);

    void setMult(final Number _multipleof);

    NumberScale getScale();

    void setScale(final NumberScale _scale);

    String getUnit();

    void setUnit(final String _unit);
}
