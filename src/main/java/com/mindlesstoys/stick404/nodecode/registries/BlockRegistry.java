package com.mindlesstoys.stick404.nodecode.registries;

import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.mindlesstoys.stick404.nodecode.Nodecode;
import com.mindlesstoys.stick404.nodecode.blocks.NodeExtender;
import com.mindlesstoys.stick404.nodecode.blocks.NodeRootBlock;
import com.mindlesstoys.stick404.nodecode.items.NodeExtenderBlockItem;

public class BlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.Blocks.createBlocks(Nodecode.MODID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.Items.createItems(Nodecode.MODID);

    public static final DeferredBlock<NodeRootBlock> NODE_ROOT_BLOCK = BLOCKS.registerBlock("node_root", NodeRootBlock::new);
    public static final DeferredItem<BlockItem> NODE_ROOT_BLOCK_ITEM = BLOCK_ITEMS.registerSimpleBlockItem("node_root", NODE_ROOT_BLOCK);

    public static final DeferredBlock<NodeExtender> NODE_EXTENDER = BLOCKS.registerBlock("node_extender", NodeExtender::new);
    public static final DeferredItem<NodeExtenderBlockItem> NODE_EXTENDER_ITEM = BLOCK_ITEMS.registerItem("node_extender", NodeExtenderBlockItem::new);

    public static void init(IEventBus bus){
        BLOCKS.register(bus);
        BLOCK_ITEMS.register(bus);
    }
}
