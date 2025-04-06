package org.sophia.nodecode.logicSystems.nodes.inputs;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.types.TypeDouble;

import static org.sophia.nodecode.registries.DataTypeRegistry.TYPE_DOUBLE;

public class NodeDoubleInput extends NodeInput<Double>{

    public NodeDoubleInput(NodeEnv env, Double val){
        super(env,new TypeDouble(),val);
    }

    public DataType[] getOutputTypes() {
        return new DataType[]{TYPE_DOUBLE.get()};
    }
}
