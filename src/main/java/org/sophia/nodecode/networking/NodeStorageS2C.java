package org.sophia.nodecode.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static org.sophia.nodecode.Utils.ID;

public record NodeStorageS2C(BlockPos centerBlock, BlockPos dirDistance) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<NodeStorageS2C> TYPE = new CustomPacketPayload.Type<>(ID("node_storage_s2c"));
    public static final StreamCodec<ByteBuf, NodeStorageS2C> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            NodeStorageS2C::centerBlock,
            BlockPos.STREAM_CODEC,
            NodeStorageS2C::dirDistance,
            NodeStorageS2C::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handleData(final NodeStorageS2C data, final IPayloadContext context){
        System.out.println("This is being ran on the client!");
    }
}
