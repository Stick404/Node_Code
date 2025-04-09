package org.sophia.nodecode.logicSystems;

import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.nodes.NodeAdd;
import org.sophia.nodecode.logicSystems.nodes.NodeAppendList;
import org.sophia.nodecode.logicSystems.nodes.NodeGetList;
import org.sophia.nodecode.logicSystems.nodes.NodePrint;
import org.sophia.nodecode.logicSystems.nodes.inputs.NodeListInput;
import org.sophia.nodecode.logicSystems.nodes.inputs.NodeDoubleInput;
import org.sophia.nodecode.logicSystems.nodes.inputs.NodeStringInput;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;

import static org.sophia.nodecode.registries.CustomRegistries.NODE_REGISTRY;
import static org.sophia.nodecode.registries.process.NodeProcess.KNOWN_NODES;

public class Testing {
    public static void testing() {
        System.out.println(KNOWN_NODES);

        System.out.println("Starting run!");

        NodeEnv env = new NodeEnv(); //start a new environment

        NodeDoubleInput input = new NodeDoubleInput(env, 1.0);
        NodeDoubleInput input2 = new NodeDoubleInput(env, 2.0);

        NodeAdd add = new NodeAdd(env);
        add.connect(input,0,0);
        add.connect(input2,0,1);


        NodeAdd addAgain = new NodeAdd(env);
        addAgain.connect(add, 0,0);
        addAgain.connect(input, 0, 1);


        NodePrint print = new NodePrint(env);
        print.connect(addAgain,0, 0);  //print the output of the other node

        env.run(input2);
        System.out.println("Finished run!");
        NodeEnv env1 = new NodeEnv();
        NodeStringInput string1 = new NodeStringInput(env1,"Hello");
        NodeStringInput string2 = new NodeStringInput(env1, "World!");
        NodeListInput array = new NodeListInput(env, new ArrayList<>());

        NodeAppendList append1 = new NodeAppendList(env);
        append1.connect(array,0,0);
        append1.connect(string1,0,1);

        NodeAppendList append2 = new NodeAppendList(env);
        append2.connect(append1,0,0);
        append2.connect(string2,0,1);

        NodeGetList get = new NodeGetList(env);
        get.connect(append2,0,0);

        NodePrint print1 = new NodePrint(env);
        print1.connect(get,0,0);

        //env.run(print1);
    }
}