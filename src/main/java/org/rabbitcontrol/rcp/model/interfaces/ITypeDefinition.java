package org.rabbitcontrol.rcp.model.interfaces;

import org.rabbitcontrol.rcp.model.RCPWritable;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;

public interface ITypeDefinition extends RCPWritable {

    //--------------------------------
    // mandatory
    Datatype getDatatype();
}
