package org.sophia.nodecode.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.sophia.nodecode.rendering.NodeLevelRendering;

import java.util.UUID;

import static org.sophia.nodecode.Utils.ID;

public record NodeStorageS2C(BlockPos centerBlock, BlockPos dirDistance, UUID uuid) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<NodeStorageS2C> TYPE = new CustomPacketPayload.Type<>(ID("node_storage_s2c"));
    public static final StreamCodec<ByteBuf, NodeStorageS2C> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            NodeStorageS2C::centerBlock,
            BlockPos.STREAM_CODEC,
            NodeStorageS2C::dirDistance,
            UUIDUtil.STREAM_CODEC,
            NodeStorageS2C::uuid,
            NodeStorageS2C::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handleData(final NodeStorageS2C data, final IPayloadContext context){
        var todos = NodeLevelRendering.getInstance().todos;
        if(todos.containsKey(data.uuid)){
            todos.replace(data.uuid,data);
        } else {
            todos.put(data.uuid,data);
        }
    }
}
