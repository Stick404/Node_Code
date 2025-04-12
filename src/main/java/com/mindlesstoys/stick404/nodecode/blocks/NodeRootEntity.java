package com.mindlesstoys.stick404.nodecode.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import com.mindlesstoys.stick404.nodecode.registries.BlockEntityRegistry;

import java.util.UUID;

public class NodeRootEntity extends BlockEntity {
    public NodeRootEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.NODE_ROOT_ENTITY.get(), pos, blockState);
        this.uuid = UUID.randomUUID();
    }

    private UUID uuid;

    public UUID getUuid() {return uuid;}

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        this.uuid = tag.getUUID("uuid");
        super.loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putUUID("uuid",this.uuid);
        super.saveAdditional(tag, registries);
    }
}
