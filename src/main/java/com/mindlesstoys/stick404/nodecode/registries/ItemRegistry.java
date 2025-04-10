package com.mindlesstoys.stick404.nodecode.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.mindlesstoys.stick404.nodecode.Nodecode;
import com.mindlesstoys.stick404.nodecode.items.NodeClicker;
import com.mindlesstoys.stick404.nodecode.items.TestingItem;

public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.Items.createItems(Nodecode.MODID);

    public static final DeferredItem<TestingItem> TESTING_ITEM = ITEMS.registerItem("testing_item",TestingItem::new);
    public static final DeferredItem<NodeClicker> NODE_CLICKER = ITEMS.registerItem("node_clicker",NodeClicker::new);

    public static void init(IEventBus bus){
        ITEMS.register(bus);
    }
}
