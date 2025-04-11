package com.mindlesstoys.stick404.nodecode.save;

import net.minecraft.core.BlockPos;

import java.util.UUID;

public class ClientNodeStorage {
    public final UUID uuid;
    public final BlockPos centerBlock;
    private BlockPos dirDistance;
    private boolean shouldRender = true;

    /**
     * This handles all the client side Rendering/Data Tracking of {@link ServerNodeStorage}.
     * We keep a {@link ClientNodeStorage#dirDistance} (BlockPos) instead of a HashMap because it is light to send over the network
     * @param uuid The UUID of this storage
     * @param centerBlock The center block of this storage
     * @param dirDistance How far the NodeArray will extend
     */
    public ClientNodeStorage(UUID uuid, BlockPos centerBlock, BlockPos dirDistance) {
        this.uuid = uuid;
        this.centerBlock = centerBlock;
        this.dirDistance = dirDistance;
    }

    /** When this {@link ClientNodeStorage}
     * @param bool if this NodeStorage should be rendered when called
     */
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
