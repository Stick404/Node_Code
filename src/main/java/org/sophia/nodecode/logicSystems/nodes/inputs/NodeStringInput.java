package org.sophia.nodecode.logicSystems.nodes.inputs;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.types.TypeDouble;
import org.sophia.nodecode.logicSystems.types.TypeString;

import static org.sophia.nodecode.registries.DataTypeRegistry.TYPE_DOUBLE;
import static org.sophia.nodecode.registries.DataTypeRegistry.TYPE_STRING;

public class NodeStringInput extends NodeInput<String>{
    public final static DataType[] outputTypes = new DataType[]{TYPE_STRING.get()};

    public NodeStringInput(){
        super();
    }

    public NodeStringInput(NodeEnv env, String val){
        super(env,new TypeString(),val);
    }

    public DataType[] getOutputTypes() {
        return outputTypes;
    }
}
