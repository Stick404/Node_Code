package org.sophia.nodecode.save;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import static org.sophia.nodecode.Nodecode.LOGGER;

public class NodeCollection extends SavedData {
    public static Factory<NodeCollection> factory = new SavedData.Factory<>(NodeCollection::create,NodeCollection::load);
    HashSet<BlockPos> extensions = new HashSet<>();
    //Store all known blocks
    HashMap<UUID,NodeStorage> nodeLocations = new HashMap<>();
    //store all known "Node Sets"
    //TODO: Re-add in Saving and Loading
    //TODO: Add in Y values
    //TODO: Make this work in all dirs, not just north

    public static NodeCollection create() {
        return new NodeCollection();
    }

    public void createNodeLocation(UUID uuid, BlockPos pos){
        NodeStorage storage = new NodeStorage(uuid,pos);
        nodeLocations.put(uuid,storage);
        extensions.add(pos);
        LOGGER.info("making new Location!");
        this.setDirty();
    }

    public void removeNodeLocation(UUID uuid){
        HashSet<BlockPos> oldBlocks = nodeLocations.get(uuid).getKnownBlocks();
        for(var block : oldBlocks){
            extensions.remove(block);
        }
        nodeLocations.remove(uuid);
        this.setDirty();
    }

    public void removeExtension(BlockPos pos){
        for (var hashSet : nodeLocations.entrySet()) {
            var storage = hashSet.getValue();
            if (storage.get(pos)) {
                storage.removeKnownBlock(pos);
                extensions.remove(pos);
                this.setDirty();
            }
        }
    }

    public HashSet<BlockPos> getExtensions() {
        return extensions;
    }
    public boolean contains(BlockPos pos){
        return extensions.contains(pos);
    }

    //returns the BlockPos that it connects too, else returns null
    public @Nullable BlockPos tryExtension(BlockPos pos){
        //TODO: Check if block is valid on multiple sides. If so, do something
        BlockPos[] adjacents = {pos.north(),pos.south(),pos.east(),pos.west(),pos.above(),pos.below()};
        for (var adjacent : adjacents) { //Massive thanks to Kyra for being better at Java than I am lmao
            if (extensions.contains(adjacent)) {
                return adjacent;
            }
        }
        return null;
    }

    public boolean tryAddExtension(BlockPos pos){
        //TODO: Check if block is valid on multiple sides. If so, do something
        BlockPos x = tryExtension(pos);
        if (x != null){
            for (var str : nodeLocations.entrySet()) {
                NodeStorage storage = str.getValue();
                if (storage.canAdd(pos)) {
                    storage.addKnownBlock(pos);
                    extensions.add(pos);
                    System.out.println("Added Block!");
                    this.setDirty();
                    return true;
                }
            }
        }
        return false;
    }


    public static NodeCollection load(CompoundTag tag, HolderLookup.Provider lookupProvider) { //TODO: Fix this!
        NodeCollection data = NodeCollection.create();
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) { //TODO: Fix this!
        ListTag tag = new ListTag();
        for(var storageTEMP : this.nodeLocations.entrySet()){
            CompoundTag storageTag = new CompoundTag();
            NodeStorage storage = storageTEMP.getValue();

            storageTag.putUUID("uuid",storage.getUuid());
            storageTag.put("storage",storage.save());
        }
        return compoundTag;
    }
}
