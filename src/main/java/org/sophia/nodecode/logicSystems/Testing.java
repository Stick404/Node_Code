package org.sophia.nodecode.logicSystems;

import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.core.Request;
import org.sophia.nodecode.logicSystems.nodes.NodeAdd;
import org.sophia.nodecode.logicSystems.nodes.NodePrint;
import org.sophia.nodecode.logicSystems.nodes.inputs.NodeDoubleInput;

public class Testing {
    public static void testing() {
        System.out.println("Starting run!");

        NodeEnv env = new NodeEnv(); //start a new environment

        NodeDoubleInput input = new NodeDoubleInput(env, 1.0);
        NodeDoubleInput input2 = new NodeDoubleInput(env, 2.0);
        //Makes a new input node with TypeString as in put, and with a value of "Hello!"
        NodeAdd add = new NodeAdd(env);
        add.connect(input,0,0);
        add.connect(input2,0,1);


        NodeAdd addAgain = new NodeAdd(env);
        addAgain.connect(add, 0,0);
        addAgain.connect(input, 0, 1);


        NodePrint print = new NodePrint(env);
        print.connect(addAgain,0, 0);  //print the output of the other node

        env.run(print);

        System.out.println("Finished run!");
    }
}