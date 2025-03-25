package org.sophia.nodecode.items;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.sophia.nodecode.registries.BlockRegistry;
import org.sophia.nodecode.save.NodeCollection;

import static org.sophia.nodecode.save.NodeCollection.factory;

public class NodeExtenderBlockItem extends BlockItem {
    public NodeExtenderBlockItem(Properties properties) {
        super(BlockRegistry.NODE_EXTENDER.get(), properties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        if (context.getLevel() instanceof ServerLevel level){
            NodeCollection collection = level.getDataStorage().computeIfAbsent(factory,"NodeCollection");
            BlockPos pos = context.getClickedPos();
            BlockPos target = pos.subtract(context.getClickedFace().getUnitVec3i());

            if(collection.contains(target)){
                return collection.testAddExtension(pos);
            }
        }
        super.canPlace(context,state);
        return false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return super.use(level, player, hand);
    }
}
