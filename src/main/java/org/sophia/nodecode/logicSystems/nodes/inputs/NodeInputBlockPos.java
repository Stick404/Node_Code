package org.sophia.nodecode.logicSystems.nodes.inputs;

import net.minecraft.core.BlockPos;
import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.types.TypeBlockPos;

import static org.sophia.nodecode.registries.DataTypeRegistry.TYPE_BLOCK_POS;

public class NodeInputBlockPos extends NodeInput<BlockPos>{
    public final static DataType<?>[] outputTypes = new DataType[]{TYPE_BLOCK_POS.get()};

    public NodeInputBlockPos(){
        super();
    }

    public NodeInputBlockPos(NodeEnv env, BlockPos val){
        super(env,new TypeBlockPos(),val);
    }

    public DataType<?>[] getOutputTypes() {
        return outputTypes;
    }
}
