package org.sophia.nodecode.items;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.sophia.nodecode.logicSystems.Testing;

import static org.sophia.nodecode.Utils.ID;
import static org.sophia.nodecode.registries.process.NodeProcess.KNOWN_NODES;

public class TestingItem extends Item {
    private static BlockPos pos = new BlockPos(0,0,0);
    public TestingItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        try{
            Testing.testing();
        } catch (Exception e) {
            System.out.println(e);
        }

        if (level instanceof ServerLevel z){
            var e = ID("node_add");
            System.out.println("NAME!!");
            System.out.println(KNOWN_NODES.get(e).getClass().getName());
        }
        return InteractionResult.SUCCESS;
    }
}
