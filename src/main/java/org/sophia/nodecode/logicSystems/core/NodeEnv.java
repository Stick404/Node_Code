package org.sophia.nodecode.logicSystems.core;

import net.minecraft.resources.ResourceLocation;
import org.sophia.nodecode.logicSystems.types.TypeNull;
import org.sophia.nodecode.logicSystems.types.TypeObject;

import java.util.*;

import static org.sophia.nodecode.registries.process.NodeProcess.KNOWN_NODES;

public class NodeEnv {
    public Node root; // Root is the last node in the graph
    public HashMap<UUID, Node> nodes; // All known nodes
    public Stack<Node> toRun; // The order to run all nodes
    public HashMap<UUID, DataType<?>[]> outputs; // Holds all the outputs of nodes

    public NodeEnv(){
        outputs = new HashMap<>();
        nodes = new HashMap<>();
    }

    /** Takes the root node (final node normally), and goes up and down the graph of nodes. This also sets the {@link NodeEnv#toRun} to the stack.
     * @return A Stack of {@link Node} to be run in that order
     */
    public Stack<Node> read() {
        if (this.root == null){
            throw new NodeExecutionError("Root is Null");
        }

        Stack<Node> finding = new Stack<>();
        List<Node> toRun = new Stack<>();
        HashSet<Node> found = new HashSet<>();
        finding.add(root);

        while (!finding.empty()){
            var top = finding.pop();

            toRun.add(top);
            for (Request child : top.inputs){
                if (child != null){
                    toRun.remove(nodes.get(child.target())); //If a node is already in the stack, move it up
                    finding.push(nodes.get(child.target()));
                }
            }
            if (!found.contains(top)){
                for (Request parent : top.outputs) {
                    found.add(top); //we only want the parents added so we can keep the order of running correct
                    finding.push(nodes.get(parent.source()));
                }
            }
        }

        Stack<Node> output = new Stack<>();
        output.addAll(toRun.reversed());
        this.toRun = output;
        return this.toRun; //returns the Stack in case it needs to be stored
    }

    public Stack<Node> read(Node node) {
        this.root = node;
        return read();
    }

    //actually runs the NodeEnv
    public void run(){
        try {
            if (toRun == null) {read();}
            for (Node node : this.toRun) {

                DataType<?>[] inputs = new DataType[node.inputs.length];
                int i = 0;

                //do type checking here
                for (Request input : node.inputs) { //check if input of prev node connects to the right type
                    //if Class wanted is not input class, or any, then throw an error
                    if (input != null) {
                        //mess of if statements, but fuck it
                        if (nodes.get(input.source()).outputTypes[input.pullSlot()].getClass() != node.inputTypes[i].getClass()
                                && node.inputTypes[i].getClass() != TypeObject.class) {
                            //if the type was wrong, or not any, then throw an error
                            throw new NodeExecutionError("Incorrect node type found, wanted: " + node.inputTypes[i] +
                                    " got: " + nodes.get(input.source()).outputTypes[input.pullSlot()]);
                        }
                        inputs[i] = this.outputs.get(input.source())[input.pullSlot()];
                    } else {
                        inputs[i] = new TypeNull(); //in case there is no input, make it null
                    }
                    i++;
                }
                DataType<?>[] output;
                //Runs the node!
                try {
                    Func func = KNOWN_NODES.get(node.function);
                    output = func.run(inputs, node.extra);
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


    /**
     * @param nodeRL The {@link ResourceLocation} (EG: "nodecode:node_add") of the node to run
     * @return The Node created
     */
    public Node createNode(ResourceLocation nodeRL){
        Node link = new Node(nodeRL);
        this.nodes.put(link.uuid,link);
        return link;
    }

    /**
     * @param nodeRL The {@link ResourceLocation} (EG: "nodecode:node_add") of the node to run
     * @param extra The Extra data a Node might need to be run (EG: Input nodes and default values)
     * @return The Node created
     */
    public Node createNode(ResourceLocation nodeRL, DataType<?> extra){
        Node link = new Node(nodeRL);
        link.extra = extra;
        this.nodes.put(link.uuid,link);
        return link;
    }

    /** Sets a connection between nodes
     * @param link The "Parent" node the request will point from
     * @param request The request stating where to output from, and what slots to target
     */
    public void connect(UUID link, Request request){
        var parent = nodes.get(link);
        var child = nodes.get(request.source());
        parent.inputs[request.targetSlot()] = request;
        child.outputs.set(request.pullSlot(), request);
    }

    /** Sets a connection between nodes
     * @param request The request stating where to output from, and what slots to target
     */
    public void connect(Request request){
        connect(request.target(),request);
    }

    /** Disconnects 2 nodes
     * @param link The Child/Source node to disconnect
     * @param slot The output slot to disconnect the parent/output from
     */
    public void disconnect(UUID link, Integer slot){
        var child = nodes.get(link); //gets the child link
        var request = child.outputs.get(slot); //gets request that holds all the data
        var parent = nodes.get(request.target()); //gets the parent link

        parent.inputs[request.targetSlot()] = null;
        child.outputs.set(slot, null);
    }

    /** Disconnects 2 nodes
     * @param request The request stating what nodes and slots to disconnect
     */
    public void disconnect(Request request){
        var child = nodes.get(request.source());
        var parent = nodes.get(request.target());

        child.outputs.set(request.pullSlot(), null);
        parent.inputs[request.targetSlot()] = null;
    }

    /**
     * A node just holds:
     * <ul>
     * <li>The {@link ResourceLocation} of the function to run</li>
     * <li>The Array of Input Types of the function</li>
     * <li>The Array of Output Types of the function</li>
     * <li>Any Extra data that the Node may require (mostly used for Input Nodes)</li>
     * <li>The array of {@link Request}s for inputs. The Length is equal to the defined {@link Func}'s inputTypes</li>
     * <li>The array of {@link Request}s for outputs. The Length is equal to the defined {@link Func}'s outputTypes</li>
     * <li>The UUID of the node</li>
     *</ul>
     *
     */
    public class Node {
        final ResourceLocation function; //the function/node to run
        Request[] inputs; //the input requests
        List<Request> outputs; //the output requests
        DataType<?> extra;
        final DataType<?>[] inputTypes, outputTypes;
        final UUID uuid; //the UUID of the link

        /**
         * @param nodeRL The {@link ResourceLocation} (EG: "nodecode:node_add") of the node to run
         */
        protected Node(ResourceLocation nodeRL) {
            Func nodeC = KNOWN_NODES.get(nodeRL);
            this.function = nodeRL;
            inputTypes = nodeC.getInputTypes();
            outputTypes = nodeC.getOutputTypes();
            inputs = new Request[nodeC.getInputTypes().length];
            outputs = new ArrayList<>();
            uuid = UUID.randomUUID();
        }
    }
}
