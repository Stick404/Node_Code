package com.mindlesstoys.stick404.nodecode.save;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.NodeEnv;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import com.mindlesstoys.stick404.nodecode.Utils;
import com.mindlesstoys.stick404.nodecode.networking.NodeStorageS2C;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class ServerNodeStorage {
    private final HashSet<BlockPos> knownBlocks;
    private final Direction dir; //rather than doing 2 directions, we just assume the 2nd direction is rotated by 90 ClockWise
    private final UUID uuid;
    private final BlockPos centerBlock;
    private BlockPos dirDistance; //these 3 are used for rendering and quickly finding the bounding box. Is in World Space
    private final NodeEnv env;
    //Add more vals if needed

    public ServerNodeStorage(UUID uuid, BlockPos centerBlock, Direction dir){
        this.uuid = uuid;
        this.centerBlock = centerBlock;
        HashSet<BlockPos> tempSet = new HashSet<>();
        tempSet.add(centerBlock);
        this.knownBlocks = tempSet;
        this.dir = dir;
        this.dirDistance = new BlockPos(0,0,0);
        this.env = new NodeEnv();
    }

    //made for ServerNodeStorage#fromTag
    //returns a fully made ServerNodeStorage
    private ServerNodeStorage(UUID uuid, BlockPos centerBlock, HashSet<BlockPos> knownBlocks, Direction dir, BlockPos dirDistance, NodeEnv env) {
        this.uuid = uuid;
        this.centerBlock = centerBlock;
        knownBlocks.add(centerBlock);
        this.knownBlocks = knownBlocks;
        this.dir = dir;
        this.dirDistance = dirDistance;
        this.env = env;
    }

    public NodeEnv getEnv() {
        return env;
    }

    public static ServerNodeStorage fromTag(CompoundTag tag, UUID uuid){
        HashSet<BlockPos> knownBlocks = new HashSet<>();

        ListTag poses = tag.getList("hashBlocks", Tag.TAG_INT_ARRAY);
        for(var block : poses) {
            var blockPos = ((IntArrayTag) block).getAsIntArray();
            BlockPos tempBlock = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
            knownBlocks.add(tempBlock);
        }
        var dir = Direction.values()[tag.getInt("dir")];
        var blockPos = ((IntArrayTag) tag.get("centerBlock")).getAsIntArray();
        var centerBlock = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);

        var blockPosDir = ((IntArrayTag) tag.get("dirDistance")).getAsIntArray();
        var dirDistance = new BlockPos(blockPosDir[0], blockPosDir[1], blockPosDir[2]);

        var env = new NodeEnv(tag.getCompound("env"));

        return new ServerNodeStorage(uuid, centerBlock, knownBlocks, dir, dirDistance, env);
    }

    public CompoundTag save(CompoundTag tag){
        ListTag blockList = new ListTag();
        for(var bp : knownBlocks){
            blockList.add(NbtUtils.writeBlockPos(bp)); //Add all the blocks in the sub-set into the `blockList`
        }
        tag.put("hashBlocks",blockList);
        tag.putInt("dir",dir.ordinal());
        tag.put("centerBlock",NbtUtils.writeBlockPos(this.centerBlock));
        tag.put("dirDistance", NbtUtils.writeBlockPos(this.dirDistance));
        tag.put("env",env.save());

        return tag;
    }

    public CompoundTag save(){
        return save(new CompoundTag());
    }

    public HashSet<BlockPos> getKnownBlocks() {
        return knownBlocks;
    }

    public boolean addKnownBlock(BlockPos pos){
        boolean canAdd = validBlock(pos) != null;
        if (canAdd){
            setDirDistance(pos);
            this.knownBlocks.add(pos);
            updateClients();
        }
        return canAdd;

    }

    public List<BlockPos> removeKnownBlock(BlockPos pos){
        List<BlockPos> returnList = new ArrayList<>();
        if(this.knownBlocks.contains(pos)){
            Direction localDir = dirOrClockwise(pos);
            if (localDir == null){
                return returnList;
            }

            //cant use the function because it has to be moved 1 over
            this.dirDistance = Utils.setAxisVal(this.dirDistance,localDir.getAxis(),pos.subtract(centerBlock).get(localDir.getAxis())+localDir.getAxisDirection().getStep()*-1);
            System.out.println(dirDistance);
            System.out.println("Broke Block!");
            updateClients();
            for(BlockPos remove : this.knownBlocks.stream().toList()){
                if (remove.get(localDir.getAxis())*localDir.getAxisDirection().getStep() >= pos.get(localDir.getAxis())*localDir.getAxisDirection().getStep()){
                    knownBlocks.remove(remove);
                    System.out.println("Broke Block!");
                    returnList.add(remove);
                }
            }
        }
        return returnList;
    }

    public @Nullable BlockPos validBlock(BlockPos pos){
        //checks if the given block can be added
        if ((pos.getX() == centerBlock.getX() && pos.getZ() != centerBlock.getZ()) || (pos.getX() != centerBlock.getX() && pos.getZ() == centerBlock.getZ())
        || pos.getY() != centerBlock.getY()){
            Direction localDir = dirOrClockwise(pos);
            if (localDir == null){
                return null;
            }
            BlockPos maybeBlock = pos.offset(localDir.getUnitVec3i().multiply(-1));
            if (this.knownBlocks.contains(maybeBlock)){
                return maybeBlock;
            }
        }
        return null;
    }

    public boolean get(BlockPos pos){
        return this.knownBlocks.contains(pos);
    }

    public BlockPos getCenterBlock() {
        return centerBlock;
    }

    public BlockPos getDirDistance() {
        return dirDistance;
    }

    public Direction getDir() {
        return dir;
    }

    public UUID getUuid() {
        return uuid;
    }

    private @Nullable Direction dirOrClockwise(BlockPos pos){
        //Not great code, but:
        //this is how we set a dir
        if (pos.subtract(centerBlock).get(dir.getAxis()) != 0 && pos.getY() == centerBlock.getY()) {
            return dir;
        } else if (pos.subtract(centerBlock).get(dir.getClockWise().getAxis()) != 0 && pos.getY() == centerBlock.getY()) {
            return dir.getClockWise();
        } else if (pos.getX() == centerBlock.getX() && pos.getZ() == centerBlock.getZ()){
           return Direction.UP;
        } else {
            return null;
            //throw new RuntimeException("Illegal block position in Node Array");
        }
    }
    private void setDirDistance(BlockPos pos){
        Direction localDir = dirOrClockwise(pos);
        if (localDir == null){
            return;
        }
        this.dirDistance = Utils.setAxisVal(this.dirDistance,localDir.getAxis(),pos.subtract(centerBlock).get(localDir.getAxis()));
        System.out.println(dirDistance);
    }
    public void updateClients(boolean shouldClear){
        PacketDistributor.sendToAllPlayers(new NodeStorageS2C(this.centerBlock,this.dirDistance,this.uuid,shouldClear));
    }
    public void updateClients(){
        updateClients(true);
    }
}
