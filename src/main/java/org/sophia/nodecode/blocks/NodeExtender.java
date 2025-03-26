package org.sophia.nodecode.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static org.sophia.nodecode.save.ServerNodeCollection.factory;

public class NodeExtender extends Block {
    public NodeExtender(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if(level instanceof ServerLevel serverLevel){
            var nodeCollection = serverLevel.getDataStorage().computeIfAbsent(factory,"ServerNodeCollection");
            nodeCollection.tryAddExtension(pos);
        }
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if (level instanceof ServerLevel serverLevel) {
            var nodeCollection = serverLevel.getDataStorage().computeIfAbsent(factory,"ServerNodeCollection");
            nodeCollection.removeExtension(pos);
        }
        super.destroy(level, pos, state);
    }
}
