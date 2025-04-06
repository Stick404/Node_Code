package org.sophia.nodecode.logicSystems.core;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class Node {
    public NodeEnv env; //the Env that this node belongs too
    public Request[] inputs, outputs; //the children this node knows
    public UUID uuid; //UUID of the node
    public Class<? extends DataType<?>>[] inputTypes;
    public Class<? extends DataType<?>>[] outputTypes;

    public Node(){
        this(null);
    }

    public Node(@Nullable NodeEnv env){
        this.env = env;
        this.uuid = UUID.randomUUID();
        this.inputs = new Request[inputTypes.length];
        env.nodes.put(this.uuid,this);
    }

    public DataType[] getInputTypes() {
        return new DataType[0];
    }

    public DataType[] getOutputTypes() {
        return new DataType[0];
    }

    //Connects a node to a specific slot
    public void connect(Request request,Integer thisSlot) {
        this.inputs[thisSlot] = request;
    }

    //Removes a node from a specific slot
    public void disconnect(Integer slot){
        this.inputs[slot] = null;
    }

    //Returns an array of outputs
    protected DataType<?>[] run() {
        DataType<?>[] outputs = new DataType[this.outputTypes.length];
        return outputs;
    }

    protected DataType<?> get(Integer slot) {
        Request request = this.inputs[slot];
        if (request == null) throw new NodeExecutionError("No input node found for slot: " + slot);

        return this.env.outputs.get(request.node().uuid)[request.slot()];
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
