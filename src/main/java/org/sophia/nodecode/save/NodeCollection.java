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
    //this is not great, but it works for now. Later it might be better to replace `HashSet<BlockPos>` with its own Class.
    //So like, it stores the `HashSet` inside the class, and other bits of important data (such as: Fork Directions,
    //eval store, power, etc)


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
                System.out.println("Remove Block");
                this.setDirty();
            }
        }
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
        return compoundTag;
    }
}
