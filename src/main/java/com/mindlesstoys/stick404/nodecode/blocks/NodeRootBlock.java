package com.mindlesstoys.stick404.nodecode.blocks;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.Request;
import com.mindlesstoys.stick404.nodecode.logicSystems.types.TypeDouble;
import com.mindlesstoys.stick404.nodecode.save.ServerNodeCollection;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static com.mindlesstoys.stick404.nodecode.Utils.ID;
import static com.mindlesstoys.stick404.nodecode.registries.ItemRegistry.NODE_CLICKER;

public class  NodeRootBlock extends BaseEntityBlock {
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof NodeRootEntity entity){
            var storage = ServerNodeCollection.getInstance(serverLevel);
            var z = storage.getNodeLocations().get(entity.getUuid());
            var env = z.getEnv();
            System.out.println("Running!");
            env.run();
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof NodeRootEntity entity && stack.is(NODE_CLICKER.asItem())){
            var storage = ServerNodeCollection.getInstance(serverLevel);
            var z = storage.getNodeLocations().get(entity.getUuid());
            var env = z.getEnv();

            if (!env.isRoot()) {
                System.out.println("Setting Nodes!");
                UUID input1 = env.createNode(ID("double_input"), new TypeDouble(5.0));
                UUID input2 = env.createNode(ID("double_input"), new TypeDouble(10.0));
                UUID add = env.createNode(ID("add"));
                UUID print = env.createNode(ID("print"));

                env.connect(new Request(input1, 0, add, 0));
                env.connect(new Request(input2, 0, add, 1));
                env.connect(new Request(add, 0, print, 0));

                env.setRoot(print);
                env.read();
                storage.setDirty();
            } else {
                System.out.println("Running Nodes!");
                env.run();
            }
        }
        return InteractionResult.SUCCESS;
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
        }
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof NodeRootEntity entity && level instanceof ServerLevel serverLevel) {
            var data = ServerNodeCollection.getInstance(serverLevel);
            data.removeNodeLocation(entity.getUuid());
            data.setDirty();
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
