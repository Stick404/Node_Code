package com.mindlesstoys.stick404.nodecode.items;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.mindlesstoys.stick404.nodecode.registries.BlockRegistry;
import com.mindlesstoys.stick404.nodecode.save.ServerNodeCollection;

import static com.mindlesstoys.stick404.nodecode.save.ServerNodeCollection.factory;

public class NodeExtenderBlockItem extends BlockItem {
    public NodeExtenderBlockItem(Properties properties) {
        super(BlockRegistry.NODE_EXTENDER.get(), properties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        if (context.getLevel() instanceof ServerLevel level){
            ServerNodeCollection collection = level.getDataStorage().computeIfAbsent(factory,"ServerNodeCollection");
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
