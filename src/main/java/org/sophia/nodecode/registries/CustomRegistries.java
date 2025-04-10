package org.sophia.nodecode.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Func;

import static net.neoforged.fml.common.EventBusSubscriber.Bus.MOD;
import static org.sophia.nodecode.Nodecode.MODID;
import static org.sophia.nodecode.Utils.ID;

@EventBusSubscriber(modid = MODID, bus = MOD)
public class CustomRegistries {
    public static final ResourceKey<Registry<DataType<?>>> DATA_TYPE_KEY = ResourceKey.createRegistryKey(ID("data_type"));
    public static final Registry<DataType<?>> DATA_TYPE_REGISTRY = new RegistryBuilder<>(DATA_TYPE_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<Func>> NODE_KEY = ResourceKey.createRegistryKey(ID("nodes"));
    public static final Registry<Func> NODE_REGISTRY = new RegistryBuilder<>(NODE_KEY)
            .sync(true)
            .create();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void registerRegistries(NewRegistryEvent event) {
        event.register(DATA_TYPE_REGISTRY);
        event.register(NODE_REGISTRY);
    }
}
