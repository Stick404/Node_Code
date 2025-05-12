package com.mindlesstoys.stick404.nodecode.registries;

import com.mindlesstoys.stick404.nodecode.networking.AddNodeC2S;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import com.mindlesstoys.stick404.nodecode.networking.NodeStorageS2C;

import static com.mindlesstoys.stick404.nodecode.Nodecode.MODID;

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
        registrar.playToServer(
                AddNodeC2S.TYPE,
                AddNodeC2S.STREAM_CODEC,
                AddNodeC2S::handleData
        );
    }
}
