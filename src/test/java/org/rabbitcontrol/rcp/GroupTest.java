package org.rabbitcontrol.rcp;

import org.junit.Assert;
import org.junit.Test;
import org.rabbitcontrol.rcp.model.Parameter;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.interfaces.IParameterManager;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;
import org.rabbitcontrol.rcp.model.parameter.StringParameter;

public class GroupTest {

    GroupParameter group;
    StringParameter parameter;

    private final IParameterManager mngr = new IParameterManager() {

        @Override
        public IParameter getParameter(final short _id) {

            if (_id == group.getId()) return group;
            if (_id == parameter.getId()) return parameter;

            return null;
        }

        @Override
        public void setParameterDirty(final IParameter _parameter) {

            System.out.println("dirty param: " + _parameter.getId());
        }
    };

    @Test
    public void testGroup() throws Exception {

        group = new GroupParameter((short)1);
        parameter = new StringParameter((short)2);

        group.setManager(mngr);
        parameter.setManager(mngr);


        group.addChild(parameter);

        //--------------------------------
        final Parameter parsed_group = ParameterTest.writeAndParse(group);

        Assert.assertNotEquals("could not parse parameter", parsed_group, null);

        Assert.assertTrue("wrong parameter type", parsed_group instanceof GroupParameter);


        //--------------------------------
        final Parameter parsed_parameter = ParameterTest.writeAndParse(parameter, mngr);
        Assert.assertNotNull("could not parse parameter", parsed_parameter);

        Assert.assertTrue("wrong parameter type", parsed_parameter instanceof StringParameter);

        Assert.assertNotEquals("no parent", parsed_parameter.getParent(), null);
        Assert.assertEquals("wrong parent", group.getId(), parsed_parameter.getParent().getId());
    }

    @Test
    public void testRemoveGroup() throws Exception {

        group = new GroupParameter((short)1);
        parameter = new StringParameter((short)2);

        group.setManager(mngr);
        parameter.setManager(mngr);

        group.addChild(parameter);

        Assert.assertEquals("set parent id missmatch", group.getId(),
                            parameter.getParent().getId());

        group.removeChild(parameter);

        Assert.assertNull("unset parent id missmatch", parameter.getParent());
    }

    @Test
    public void testResolvePendingGroup() throws Exception {

        group = new GroupParameter((short)1);
        parameter = new StringParameter((short)2);

        group.setManager(mngr);
        parameter.setManager(mngr);

        group.addChild(parameter);


        //--------------------------------
        final Parameter parsed_parameter = ParameterTest.writeAndParse(parameter);
        Assert.assertNotNull("could not parse parameter", parsed_parameter);

        // parameter without manager

        Assert.assertTrue(parsed_parameter.hasPendingParents());

        parsed_parameter.setManager(mngr);

        // this adds the parsed parameter to same group.
        // !! group ends up with 2 parameters with same id !!
        parsed_parameter.resolvePendingParents();

        // we should have a parent now!
        Assert.assertFalse(parsed_parameter.hasPendingParents());


        Assert.assertEquals("set parent id missmatch", group.getId(),
                            parameter.getParent().getId());
    }
}
