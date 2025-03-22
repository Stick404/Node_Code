package org.sophia.nodecode.registries;

import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.sophia.nodecode.Nodecode;
import org.sophia.nodecode.blocks.NodeExtender;
import org.sophia.nodecode.blocks.NodeRootBlock;

public class BlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.Blocks.createBlocks(Nodecode.MODID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.Items.createItems(Nodecode.MODID);

    public static final DeferredBlock<NodeRootBlock> NODE_ROOT_BLOCK = BLOCKS.registerBlock("node_root", NodeRootBlock::new);
    public static final DeferredItem<BlockItem> NODE_ROOT_BLOCK_ITEM = BLOCK_ITEMS.registerSimpleBlockItem("node_root", NODE_ROOT_BLOCK);

    public static final DeferredBlock<NodeExtender> NODE_EXTENDER = BLOCKS.registerBlock("node_extender", NodeExtender::new);
    public static final DeferredItem<BlockItem> NODE_EXTENDER_ITEM = BLOCK_ITEMS.registerSimpleBlockItem("node_extender",NODE_EXTENDER);

    public static void init(IEventBus bus){
        BLOCKS.register(bus);
        BLOCK_ITEMS.register(bus);
    }
}
