package org.sophia.nodecode.logicSystems.core;

import org.sophia.nodecode.logicSystems.types.TypeNull;
import org.sophia.nodecode.logicSystems.types.TypeObject;

import java.util.*;

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

    //prepares the NodeEnv to be ran or saving
    public Stack<? extends Node> read() {
        if (this.root == null){
            throw new NodeExecutionError("Root is Null");
        }
        //TODO: Make this not make an inf loop!

        Stack<Node> finding = new Stack<>();
        List<Node> toRun = new Stack<>();
        HashSet<Node> found = new HashSet<>(); // so we dont loop from parents to children
        finding.add(root);

        HashSet<Class<? extends Node>> registeredTypes = new HashSet<>();
        for (var z : NODES.getEntries()) {
            registeredTypes.add(z.get().getClass());
        }
        while (!finding.empty()){
            var top = finding.pop();
            if (!registeredTypes.contains(top.getClass())) throw new NodeExecutionError("Tried to run unregistered node of: " + top.getClass());

            toRun.add(top);
            for (Request child : top.getInputs()){
                if (child != null){
                    toRun.remove(child.node()); //If a node is already in the stack, move it up
                    finding.push(child.node());
                }
            }
            if (!found.contains(top)){
                for (Request parent : top.getOutputs()) {
                    found.add(top); //we only want the parents added so we can keep the order of running correct
                    finding.push(parent.node());
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

                DataType<?>[] inputs = new DataType[node.getInputs().length +1];
                int i = 0;

                //do type checking here
                for (var input : node.getInputs()) { //check if input of prev node connects to the right type
                    //if Class wanted is not input class, or any, then throw an error
                    if (input != null) {
                        //mess of if statements, but fuck it
                        if (input.node().getOutputTypes()[input.pullSlot()].getClass() != node.getInputTypes()[i].getClass()
                                && node.getInputTypes()[i].getClass() != TypeObject.class) {
                            //if the type was wrong, or not any, then throw an error
                            throw new NodeExecutionError("Incorrect node type found, wanted: " + node.getInputTypes()[i] +
                                    " got: " + input.node().getOutputTypes()[input.pullSlot()]);
                        }
                        inputs[i] = this.outputs.get(input.node().uuid)[input.pullSlot()];
                    } else {
                        inputs[i] = new TypeNull(); //in case there is no input, make it null
                    }
                    i++;
                }
                DataType<?>[] output = null;
                try {
                    output = node.run(inputs);
                } catch (NullPointerException e) {
                    throw new NodeExecutionError("Null Value found!");
                }
                if (output.length > 0 && output[0] != null) {
                    outputs.put(node.uuid, output);
                    //System.out.println(output[0].getData());
                }
            }
        } catch (RuntimeException e) {
            System.out.println(e); //Handle the error :clueless:
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
