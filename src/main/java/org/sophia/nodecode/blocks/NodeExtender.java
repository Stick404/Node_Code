package org.sophia.nodecode.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.sophia.nodecode.networking.NodeStorageS2C;

import java.util.Arrays;

import static org.sophia.nodecode.save.NodeCollection.factory;

public class NodeExtender extends Block {
    public NodeExtender(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if(level instanceof ServerLevel serverLevel){
            var nodeCollection = serverLevel.getDataStorage().computeIfAbsent(factory,"NodeCollection");
            nodeCollection.tryAddExtension(pos);
            PacketDistributor.sendToAllPlayers(new NodeStorageS2C(new BlockPos(0,0,0),new BlockPos(0,0,0)));
        }
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if (level instanceof ServerLevel serverLevel) {
            var nodeCollection = serverLevel.getDataStorage().computeIfAbsent(factory,"NodeCollection");
            nodeCollection.removeExtension(pos);
        }
        super.destroy(level, pos, state);
    }
}
