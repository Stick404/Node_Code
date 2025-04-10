package org.sophia.nodecode.logicSystems;

import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.core.Request;
import org.sophia.nodecode.logicSystems.types.TypeDouble;

import static org.sophia.nodecode.Utils.ID;

public class Testing {
    public static void testing() {
        NodeEnv env = new NodeEnv();
        NodeEnv.Node input1 = env.createNode(ID("double_input"), new TypeDouble(1.0));
        NodeEnv.Node input2 = env.createNode(ID("double_input"), new TypeDouble(2.0));
        NodeEnv.Node add = env.createNode(ID("node_add"));
        NodeEnv.Node print = env.createNode(ID("node_print"));

        env.connect(new Request(input1.uuid,0,add.uuid,0));
        env.connect(new Request(input2.uuid,0,add.uuid,1));
        env.connect(new Request(add.uuid,0,print.uuid,0));

        env.setRoot(print);
        env.run();
    }
}