package org.sophia.nodecode.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.sophia.nodecode.blocks.NodeRootEntity;

import java.util.function.Supplier;

import static org.sophia.nodecode.Nodecode.MODID;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE,MODID);

    public static final Supplier<BlockEntityType<NodeRootEntity>> NODE_ROOT_ENTITY = BLOCK_ENTITY.register("node_root_entity",
            ()-> new BlockEntityType<>(NodeRootEntity::new,
                    BlockRegistry.NODE_ROOT_BLOCK.get()));


    public static void init(IEventBus bus){
        BLOCK_ENTITY.register(bus);
    }
}
