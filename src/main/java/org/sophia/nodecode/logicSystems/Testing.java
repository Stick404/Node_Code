package org.sophia.nodecode.logicSystems;

import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.core.Request;
import org.sophia.nodecode.logicSystems.nodes.NodeInput;
import org.sophia.nodecode.logicSystems.nodes.NodePrint;
import org.sophia.nodecode.logicSystems.types.TypeDouble;

public class Testing {
    public static void testing() {
        System.out.println("Starting run!");

        NodeEnv env2 = new NodeEnv(); //start a new environment

        NodeInput string = new NodeInput(env2, new TypeDouble(), 9999.0);
        //Makes a new input node with TypeString as in put, and with a value of "Hello!"
        NodePrint print1 = new NodePrint(env2);

        print1.connect(new Request(string,0),0);  //print the output of the other node

        env2.run(print1);

        System.out.println("Finished run!");
    }
}