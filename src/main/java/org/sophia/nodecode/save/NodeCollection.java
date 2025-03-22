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
    HashMap<UUID,HashSet<BlockPos>> nodeLocations = new HashMap<>();
    //store all known "Node Sets"
    //this is not great, but it works for now. Later it might be better to replace `HashSet<BlockPos>` with its own Class.
    //So like, it stores the `HashSet` inside the class, and other bits of important data (such as: Fork Directions,
    //eval store, power, etc)


    public static NodeCollection create() {
        return new NodeCollection();
    }

    public void createNodeLocation(UUID uuid, BlockPos pos){
        extensions.add(pos);
        HashSet<BlockPos> setNew = new HashSet<>();
        setNew.add(pos);
        nodeLocations.put(uuid,setNew);
        LOGGER.info("making new Location!");
        this.setDirty();
    }

    public void removeNodeLocation(UUID uuid){
        HashSet<BlockPos> oldBlocks = nodeLocations.get(uuid);
        for(var block : oldBlocks){
            extensions.remove(block);
        }
        nodeLocations.remove(uuid);
        this.setDirty();
    }

    public void removeExtension(BlockPos pos){
        //TODO: make this remove all other extensions down the line

        for (var hashSet : nodeLocations.entrySet()) {
            if (hashSet.getValue().contains(pos)) {
                hashSet.getValue().remove(pos);
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
            for (var hashSet : nodeLocations.entrySet()) {
                if (hashSet.getValue().contains(x)) {
                    hashSet.getValue().add(pos);
                    extensions.add(pos);
                    System.out.println("Added Block!");
                    this.setDirty();
                    return true;
                }
            }
        }
        return false;
    }


    public static NodeCollection load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        NodeCollection data = NodeCollection.create();

        //loading Locations
        var locationsTag = (ListTag) tag.get("nodeLocations");
        assert locationsTag != null;
        for(var locationTEMP : locationsTag){
            CompoundTag locationTag = (CompoundTag) locationTEMP;
            UUID uuid = locationTag.getUUID("uuid");


            HashSet<BlockPos> blocks = new HashSet<>();
            ListTag poses = locationTag.getList("blocklist", Tag.TAG_INT_ARRAY);
            for(var block : poses){
                var blockPos = ((IntArrayTag) block).getAsIntArray();
                BlockPos tempBlock = new BlockPos(blockPos[0],blockPos[1],blockPos[2]);
                data.extensions.add(tempBlock); //so there is never random blocks in extensions
                blocks.add(tempBlock);
            }
            data.nodeLocations.put(uuid,blocks);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ListTag nodeLocationTag = new ListTag();
        for(var sets : nodeLocations.entrySet()){
            CompoundTag nodeLocTag = new CompoundTag();

            nodeLocTag.put("uuid",NbtUtils.createUUID(sets.getKey()));

            ListTag blockList = new ListTag();
            for(var bad : sets.getValue()){
                blockList.add(NbtUtils.writeBlockPos(bad)); //Add all the blocks in the sub-set into the `blockList`
            }
            nodeLocTag.put("blocklist",blockList); //add BlockPoss into the main tag
            nodeLocationTag.add(nodeLocTag);
        }

        compoundTag.put("nodeLocations",nodeLocationTag);
        return compoundTag;
    }
}
