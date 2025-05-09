package com.mindlesstoys.stick404.nodecode.networking;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.NodeEnv;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.mindlesstoys.stick404.nodecode.save.ClientNodeCollection;
import com.mindlesstoys.stick404.nodecode.save.ClientNodeStorage;

import java.util.UUID;

import static com.mindlesstoys.stick404.nodecode.Utils.ID;

public record NodeStorageS2C(BlockPos centerBlock, BlockPos dirDistance, UUID uuid, boolean shouldClear, String nodes) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<NodeStorageS2C> TYPE = new CustomPacketPayload.Type<>(ID("node_storage_s2c"));
    public static final StreamCodec<ByteBuf, NodeStorageS2C> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            NodeStorageS2C::centerBlock,
            BlockPos.STREAM_CODEC,
            NodeStorageS2C::dirDistance,
            UUIDUtil.STREAM_CODEC,
            NodeStorageS2C::uuid,
            ByteBufCodecs.BOOL,
            NodeStorageS2C::shouldClear,
            ByteBufCodecs.STRING_UTF8,
            NodeStorageS2C::nodes,
            NodeStorageS2C::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handleData(final NodeStorageS2C data, final IPayloadContext context){
        var inst = ClientNodeCollection.get();
        var todos = inst.getNodeLocations();

        inst.setShouldClear(data.shouldClear);
        if(todos.containsKey(data.uuid)){
            todos.replace(data.uuid, new ClientNodeStorage(data.uuid,data.centerBlock,data.dirDistance,data.nodes));
        } else {
            todos.put(data.uuid, new ClientNodeStorage(data.uuid,data.centerBlock,data.dirDistance,data.nodes));
        }
    }
}
