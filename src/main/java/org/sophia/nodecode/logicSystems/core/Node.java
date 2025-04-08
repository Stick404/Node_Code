package org.sophia.nodecode.logicSystems.core;

import org.jetbrains.annotations.Nullable;
import org.sophia.nodecode.logicSystems.types.TypeNull;
import org.sophia.nodecode.logicSystems.types.TypeObject;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Node {
    public NodeEnv env; //the Env that this node belongs too
    public Request[] inputs;
    public ArrayList<Request> outputs; //the children this node knows
    public UUID uuid; //UUID of the node

    public final static DataType[] inputTypes = new DataType[]{};
    public final static DataType[] outputTypes = new DataType[]{};

    public Node(){
    }

    public Node(@Nullable NodeEnv env){
        this.env = env;
        this.uuid = UUID.randomUUID();
        this.inputs = new Request[getInputTypes().length];
        this.outputs = new ArrayList<>();
        env.nodes.put(this.uuid,this);
    }

    public DataType[] getInputTypes() {
        return inputTypes;
    }

    public DataType[] getOutputTypes() {
        return outputTypes;
    }

    //Connects a node to a specific slot
    public void connect(Request request) {
        this.inputs[request.targetSlot()] = request;
        request.node().parent(new Request(this,request.pullSlot(),request.targetSlot()));
    }

    public void parent(Request request){ //SHOULD ONLY BE USED IN THE CONNECT METHOD
        this.outputs.add(request);
    }

    public void connect(Node node, Integer pullSlot, Integer targetSlot){
        connect(new Request(node, pullSlot, targetSlot));
    }

    //Removes a node from a specific slot
    public void disconnect(Integer slot){
        this.inputs[slot] = null;
    }

    //Returns an array of outputs
    protected DataType<?>[] run(DataType<?>[] inputs) {
        DataType<?>[] outputs = new DataType[getOutputTypes().length];
        return outputs;
    }

    public Request[] getInputs() {
        return inputs;
    }

    public ArrayList<Request> getOutputs() {
        return outputs;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Nullable
    protected <T> T getSlot(DataType<?>[] inputs, Integer slot){
        if (slot > inputs.length) throw new NodeExecutionError("tried to get output outside of bounds, target: " + slot + " max:" + (inputs.length -1));
        var value = inputs[slot];

        if (value.getClass().isInstance(this.getInputTypes()[slot])){
            return (T) this.getInputTypes()[slot].getClass().cast(value).getData();

        } else if (this.getInputTypes()[slot].getClass() == TypeObject.class) {
            return (T) inputs[slot];

        } else if (value.getClass() == TypeNull.class) {
            return null;

        }
        throw new NodeCastError(this.getInputTypes()[slot].getClass(),value.getClass());
    }
}
