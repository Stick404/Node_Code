package org.sophia.nodecode.logicSystems.nodes;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.core.NodeEnv;

public class NodeInputNew extends Node {
    private DataType<?> type;
    private Object value;

    public NodeInputNew(){
        super();
    }

    public NodeInputNew(NodeEnv env, DataType<?> type, Object value) { //The init value to use
        super(env);
        this.type = type;
        this.value = value;
    }

    @Override
    public DataType[] getInputTypes() {
        return new DataType[0];
    }

    @Override
    public DataType[] getOutputTypes() {
        return new DataType[]{
            this.type
        };
    }

    @Override
    protected DataType<?>[] run() {
        try {
            var z = type.getClass().getConstructor().newInstance();
            z.setData(this.value);
            System.out.println(z);
            return new DataType[]{z};
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
