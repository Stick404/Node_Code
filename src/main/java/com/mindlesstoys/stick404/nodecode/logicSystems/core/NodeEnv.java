package com.mindlesstoys.stick404.nodecode.logicSystems.core;

import com.mindlesstoys.stick404.nodecode.save.ServerNodeStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mindlesstoys.stick404.nodecode.logicSystems.types.TypeNull;
import com.mindlesstoys.stick404.nodecode.logicSystems.types.TypeObject;

import java.util.*;

import static com.mindlesstoys.stick404.nodecode.registries.process.RegistryProcess.KNOWN_NODES;

public class NodeEnv {
    private static final Logger log = LogManager.getLogger(NodeEnv.class);
    public Node root; // Root is the last node in the graph
    public HashMap<UUID, Node> nodes; // All known nodes
    public Stack<Node> toRun; // The order to run all nodes TODO: maybe make this a Stack of UUIDS? And contact NodeEnv#nodes to get the Nodes
    public HashMap<UUID, DataType<?>[]> outputs; // Holds all the outputs of nodes
    private final UUID parentStorage;

    public NodeEnv(){
        outputs = new HashMap<>();
        nodes = new HashMap<>();
        toRun = new Stack<>();
        parentStorage = null; //because there is no parent storage when a NodeEnv is made this way
    }
    public NodeEnv(ServerNodeStorage storage){
        outputs = new HashMap<>();
        nodes = new HashMap<>();
        toRun = new Stack<>();
        parentStorage = storage.getUuid();
    }

    /** Creates a new NodeEnv via a {@link CompoundTag}
     * @param tag A {@link CompoundTag} to read into a new NodeEnv
     */
    public NodeEnv(CompoundTag tag, UUID parentStorage){
        this.outputs = new HashMap<>();
        this.nodes = new HashMap<>();
        this.toRun = new Stack<>();
        this.parentStorage = parentStorage;

        ListTag nodes = tag.getList("nodes",Tag.TAG_COMPOUND);
        for (var temp : nodes){
            CompoundTag nodeT = (CompoundTag) temp;
            Node node = new Node(nodeT);
            this.nodes.put(node.uuid,node);
        }

        ListTag toRun = tag.getList("toRun", Tag.TAG_COMPOUND);
        for (var temp : toRun){
            CompoundTag nodeT = (CompoundTag) temp;
            this.toRun.push(new Node(nodeT));
        }

        if (tag.contains("root")) this.root = this.nodes.get(tag.getUUID("root"));
    }

    /** Takes the root node (final node normally), and goes up and down the graph of nodes. This also sets the {@link NodeEnv#toRun} to the stack.
     * @return A Stack of {@link Node} to be run in that order
     */
    public Stack<Node> read() {
        if (this.root == null){
            throw new NodeExecutionError("Root is Null");
        }

        Stack<Node> finding = new Stack<>();
        Stack<Node> toRun = new Stack<>();
        finding.add(root);

        while (!finding.empty()){
            var top = finding.pop();

            toRun.add(top);
            for (Request child : top.inputs){
                if (child != null){
                    toRun.remove(nodes.get(child.source())); //If a node is already in the stack, move it up
                    finding.push(nodes.get(child.source()));
                }
            }
            if (!toRun.contains(top)){
                for (Request parent : top.outputs) {
                    //we only want the parents added so we can keep the order of running correct
                    finding.push(nodes.get(parent.target()));
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

    public void setRoot(UUID node){
        this.root = nodes.get(node);
    }

    public UUID getRoot(){
        return this.root.uuid;
    }

    public boolean isRoot(){
        return this.root != null;
    }

    /**
     * Runs the node env. Requires a Root Node to be set fist. <br>
     * Also calls {@link NodeEnv#read()} if {@link NodeEnv#toRun} is null
     */
    public void run(){
        try {
            if (toRun == null) {read();}
            for (Node node : this.toRun) {

                DataType<?>[] inputs = new DataType[node.inputs.length];
                int i = 0;
                //do type checking here
                if (node.inputs.length > 0){
                    for (Request request : node.inputs) { //check if input of prev node connects to the right type
                        //if Class wanted is not input class, or any, then throw an error
                        if (request != null) {
                            //mess of if statements, but fuck it
                            //What this is doing: is output type same as input type? If not, error
                            if (this.nodes.get(request.source()).outputTypes[request.pullSlot()].getClass() != node.inputTypes[i].getClass()
                                    && node.inputTypes[request.targetSlot()].getClass() != TypeObject.class) {
                                throw new NodeExecutionError("Incorrect node type found, wanted: " + node.inputTypes[request.pullSlot()] +
                                        " got: " + nodes.get(request.source()).outputTypes[request.pullSlot()]);
                            }
                            inputs[i] = this.outputs.get(request.source())[request.pullSlot()];
                        } else {
                            inputs[i] = new TypeNull(); //in case there is no input, make it null
                        }
                        i++;
                    }
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
                }
            }
        } catch (RuntimeException e) {
            log.error("e: ", e); //Handle the error :clueless:
        }
        outputs.clear(); //so you cant read the old data of nodes
    }


    /**
     * @param nodeRL The {@link ResourceLocation} (EG: "nodecode:node_add") of the node to run
     * @return The Node created
     */
    public UUID createNode(ResourceLocation nodeRL){
        Node link = new Node(nodeRL);
        this.nodes.put(link.uuid,link);
        return link.uuid;
    }

    public UUID createNode(ResourceLocation nodeRl, Vec3 pos){
        Node link = new Node(nodeRl);
        link.position = pos;
        this.nodes.put(link.uuid,link);
        return link.uuid;
    }

    /**
     * @param nodeRL The {@link ResourceLocation} (EG: "nodecode:node_add") of the node to run
     * @param extra The Extra data a Node might need to be run (EG: Input nodes and default values)
     * @return The Node created
     */
    public UUID createNode(ResourceLocation nodeRL, DataType<?> extra){
        Node link = new Node(nodeRL);
        link.extra = extra;
        this.nodes.put(link.uuid,link);
        return link.uuid;
    }

    /** Sets a connection between nodes
     * @param link The "Parent" node the request will point from
     * @param request The request stating where to output from, and what slots to target
     */
    public void connect(UUID link, Request request){
        if (!this.nodes.containsKey(request.source()) && !this.nodes.containsKey(request.target())) throw new NodeExecutionError("Node was not found in NodeEnv!");

        var parent = nodes.get(link);
        var child = nodes.get(request.source());
        parent.inputs[request.targetSlot()] = request;
        child.outputs.add(request);
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
        child.outputs.remove(request);
    }

    /** Disconnects 2 nodes
     * @param request The request stating what nodes and slots to disconnect
     */
    public void disconnect(Request request){
        var child = nodes.get(request.source());
        var parent = nodes.get(request.target());

        child.outputs.remove(request);
        parent.inputs[request.targetSlot()] = null;
    }

    /** Not recommended to use, instead use other methods that cleanly interact with the NodeEnv System
     * @param uuid The {@link UUID} of a Node
     * @return a {@link Node}
     */
    public Node retrieveNode(UUID uuid){
        return this.nodes.get(uuid);
    }

    /** Converts the NodeEnv into a CompoundTag
     * @return A {@link CompoundTag} representing the NodeEnv
     */
    public CompoundTag save(){
        CompoundTag tag = new CompoundTag();

        if (isRoot()){
            tag.putUUID("root",root.uuid);
        }

        ListTag nodesTag = new ListTag();
        for (var temp : nodes.entrySet()){
            var node = temp.getValue();
            nodesTag.add(node.save());
        }
        tag.put("nodes",nodesTag);

        ListTag toRunTag = new ListTag();
        for (var node : toRun) {
            toRunTag.add(node.save());
        }
        tag.put("toRun",toRunTag);
        return tag;
    }

    /**
     * A node "just" holds:
     * <ul>
     * <li>The {@link ResourceLocation} of the function to run</li>
     * <li>The Array of Input Types of the function</li>
     * <li>The Array of Output Types of the function</li>
     * <li>Any Extra data that the Node may require (mostly used for Input Nodes)</li>
     * <li>The array of {@link Request}s for inputs. The Length is equal to the defined {@link Func}'s inputTypes</li>
     * <li>The array of {@link Request}s for outputs. The Length is equal to the defined {@link Func}'s outputTypes</li>
     * <li>The UUID of the node</li>
     * <li>The position of the node in 3d. Off centered on the NodeArray center block</li>
     *</ul>
     *
     */
    public static class Node {
        final public ResourceLocation function; //the function/node to run
        Request[] inputs; //the input requests
        List<Request> outputs; //the output requests
        DataType<?> extra;
        public final DataType<?>[] inputTypes, outputTypes;
        public final UUID uuid; //the UUID of the link
        public Vec3 position = new Vec3(0,3,0); //where the node its self is on the Node Plane

        /**
         * @param nodeRL The {@link ResourceLocation} (EG: "nodecode:node_add") of the node to run
         */
        protected Node(ResourceLocation nodeRL) {
            Func nodeC = KNOWN_NODES.get(nodeRL);
             this.function = nodeRL;
            this.inputTypes = nodeC.getInputTypes();
            this.outputTypes = nodeC.getOutputTypes();
            this.inputs = new Request[nodeC.getInputTypes().length];
            this.outputs = new ArrayList<>();
            this.uuid = UUID.randomUUID();
        }

        /** Creates a new Node via a {@link CompoundTag}
         * @param tag A {@link CompoundTag} to read into a new Node
         */
        public Node(CompoundTag tag){
            this.function = ResourceLocation.parse(tag.getString("function"));
            Func nodeC = KNOWN_NODES.get(function);
            this.uuid = tag.getUUID("uuid");
            this.inputTypes = nodeC.getInputTypes();
            this.outputTypes = nodeC.getOutputTypes();

            ListTag inputs = tag.getList("inputs", Tag.TAG_COMPOUND);
            this.inputs = new Request[nodeC.getInputTypes().length];
            for (var temp : inputs){
                CompoundTag in = (CompoundTag) temp;
                this.inputs[in.getInt("slot")] = Request.load(in.getCompound("request"));
            }

            ListTag outputs = tag.getList("outputs", Tag.TAG_COMPOUND);
            this.outputs = new ArrayList<>();
            for (var temp : outputs){
                CompoundTag in = (CompoundTag) temp;
                this.outputs.add(Request.load(in));
            }

            if (tag.contains("extra")) this.extra = DataType.load(tag.getCompound("extra"));

            this.position = new Vec3(tag.getFloat("posX"),tag.getFloat("posY"),tag.getFloat("posZ"));
        }

        /** Converts the Node into a CompoundTag
         * @return A {@link CompoundTag} representing the Node
         */
        public CompoundTag save(){
            CompoundTag tag = new CompoundTag();
            tag.putString("function",function.toString());

            int i;
            ListTag inputTags = new ListTag();
            for (i = 0; i < inputs.length; i++){
                if (inputs[i] != null) {
                    CompoundTag in = new CompoundTag();
                    in.putInt("slot", i);
                    in.put("request", inputs[i].save());
                    inputTags.add(in);
                }
            }
            tag.put("inputs",inputTags);

            ListTag outputTag = new ListTag();
            for (var output : outputs){
                outputTag.add(output.save());
            }
            tag.put("outputs",outputTag);

            tag.putUUID("uuid",uuid);

            if (this.extra != null) tag.put("extra", this.extra.save());

            tag.putDouble("posX", this.position.x);
            tag.putDouble("posY", this.position.y);
            tag.putDouble("posZ", this.position.z);

            return tag;
        }
    }
}
