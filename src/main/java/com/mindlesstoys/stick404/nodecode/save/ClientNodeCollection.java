package com.mindlesstoys.stick404.nodecode.save;

import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.UUID;

//TODO: Make a NodeCollection interface
//TODO: Make a NodeStorage interface
public class ClientNodeCollection {
    //Rather than using `SavedData`, this is just a singleton.
    //This is so rendering code and Client Items can refer to a global list
    private static ClientNodeCollection INSTANCE;
    private HashMap<UUID,ClientNodeStorage> nodeLocations = new HashMap<>();
    private boolean shouldClear = true;

    private ClientNodeCollection(){
    }

    /**
     * @return The singleton of {@link ClientNodeCollection}
     */
    public static ClientNodeCollection get(){
        if (INSTANCE == null){
            return INSTANCE = new ClientNodeCollection();
        }
        return INSTANCE;
    }

    /**
     * Clears the {@link ClientNodeCollection#nodeLocations}, and resets {@link ClientNodeCollection#shouldClear} to true
     */
    public void clearTodo(){
        if (!shouldClear) {
            shouldClear = true;
            return;
        }
        nodeLocations.clear();
    }

    /**
     * @param shouldClear a boolean of if the next {@link ClientNodeCollection#clearTodo()} should clear it or not
     */
    public void setShouldClear(boolean shouldClear) {
        this.shouldClear = shouldClear;
    }

    public void addStorage(ClientNodeStorage storage){
        this.nodeLocations.put(storage.uuid,storage);
    }

    public void addStorage(UUID uuid, BlockPos centerBlock, BlockPos dir){
        this.nodeLocations.put(uuid,new ClientNodeStorage(uuid,centerBlock,dir));
    }

    public void updateStorage(UUID uuid, ClientNodeStorage storage){
        this.nodeLocations.replace(uuid,storage);
    }

    public void removeStorage(UUID uuid){
        this.nodeLocations.remove(uuid);
    }
    public HashMap<UUID,ClientNodeStorage> getNodeLocations(){
        return this.nodeLocations;
    }
}
