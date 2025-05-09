package com.mindlesstoys.stick404.nodecode.save;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.NodeEnv;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;

import java.util.HashMap;
import java.util.UUID;

public class ClientNodeStorage {
    public final UUID uuid;
    public final BlockPos centerBlock;
    private BlockPos dirDistance;
    private boolean shouldRender = true;
    private HashMap<UUID, NodeEnv.Node> nodes;

    /**
     * This handles all the client side Rendering/Data Tracking of {@link ServerNodeStorage}.
     * We keep a {@link ClientNodeStorage#dirDistance} (BlockPos) instead of a HashMap because it is light to send over the network
     * @param uuid The UUID of this storage
     * @param centerBlock The center block of this storage
     * @param dirDistance How far the NodeArray will extend
     */
    public ClientNodeStorage(UUID uuid, BlockPos centerBlock, BlockPos dirDistance, String nodes) {
        this.uuid = uuid;
        this.centerBlock = centerBlock;
        this.dirDistance = dirDistance;
        CompoundTag tag = null;
        try {
            tag = TagParser.parseTag(nodes);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        this.nodes = new HashMap<>();
        for (var tempNode : tag.getList("nodes",CompoundTag.TAG_COMPOUND)){
            CompoundTag nodeTag = (CompoundTag) tempNode;
            NodeEnv.Node node = new NodeEnv.Node(nodeTag);

            this.nodes.put(node.uuid,node);
        }
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

    public void setNodes(HashMap<UUID, NodeEnv.Node> nodes) {
        this.nodes = nodes;
    }
    public NodeEnv.Node getNode(UUID uuid) {
        return this.nodes.get(uuid);
    }

    public HashMap<UUID, NodeEnv.Node> getNodes() {
        return nodes;
    }
}
