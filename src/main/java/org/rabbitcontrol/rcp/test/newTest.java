package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.exceptions.TypeMissmatch;
import org.rabbitcontrol.rcp.model.gen.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.interfaces.INumberParameter;
import org.rabbitcontrol.rcp.model.interfaces.IValueParameter;
import org.rabbitcontrol.rcp.model.ParameterFactory;

/**
 * Created by inx on 01/11/17.
 */
public class newTest {

    public static void main(String[] args) {

        try {
            INumberParameter<Float> num = ParameterFactory.createNumberParameter(123,
                                                                                  Datatype.FLOAT32,
                                                                                  Float.class);

            num.getTypeDefinition().setMin(12);

            num.getTypeDefinition().setDefault(1.F);
            num.getTypeDefinition().setMaximum(123.F);

            Number max = num.getTypeDefinition().getMaximum();

            System.out.println("max : " + max);
        }
        catch (TypeMissmatch _typeMissmatch) {
            _typeMissmatch.printStackTrace();
        }

        final IValueParameter<Boolean> bParam = ParameterFactory.createBooleanParameter(222);
        bParam.getTypeDefinition().setDefault(true);
        bParam.setValue(false);

        final IValueParameter<String> sParam = ParameterFactory.createStringParameter(234);
        sParam.getTypeDefinition().setDefault("default");
        sParam.setValue("value");

    }
}
