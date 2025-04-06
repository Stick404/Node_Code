package org.sophia.nodecode.logicSystems;

import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.core.Request;
import org.sophia.nodecode.logicSystems.nodes.NodeAdd;
import org.sophia.nodecode.logicSystems.nodes.NodeInput;
import org.sophia.nodecode.logicSystems.nodes.NodePrint;
import org.sophia.nodecode.logicSystems.types.TypeDouble;

public class Testing {
    public static void testing() {
        System.out.println("Starting run!");

        NodeEnv env = new NodeEnv(); //start a new environment

        NodeInput input = new NodeInput(env, new TypeDouble(), 1.0);
        NodeInput input2 = new NodeInput(env, new TypeDouble(), 2.0);
        //Makes a new input node with TypeString as in put, and with a value of "Hello!"
        NodeAdd add = new NodeAdd(env);
        add.connect(new Request(input,0),0);
        add.connect(new Request(input2,0),1);

        NodePrint print1 = new NodePrint(env);
        print1.connect(new Request(add,0),0);  //print the output of the other node

        env.run(print1);

        System.out.println("Finished run!");
    }
}