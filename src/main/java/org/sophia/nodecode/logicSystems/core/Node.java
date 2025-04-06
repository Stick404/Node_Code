package org.sophia.nodecode.logicSystems.core;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class Node {
    public NodeEnv env; //the Env that this node belongs too
    public Request[] inputs, outputs; //the children this node knows
    public UUID uuid; //UUID of the node

    public final static DataType[] inputTypes = new DataType[]{};
    public final static DataType[] outputTypes = new DataType[]{};

    public Node(){
    }

    public Node(@Nullable NodeEnv env){
        this.env = env;
        this.uuid = UUID.randomUUID();
        this.inputs = new Request[getInputTypes().length];
        this.outputs = new Request[getOutputTypes().length];
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

    public Request[] getOutputs() {
        return outputs;
    }

    public UUID getUuid() {
        return uuid;
    }
}
