package org.sophia.nodecode.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import static org.sophia.nodecode.registries.CustomRegistries.DATA_TYPE_REGISTRY;

public class TestingItem extends Item {
    public static int MAX_DIST = 5;
    public TestingItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        try{
            System.out.println(DATA_TYPE_REGISTRY.getAny());
        } catch (Exception e) {
            System.out.println(e);
        }
        return InteractionResult.SUCCESS;
    }
}
