package org.sophia.nodecode.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.sophia.nodecode.Nodecode;
import org.sophia.nodecode.items.TestingItem;

public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.Items.createItems(Nodecode.MODID);

    public static final DeferredItem<TestingItem> TESTING_ITEM = ITEMS.registerItem("testing_item",TestingItem::new);

    public static void init(IEventBus bus){
        ITEMS.register(bus);
    }
}
