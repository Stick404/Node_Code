package org.sophia.nodecode.save;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class NodeStorage {
    private HashSet<BlockPos> knownBlocks;
    private Direction dir = Direction.EAST; //rather than doing 2 directions, we just assume the 2nd direction is rotated by 90 ClockWise
    private final UUID uuid;
    private final BlockPos centerBlock;
    //Eval eval;
    //Add more vals if needed

    public NodeStorage(UUID uuid, BlockPos centerBlock,Direction dir){
        this.uuid = uuid;
        this.centerBlock = centerBlock;
        HashSet<BlockPos> tempSet = new HashSet<>();
        tempSet.add(centerBlock);
        this.knownBlocks = tempSet;
        this.dir = dir;
    }

    //made for NodeStorage#fromTag
    //returns a fully made NodeStorage
    private NodeStorage(UUID uuid, BlockPos centerBlock, Direction dir, HashSet<BlockPos> knownBlocks){
        this.uuid = uuid;
        this.centerBlock = centerBlock;
        knownBlocks.add(centerBlock);
        this.knownBlocks = knownBlocks;
        this.dir = dir;
    }

    public static NodeStorage fromTag(CompoundTag tag, UUID uuid){
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

        return new NodeStorage(uuid,centerBlock,dir,knownBlocks);
    }

    public CompoundTag save(CompoundTag tag){
        ListTag blockList = new ListTag();
        for(var bp : knownBlocks){
            blockList.add(NbtUtils.writeBlockPos(bp)); //Add all the blocks in the sub-set into the `blockList`
        }
        tag.put("hashBlocks",blockList);
        tag.putInt("dir",dir.ordinal());
        tag.put("centerBlock",NbtUtils.writeBlockPos(centerBlock));
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
            this.knownBlocks.add(pos);
        }
        return canAdd;

    }

    public List<BlockPos> removeKnownBlock(BlockPos pos){ //TODO: Make this use NodeStorage#validBlock
        List<BlockPos> returnList = new ArrayList<>();

        if(this.knownBlocks.contains(pos)){

            Direction localDir;
            if (pos.subtract(centerBlock).get(dir.getAxis()) != 0 && pos.getY() == centerBlock.getY()) {
                localDir = dir;
            } else if (pos.subtract(centerBlock).get(dir.getClockWise().getAxis()) != 0 && pos.getY() == centerBlock.getY()) {
                localDir = dir.getClockWise();
            } else if (pos.getX() == centerBlock.getX() && pos.getZ() == centerBlock.getZ()){
                localDir = Direction.UP;
            } else {
                throw new RuntimeException("Illegal block position in Node Array");
            }
            knownBlocks.remove(pos);
            System.out.println("Broke Block!");
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
        if (pos.getX() == centerBlock.getX() || pos.getY() == centerBlock.getY() || pos.getZ() == centerBlock.getZ()){
            //Not great code, but:
            //this is how we set a dir


            Direction localDir;
            if (pos.subtract(centerBlock).get(dir.getAxis()) != 0 && pos.getY() == centerBlock.getY()) {
                localDir = dir;
            } else if (pos.subtract(centerBlock).get(dir.getClockWise().getAxis()) != 0 && pos.getY() == centerBlock.getY()) {
                localDir = dir.getClockWise();
            } else if (pos.getX() == centerBlock.getX() && pos.getZ() == centerBlock.getZ()){
                localDir = Direction.UP;
            } else {
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

    public Direction getDir() {
        return dir;
    }
    public Boolean trySetDir(Direction dir){
        //if the dir is Null,
        if (this.dir == null){
            this.dir = dir;
            return true;
        } else if (this.dir.getCounterClockWise() == dir) {
            this.dir = dir;
        }
        return false;
    }

    public UUID getUuid() {
        return uuid;
    }
}
