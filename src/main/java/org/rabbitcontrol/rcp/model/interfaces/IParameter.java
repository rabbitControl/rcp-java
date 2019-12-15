package org.rabbitcontrol.rcp.model.interfaces;

import org.rabbitcontrol.rcp.model.Parameter.PARAMETER_UPDATED;
import org.rabbitcontrol.rcp.model.Parameter.PARAMETER_VALUE_UPDATED;
import org.rabbitcontrol.rcp.model.RCPWritable;
import org.rabbitcontrol.rcp.model.Widget;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;

import java.util.Set;

public interface IParameter extends RCPWritable {

    //--------------------------------
    // mandatory
    short getId();

    ITypeDefinition getTypeDefinition();

    //--------------------------------
    // optional
    String getLabel();

    void setLabel(final String _label);

    Set<String> getLabelLanguages();

    String getLanguageLabel(final String _code);

    void clearLanguageLabel();

    void setLanguageLabel(final String _code, final String _label);

    void removeLanguageLabel(final String _code);

    String getDescription();

    void setDescription(final String _description);

    Set<String> getDescriptionLanguages();

    String getLanguageDescription(final String _code);

    void clearLangaugeDescription();

    void setLanguageDescription(final String _code, final String _label);

    void removeLanguageDescription(final String _code);

    String getTags();

    void setTags(final String _tags);

    Integer getOrder();

    void setOrder(final int _order);

    GroupParameter getParent();

    //    void setParent(GroupParameter _parent);

    Widget getWidget();

    void setWidget(Widget _widget);

    String getUserid();

    void setUserid(final String _userid);

    byte[] getUserdata();

    void setUserdata(final byte[] _userdata);

    boolean getReadonly();

    void setReadonly(final boolean _readonly);

    void setDirty();

    void setParent(final GroupParameter _parent);

    void setManager(final IParameterManager _model);

    // update listener
    void addUpdateListener(final PARAMETER_UPDATED _listener);
    void removeUpdateListener(final PARAMETER_UPDATED _listener);
    void clearUpdateListener();
    void addValueUpdateListener(final PARAMETER_VALUE_UPDATED _listener);
    void removeValueUpdateListener(final PARAMETER_VALUE_UPDATED _listener);
    void clearValueUpdateListener();

    String getStringValue();
    void setStringValue(final String _value);

    boolean onlyValueChanged();

    //
    void dump();
}
