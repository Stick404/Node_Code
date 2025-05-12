package com.mindlesstoys.stick404.nodecode.networking;

import com.mindlesstoys.stick404.nodecode.Nodecode;
import com.mindlesstoys.stick404.nodecode.save.ServerNodeCollection;
import com.mindlesstoys.stick404.nodecode.save.ServerNodeStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

import static com.mindlesstoys.stick404.nodecode.Utils.ID;

public record AddNodeC2S(String namespace, String path, UUID system, Vec3 position) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AddNodeC2S> TYPE = new CustomPacketPayload.Type<>(ID("add_node_c2s"));
    public static final StreamCodec<ByteBuf, AddNodeC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            AddNodeC2S::namespace,
            ByteBufCodecs.STRING_UTF8,
            AddNodeC2S::path,
            UUIDUtil.STREAM_CODEC,
            AddNodeC2S::system,
            Vec3.STREAM_CODEC,
            AddNodeC2S::position,
            AddNodeC2S::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(final AddNodeC2S data, final IPayloadContext context){
        ServerNodeCollection collection = ServerNodeCollection.getInstance((ServerLevel) context.player().level());
        ServerNodeStorage storage = collection.getNodeLocations().get(data.system);
        if (storage == null) {
            Nodecode.LOGGER.warn("Player: {} tried to get an invalid NodeStorage!", context.player());
            return;
        }
        storage.getEnv().createNode(ResourceLocation.parse(data.namespace + ":" + data.path),data.position);
        collection.setDirty();
        collection.updatePlayers();
    }
}
