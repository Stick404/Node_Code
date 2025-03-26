package org.sophia.nodecode.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;

public class TestingItem extends Item {
    public TestingItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var eye = player.getEyePosition();
        var look = player.getLookAngle();
        var z = level.clip(new ClipContext(
                eye,eye.add(look.normalize().scale(5.0)),ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,player)
        );
        System.out.println(z);
        System.out.println(z.getBlockPos());
        System.out.println(z.getType());

        return super.use(level, player, hand);
    }
}
