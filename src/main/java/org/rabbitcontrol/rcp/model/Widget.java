package org.rabbitcontrol.rcp.model;

import org.rabbitcontrol.rcp.model.RcpTypes.Widgettype;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;

public interface Widget extends RCPWritable {

    Widgettype getWidgettype();

    void setEnabled(final boolean enable);

    boolean isEnabled();

    void setLabelVisible(final boolean visible);

    boolean isLabelVisible();

    void setValueVisible(final boolean visible);

    boolean isValueVisible();

    void dump();

    void setParameter(IParameter parameter);
}
