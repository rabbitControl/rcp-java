package org.rabbitcontrol.rcp.test;

import org.rabbitcontrol.rcp.model.types.UInt8Definition;

/**
 * Created by inx on 01/11/17.
 */
public class newTest {

    public static void t(byte b){

    }

    public static void main(String[] args) {


        int i;
        byte  b;

        b = (byte)200;

        i = UInt8Definition.getUnsigned((byte)100);

//        System.out.println((byte)200);
//        System.out.println((byte)220);
        System.out.println((int)b);
        System.out.println(i);

//        Short s = 300;
//        System.out.println(s.byteValue());


//        try {
//            INumberParameter<Float> num = ParameterFactory.createNumberParameter(123,
//                                                                                  Datatype.FLOAT32,
//                                                                                  Float.class);
//
//            num.getTypeDefinition().setMin(12);
//
//            num.getTypeDefinition().setDefault(1.F);
//            num.getTypeDefinition().setMaximum(123.F);
//
//            Number max = num.getTypeDefinition().getMaximum();
//
//            System.out.println("max : " + max);
//        }
//        catch (TypeMissmatch _typeMissmatch) {
//            _typeMissmatch.printStackTrace();
//        }
//
//        final IValueParameter<Boolean> bParam = ParameterFactory.createBooleanParameter(222);
//        bParam.getTypeDefinition().setDefault(true);
//        bParam.setValue(false);
//
//        final IValueParameter<String> sParam = ParameterFactory.createStringParameter(234);
//        sParam.getTypeDefinition().setDefault("default");
//        sParam.setValue("value");

    }
}
