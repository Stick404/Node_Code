package org.sophia.nodecode.logicSystems;

import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.core.Request;
import org.sophia.nodecode.logicSystems.nodes.NodeAdd;
import org.sophia.nodecode.logicSystems.nodes.NodeDouble;
import org.sophia.nodecode.logicSystems.nodes.NodeInput;
import org.sophia.nodecode.logicSystems.nodes.NodePrint;
import org.sophia.nodecode.logicSystems.types.TypeString;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting run!");


        NodeEnv env = new NodeEnv(); //start a new environment

        NodeDouble input1 = new NodeDouble(env,1.0); //set a Double Node with a default value of 1
        NodeDouble input2 = new NodeDouble(env,2.0); //set a Double Node with a default value of 2
        NodeAdd add = new NodeAdd(env); //make a new Add Node

        add.connect(new Request(input1, 0),0); //connect Add Slot 0 to input1 slot 0
        add.connect(new Request(input2, 0),1); //connect Add Slot 1 to input2 slot 0
        NodePrint print = new NodePrint(env); //make a new Print Node

        print.connect(new Request(add,0),0); //connect Print Slot 0 to add slot 0

        env.setRoot(print); //set the final Node to Print
        env.run(); //run it!

        System.out.println("Finished run!");


        NodeEnv env2 = new NodeEnv(); //start a new environment

        NodeInput<String> string = new NodeInput<>(env2, TypeString.class, "Hello!");
        //Makes a new input node with TypeString as in put, and with a value of "Hello!"
        NodePrint print1 = new NodePrint(env2);

        print1.connect(new Request(string,0),0);  //print the output of the other node

        env2.run(print1);
    }
}