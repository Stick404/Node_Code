package com.mindlesstoys.stick404.nodecode.save;

import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.UUID;

public class ClientNodeCollection {
    //Rather than using `SavedData`, this is just a singleton.
    //This is so rendering code and Client Items can refer to a global list
    private static ClientNodeCollection INSTANCE;
    private HashMap<UUID,ClientNodeStorage> nodeLocations = new HashMap<>();
    private boolean shouldClear = true;

    private ClientNodeCollection(){
    }

    public static ClientNodeCollection get(){
        if (INSTANCE == null){
            return INSTANCE = new ClientNodeCollection();
        }
        return INSTANCE;
    }

    public void clearTodo(){
        if (!shouldClear) {
            shouldClear = true;
            return;
        }
        nodeLocations.clear();
    }

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
