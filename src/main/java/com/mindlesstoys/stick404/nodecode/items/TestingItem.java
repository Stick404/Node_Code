package com.mindlesstoys.stick404.nodecode.items;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import com.mindlesstoys.stick404.nodecode.logicSystems.Testing;

public class TestingItem extends Item {
    private static BlockPos pos = new BlockPos(0,0,0);
    public TestingItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel z){
            try{
                Testing.testing();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
