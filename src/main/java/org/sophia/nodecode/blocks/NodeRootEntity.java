package org.sophia.nodecode.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.sophia.nodecode.registries.BlockEntityRegistry;

import java.util.UUID;

public class NodeRootEntity extends BlockEntity {
    public NodeRootEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.NODE_ROOT_ENTITY.get(), pos, blockState);
        this.uuid = UUID.randomUUID();
    }

    private final UUID uuid;

    private Integer xCount = 0;
    private Integer yCount = 0;
    private Integer zCount = 0;

    public Integer getxCount() {return xCount;}
    public Integer getyCount() {return yCount;}
    public Integer getzCount() {return zCount;}
    public UUID getUuid() {return uuid;}

    public void setxCount(Integer count) {this.xCount = count;}
    public void setyCount(Integer count) {this.yCount = count;}
    public void setzCount(Integer count) {this.zCount = count;}

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        this.xCount = tag.getInt("xcount");
        this.yCount = tag.getInt("ycount");
        this.zCount = tag.getInt("zcount");
        super.loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("xcount",this.xCount);
        tag.putInt("ycount",this.yCount);
        tag.putInt("zcount",this.zCount);
        super.saveAdditional(tag, registries);
    }
}
