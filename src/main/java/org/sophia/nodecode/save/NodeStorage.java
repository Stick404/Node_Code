package org.sophia.nodecode.save;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;

import java.util.HashSet;
import java.util.UUID;

public class NodeStorage {
    private HashSet<BlockPos> knownBlocks;
    private Direction dir = Direction.NORTH; //rather than doing 2 directions, we just assume the 2nd direction is rotated by 90 ClockWise
    private final UUID uuid;
    private final BlockPos centerBlock;
    //Eval eval;
    //Add more vals if needed

    public NodeStorage(UUID uuid, BlockPos centerBlock){
        this.uuid = uuid;
        this.centerBlock = centerBlock;
        HashSet<BlockPos> tempSet = new HashSet<>();
        tempSet.add(centerBlock);
        this.knownBlocks = tempSet;
    }
    public NodeStorage(CompoundTag tag, UUID uuid){
        ListTag poses = tag.getList("hashBlocks", Tag.TAG_INT_ARRAY);
        for(var block : poses) {
            var blockPos = ((IntArrayTag) block).getAsIntArray();
            BlockPos tempBlock = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
            this.knownBlocks.add(tempBlock);
        }
        this.dir = Direction.values()[tag.getInt("dir")];
        this.uuid = uuid;
        var blockPos = ((IntArrayTag) tag.get("centerBlock")).getAsIntArray();
        this.centerBlock = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
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

    public Boolean canAdd(BlockPos pos){
        //check if "can add" is in the same direction of `dir` or rotated 90
        for(BlockPos maybe : knownBlocks){
            BlockPos offset = maybe.offset(dir.getUnitVec3i());
            BlockPos offsetTurn = maybe.offset(dir.getClockWise().getUnitVec3i());

            if ((offset.getX() == pos.getX() && offset.getY() == pos.getY() && offset.getZ() == pos.getZ())
                    || (offsetTurn.getX() == pos.getX() && offsetTurn.getY() == pos.getY() && offsetTurn.getZ() == pos.getZ())){
                return true;
            }
        }
        return false;
    }

    public HashSet<BlockPos> getKnownBlocks() {
        return knownBlocks;
    }

    public void addKnownBlock(BlockPos pos){
        if (canAdd(pos)){
            this.knownBlocks.add(pos);
        }
    }

    public void removeKnownBlock(BlockPos pos){
        if(this.knownBlocks.contains(pos) && pos.getY() == centerBlock.getY()){
            Direction localDir;
            if (pos.subtract(centerBlock).get(dir.getAxis()) != 0) {
                localDir = dir;
            } else if (pos.subtract(centerBlock).get(dir.getClockWise().getAxis()) != 0) {
                localDir = dir.getClockWise();
            } else {
                throw new RuntimeException("Illegal block position in Node Array");
            }
            knownBlocks.remove(pos);

            for(BlockPos remove : this.knownBlocks.stream().toList()){
                if (remove.get(localDir.getAxis())*localDir.getAxisDirection().getStep() >= pos.get(localDir.getAxis())*localDir.getAxisDirection().getStep()){
                    knownBlocks.remove(remove);
                    System.out.println("Broke Block!");
                }
            }
        }
    }
    public boolean get(BlockPos pos){
        return this.knownBlocks.contains(pos);
    }


    public Direction getDir() {
        return dir;
    }
    public Boolean trySetDir(Direction dir){
        if (this.dir != null){
            this.dir = dir;
        }
        return false;
    }

    public UUID getUuid() {
        return uuid;
    }
}
