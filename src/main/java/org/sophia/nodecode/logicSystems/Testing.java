package org.sophia.nodecode.logicSystems;

import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.core.Request;
import org.sophia.nodecode.logicSystems.types.TypeDouble;

import java.util.UUID;

import static org.sophia.nodecode.Utils.ID;

public class Testing {
    public static void testing() {

        NodeEnv env = new NodeEnv();
        //Create the Node Environment

        UUID input1 = env.createNode(ID("double_input"), new TypeDouble(1.0));
        //Create a "double input" that supplies 1
        UUID input2 = env.createNode(ID("double_input"), new TypeDouble(2.0));
        //Create a "double input" that supplies 2
        UUID add = env.createNode(ID("node_add"));
        //Create a node that adds 2 doubles
        UUID print = env.createNode(ID("node_print"));
        //Create a node that will print out the double

        env.connect(new Request(input1,0,add,0));
        //Connect input1 to add
        env.connect(new Request(input2,0,add,1));
        //Connect input2 to add
        env.connect(new Request(add,0,print,0));
        //Connect add to print

        env.setRoot(print);
        //Set print to the root node

        env.run();
        //Evaluate the system
        //What this system will do is add 1.0 and 2.0 and print out 3.0
        NodeEnv e = new NodeEnv();
        UUID in1 = e.createNode(ID("double_input"), new TypeDouble(1.0));
        UUID in2 = e.createNode(ID("double_input"), new TypeDouble(1.0));
        UUID in3 = e.createNode(ID("double_input"), new TypeDouble(1.0));
        UUID in4 = e.createNode(ID("double_input"), new TypeDouble(1.0));

        UUID ad1 = e.createNode(ID("node_add"));
        UUID ad2 = e.createNode(ID("node_add"));
        UUID ad3 = e.createNode(ID("node_add"));

        UUID pri = e.createNode(ID("node_print"));

        e.connect(new Request(in1,0,ad1,0));
        e.connect(new Request(in2,0,ad1,1));

        e.connect(new Request(ad1,0,ad2,0));
        e.connect(new Request(in3,0,ad2,1));

        e.connect(new Request(ad2,0,ad3,0));
        e.connect(new Request(in4,0,ad3,1));

        e.connect(new Request(ad3,0,pri,0));

        e.setRoot(pri);
        e.run();
    }
}