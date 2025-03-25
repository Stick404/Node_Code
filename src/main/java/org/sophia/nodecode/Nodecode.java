package org.sophia.nodecode;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import org.sophia.nodecode.registries.BlockEntityRegistry;
import org.sophia.nodecode.registries.BlockRegistry;
import org.sophia.nodecode.rendering.NodeLevelRendering;

import static org.sophia.nodecode.registries.BlockRegistry.NODE_ROOT_BLOCK_ITEM;
import static org.sophia.nodecode.save.NodeCollection.factory;

@Mod(Nodecode.MODID)
public class Nodecode {
    public static final String MODID = "nodecode";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Nodecode(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        BlockEntityRegistry.init(modEventBus);
        BlockRegistry.init(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Nodecode) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(NODE_ROOT_BLOCK_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        event.getServer().getAllLevels().forEach((z) -> z.getDataStorage().computeIfAbsent(factory,"NodeCollection"));

        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,EntityJoinLevelEvent.class,
                (entityJoinEvent)-> {
                    if (entityJoinEvent.getEntity().getType() == EntityType.PLAYER) {
                        if (entityJoinEvent.getLevel() instanceof ServerLevel level) {
                            System.out.println("UPDATEING PLAYERS");
                            level.getDataStorage().get(factory, "NodeCollection").updatePlayers(false);
                        }
                    }
                });
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            NeoForge.EVENT_BUS.addListener(RenderLevelStageEvent.class, (x) -> NodeLevelRendering.getInstance().render(x));

            NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST,LevelEvent.Load.class, (x) ->{
                NodeLevelRendering.getInstance().clearTodo();
                System.out.println("CLEARING!!!");
            });

            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
