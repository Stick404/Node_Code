package org.sophia.nodecode.logicSystems.nodes.inputs;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.types.TypeList;

import java.util.ArrayList;

import static org.sophia.nodecode.registries.DataTypeRegistry.TYPE_LIST;

public class NodeListInput extends NodeInput<ArrayList>{
    public final static DataType[] outputTypes = new DataType[]{TYPE_LIST.get()};
    public NodeListInput(){
        super();
    }

    public NodeListInput(NodeEnv env, ArrayList val){
        super(env,new TypeList(),val);
    }

    public DataType[] getOutputTypes() {
        return outputTypes;
    }
}
