package org.sophia.nodecode.logicSystems.core;

import org.jetbrains.annotations.Nullable;
import org.sophia.nodecode.logicSystems.types.TypeNull;
import org.sophia.nodecode.logicSystems.types.TypeObject;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Node {
    /**
     * The abstract Node, all Node subclasses extend from this root. See {@link org.sophia.nodecode.logicSystems.nodes.NodeAdd} for an example of a basic Node
     */
    public NodeEnv env; // The Env that this node belongs too
    public Request[] inputs; // The children this node knows
    public ArrayList<Request> outputs; // The parents
    public UUID uuid; // UUID of the node

    public final static DataType<?>[] inputTypes = new DataType[]{}; //The list of DataTypes this node will take in
    public final static DataType<?>[] outputTypes = new DataType[]{}; //The list of DataTypes this node will output

    /**
     * DO NOT USE. This method is for registering the nodes, and might be removed at a later date
     */
    public Node(){
    }

    /** Use this constructor for making the nodes, rather than registering the nodes
     * @param env The NodeEnv that this node will live in
     */
    public Node(@Nullable NodeEnv env){
        this.env = env;
        this.uuid = UUID.randomUUID();
        this.inputs = new Request[getInputTypes().length];
        this.outputs = new ArrayList<>();
        env.nodes.put(this.uuid,this);
    }

    /**
     * @return The input types that this node will take
     */
    public DataType<?>[] getInputTypes() {
        return inputTypes;
    }

    /**
     * @return The output types that this node will give
     */
    public DataType<?>[] getOutputTypes() {
        return outputTypes;
    }

    /**
     * @param request Uses the record request. See {@link org.sophia.nodecode.logicSystems.core.Request} for how to use Requests
     */
    public void connect(Request request) {
        this.inputs[request.targetSlot()] = request;
        request.node().parent(new Request(this,request.pullSlot(),request.targetSlot()));
    }

    /** This is an overload for {@link Node#connect(Request)}
     * @param node - The {@link Node} this request will point to
     * @param pullSlot The slot this node will try to pull from
     * @param targetSlot The target this request will try to push from
     */
    public void connect(Node node, Integer pullSlot, Integer targetSlot){
        connect(new Request(node, pullSlot, targetSlot));
    }

    /**
     * DO NOT USE. This is just for the connect method. This will be called by the parent when connecting
     */
    public void parent(Request request){ //SHOULD ONLY BE USED IN THE CONNECT METHOD
        this.outputs.add(request);
    }

    /** This is to disconnect a slot of the node. Currently, does not disconnect any parents
     * @param slot The slot of the node to set too null
     */
    public void disconnect(Integer slot){
        this.inputs[slot] = null;
    }

    /**
     * @param inputs A list of {@link DataType}s formated with either null, or {@link Node#inputTypes}
     * @return A list of {@link DataType}s formated with either null, or {@link Node#outputTypes}
     */
    protected DataType<?>[] run(DataType<?>[] inputs) {
        return new DataType[getOutputTypes().length];
    }

    /**
     * @return The list input types. Formated over in {@link Node#inputTypes}
     */
    public Request[] getInputs() {
        return inputs;
    }

    /**
     * @return The list output types. Formated over in {@link Node#outputTypes}
     */
    public ArrayList<Request> getOutputs() {
        return outputs;
    }

    /**
     * @return the {@link UUID} of this node
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     *  A protected method to return the value of a slot during Node Runtime. Meant to be a smarter replacement than manually casting Nodes to their values
     * @param inputs The input list to "parse," should be formated according too {@link Node#run(DataType[])}'s inputs
     * @param slot The slot to try to read from, ranges from 0 to {@link Node#inputTypes}'s max length
     * @return The wanted value of {@link Node#inputTypes}
     * @param <T> The value that is wanted. Should be the same as {@link Node#inputTypes}
     */
    @Nullable
    protected <T> T getSlot(DataType<?>[] inputs, Integer slot){
        if (slot > inputs.length) throw new NodeExecutionError("tried to get output outside of bounds, target: " + slot + " max:" + (inputs.length -1));
        var value = inputs[slot];

        if (value.getClass().isInstance(this.getInputTypes()[slot])){
            //If the value is the one that is wanted, return it as said value
            return (T) this.getInputTypes()[slot].getClass().cast(value).getData();

        } else if (this.getInputTypes()[slot].getClass() == TypeObject.class) {
            //If the value is marked as "Object"/"Any," then return the raw DataType<?>
            return (T) inputs[slot];

        } else if (value.getClass() == TypeNull.class) {
            //If the value is given is a TypeNull, then just return null. This is for if we want a Nullable input
            return null;
        }
        //If all the if-checks fail, throw a NodeCastError to be picked up by the main Eval
        throw new NodeCastError(this.getInputTypes()[slot].getClass(),value.getClass());
    }
}
