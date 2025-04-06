package org.sophia.nodecode.logicSystems.core;

import org.sophia.nodecode.logicSystems.types.TypeObject;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

import static org.sophia.nodecode.registries.NodeRegistry.NODES;

public class NodeEnv {
    public Node root; // Root is the last node in the graph
    public HashMap<UUID,Node> nodes; // All known nodes
    public Stack<? extends Node> toRun; // The order to run all nodes
    public HashMap<UUID, DataType<?>[]> outputs; // Holds all the outputs of nodes

    public NodeEnv(){
        outputs = new HashMap<>();
        nodes = new HashMap<>();
    }

    public NodeEnv(Node root){
        this();
        this.root = root;
    }

    public void addNode() {

    }

    //prepares the NodeEnv to be ran or saving
    public Stack<? extends Node> read() {
        if (this.root == null){
            throw new NodeExecutionError("Root is Null");
        }
        //TODO: Make this not make an inf loop!

        Stack<Node> finding = new Stack<>();
        List<Node> toRun = new Stack<>();
        finding.add(root);
        while (!finding.empty()){
            var top = finding.pop();
            if (!NODES.getEntries().contains(top.getClass())) throw new NodeExecutionError("Found unregistered node of: " + top.getClass());

            toRun.add(top);
            for (Request child : top.getInputs()){
                if (child != null){
                    toRun.remove(child.node()); //If a node is already in the stack, move it up
                    finding.push(child.node());
                }
            }
        }

        Stack<Node> output = new Stack<>();
        output.addAll(toRun.reversed());
        this.toRun = output;
        return this.toRun; //returns the Stack in case it needs to be stored
    }

    public Stack<? extends Node> read(Node node) {
        this.root = node;
        return read();
    }

    //actually runs the NodeEnv
    public void run(){
        try {
            if (toRun == null) {read();}
            for (Node node : this.toRun) {

                DataType<?>[] inputs = new DataType[node.getInputs().length];
                int i = 0;

                //do type checking here
                for (var input : node.getInputs()) { //check if input of prev node connects to the right type
                    //if Class wanted is not input, or any, then throw an error
                    if (input.node().getOutputTypes()[input.pullSlot()].getClass() != node.getInputTypes()[i].getClass() && node.getInputTypes()[i].getClass() != TypeObject.class){
                        throw new NodeExecutionError("Incorrect node type found, wanted: " + node.getInputTypes()[i] +
                                " got: " + input.node().getOutputTypes()[input.pullSlot()]);
                    }

                    inputs[i] = this.outputs.get(input.node().uuid)[input.pullSlot()];
                    i++;
                }
                var output = node.run(inputs);

                if (output.length > 0 && output[0] != null) {
                    outputs.put(node.uuid, output);
                }
            }
        } catch (RuntimeException e) {
            System.out.println(e); //Handle the error :clueless:
            System.out.println(e.fillInStackTrace());
        }
        outputs.clear(); //so you cant read the old data of nodes
    }

    public void run(Node node){
        setRoot(node);
        run();
    }

    public void setRoot(Node node){
        this.root = node;
    }
}
