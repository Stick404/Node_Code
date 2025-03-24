package org.sophia.nodecode.registries;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.sophia.nodecode.networking.NodeStorageS2C;

import static org.sophia.nodecode.Nodecode.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkRegistry {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        PayloadRegistrar registrar = event.registrar("1");
        registrar = registrar.executesOn(HandlerThread.NETWORK);

        registrar.playToClient(
                NodeStorageS2C.TYPE,
                NodeStorageS2C.STREAM_CODEC,
                NodeStorageS2C::handleData
        );
    }
}
