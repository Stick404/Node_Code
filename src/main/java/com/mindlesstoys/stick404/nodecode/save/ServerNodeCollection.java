package com.mindlesstoys.stick404.nodecode.save;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import static com.mindlesstoys.stick404.nodecode.Nodecode.LOGGER;

/**
 * This class handles *all* if the Node Arrays/{@link ServerNodeStorage},and is the server counterpart to {@link ClientNodeCollection}.
 */
//TODO: Make a NodeCollection interface
//TODO: Make a NodeStorage interface
public class ServerNodeCollection extends SavedData {
    public static Factory<ServerNodeCollection> factory = new Factory<>(ServerNodeCollection::create, ServerNodeCollection::load);
    HashSet<BlockPos> extensions = new HashSet<>();
    //Store all known blocks
    HashMap<UUID, ServerNodeStorage> nodeLocations = new HashMap<>();
    //store all known "Node Sets"
    //TODO: Make the blocks look different if they are in the global extensions

    public static ServerNodeCollection getInstance(ServerLevel level){
        return level.getDataStorage().computeIfAbsent(factory,"server_node_collection");
    }

    public static ServerNodeCollection create() {
        return new ServerNodeCollection();
    }

    public void createNodeLocation(UUID uuid, BlockPos pos, Direction dir){
        ServerNodeStorage storage = new ServerNodeStorage(uuid,pos,dir);
        nodeLocations.put(uuid,storage);
        extensions.add(pos);
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
                List<BlockPos> x = storage.removeKnownBlock(pos);
                extensions.remove(pos);
                for (BlockPos block : x){
                    extensions.remove(block);
                }
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

    /**
     * @param pos A BlockPos in the world
     * @return A BlockPos that is connected to the global HashSet of pos ({@link ServerNodeCollection#extensions})
     */
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

    /** Attempts to add a pos to a {@link ServerNodeStorage}, calls {@link ServerNodeCollection}
     *
     * @param pos The block to try to add
     * @return A boolean that states if it could add the block
     */
    public boolean tryAddExtension(BlockPos pos){
        //TODO: Check if block is valid on multiple sides. If so, do something
        BlockPos x = tryExtension(pos);
        if (x != null){
            for (var str : nodeLocations.entrySet()) {
                ServerNodeStorage storage = str.getValue();
                if (storage.addKnownBlock(pos)) {
                    extensions.add(pos);
                    System.out.println("Added Block!");
                    this.setDirty();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *  The exact same as {@link ServerNodeCollection#tryAddExtension(BlockPos)} except it just returns if it can, rather than adding
     * @param pos The block to try to add
     * @return A boolean that states if it could add the block
     */
    public boolean testAddExtension(BlockPos pos){
        //TODO: Check if block is valid on multiple sides. If so, do something
        BlockPos x = tryExtension(pos);
        if (x != null){
            for (var str : nodeLocations.entrySet()) {
                ServerNodeStorage storage = str.getValue();
                if (storage.validBlock(pos) != null) {
                    System.out.println("TRUEEEE!");
                    return true;
                }
            }
        }
        return false;
    }


    public static ServerNodeCollection load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        ServerNodeCollection data = ServerNodeCollection.create();

        ListTag storageList = tag.getList("storages", Tag.TAG_COMPOUND);
        HashSet<BlockPos> extensions = new HashSet<>();

        for(var tagDataTEMP : storageList){
            CompoundTag tagData = (CompoundTag) tagDataTEMP;
            CompoundTag storageVal = tagData.getCompound("storage");
            UUID uuid = tagData.getUUID("uuid");

            var nodeStorage = ServerNodeStorage.fromTag(storageVal,uuid);
            data.nodeLocations.put(uuid,nodeStorage);
            extensions.addAll(nodeStorage.getKnownBlocks());
        }

        data.extensions = extensions;
        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        ListTag tag = new ListTag();
        for(var storageTEMP : this.nodeLocations.entrySet()){
            CompoundTag storageTag = new CompoundTag();
            ServerNodeStorage storage = storageTEMP.getValue();
            storageTag.putUUID("uuid",storage.getUuid());
            storageTag.put("storage",storage.save());
            tag.add(storageTag);
        }
        compoundTag.put("storages",tag);
        return compoundTag;
    }

    public HashMap<UUID, ServerNodeStorage> getNodeLocations() {
        return nodeLocations;
    }

    /** Updates the place's local list of NodeCollections ({@link ClientNodeCollection}), boolean states if this update should clear the old list of known Collections
     * @param shouldClear
     */
    public void updatePlayers(boolean shouldClear){
        this.nodeLocations.forEach((z,x) -> x.updateClients(shouldClear));
    }
    public void updatePlayers(){
        this.nodeLocations.forEach((z,x) -> x.updateClients());
    }
}
