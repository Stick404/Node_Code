package com.mindlesstoys.stick404.nodecode.blocks;

import com.mindlesstoys.stick404.nodecode.save.ServerNodeCollection;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

import static com.mindlesstoys.stick404.nodecode.save.ServerNodeCollection.factory;

public class NodeRootBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING;

    public NodeRootBlock(Properties p_49224_) {
        super(p_49224_);
        this.registerDefaultState((this.getStateDefinition().any())
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(NodeRootBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NodeRootEntity(blockPos, blockState);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level.getBlockEntity(pos) instanceof NodeRootEntity entity){
            if (level instanceof ServerLevel serverLevel) {
                var data = ServerNodeCollection.getInstance(serverLevel);
                data.createNodeLocation(entity.getUuid(), pos,state.getValue(FACING));
                data.setDirty();
            }
            entity.setxCount(1);
            entity.setyCount(1);
            entity.setzCount(1);
        }
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof NodeRootEntity entity && level instanceof ServerLevel serverLevel) {
            var data = serverLevel.getDataStorage().computeIfAbsent(factory,"ServerNodeCollection");
            data.removeNodeLocation(entity.getUuid());
        }
        super.destroy(level, pos, state);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return super.rotate(state, level, pos, direction);
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING,context.getHorizontalDirection());
    }

    static {
        FACING = BlockStateProperties.FACING;
    }
}
