package com.mindlesstoys.stick404.nodecode.save;

import net.minecraft.core.BlockPos;

import java.util.UUID;

public class ClientNodeStorage {
    public final UUID uuid;
    public final BlockPos centerBlock;
    private BlockPos dirDistance;
    private boolean shouldRender = true;

    public ClientNodeStorage(UUID uuid, BlockPos centerBlock, BlockPos dirDistance) {
        this.uuid = uuid;
        this.centerBlock = centerBlock;
        this.dirDistance = dirDistance;
    }

    public void updateDirDistance(BlockPos dirDistance){
        this.dirDistance = dirDistance;
    }

    public void setShouldRender(boolean bool){
        this.shouldRender = bool;
    }

    public boolean isShouldRender() {
        return shouldRender;
    }

    public BlockPos getDirDistance() {
        return dirDistance;
    }
}
