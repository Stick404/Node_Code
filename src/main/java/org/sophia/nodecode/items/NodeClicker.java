package org.sophia.nodecode.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.sophia.nodecode.save.ClientNodeCollection;
import org.sophia.nodecode.save.ClientNodeStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NodeClicker extends Item {
    public static int MAX_DIST = 5;
    public NodeClicker(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ClientNodeCollection collection = ClientNodeCollection.get();
        var lookDir = player.getLookAngle();
        var playerPos = player.getEyePosition();

        List<Vec3> raycast = new ArrayList<>();
        raycast.add(playerPos);
        for (int i = 0; i < MAX_DIST; i++) {
            //TODO: Check if add is > than clip
            raycast.add(raycast.getLast().add(lookDir));
        }
        var clip = level.clip(new ClipContext(raycast.getFirst(),raycast.getLast(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,player));

        for(Map.Entry<UUID, ClientNodeStorage> storageTEMP : collection.getNodeLocations().entrySet()){
            ClientNodeStorage storage = storageTEMP.getValue();
            BlockPos dirDist = storage.getDirDistance();
            BlockPos center = storage.centerBlock;
            AABB side1 = AABB.encapsulatingFullBlocks(center,
                    new BlockPos(center.offset(dirDist).getX(),center.offset(dirDist).getY(),center.getZ()));
            AABB side2 = AABB.encapsulatingFullBlocks(center,
                    new BlockPos(center.getX(),center.offset(dirDist).getY(),center.offset(dirDist).getZ()));

            for (Vec3 vec : raycast){
                if (clip.getType() != HitResult.Type.MISS
                        && vec.distanceTo(clip.getBlockPos().getCenter()) <= 0.7
                        && (side1.contains(vec) || side2.contains(vec))) {
                    System.out.println("FAIL");
                    return InteractionResult.FAIL;
                }
                if (side1.contains(vec)){
                    System.out.println("X SIDE!");
                    player.displayClientMessage(Component.literal("X SIDE!"),false);
                    //TODO: Do something with the nodes!
                    return editNodes(player,vec,storage);
                }
                if (side2.contains(vec)){
                    System.out.println("Z SIDE!");
                    player.displayClientMessage(Component.literal("Z SIDE!"),false);
                    //TODO: Do something with the nodes!
                    return editNodes(player,vec,storage);
                }
            }
        }
        return InteractionResult.FAIL;
    }
    private static InteractionResult editNodes(Player player, Vec3 vec, ClientNodeStorage storage){
        player.playSound(SoundEvent.createFixedRangeEvent(
                ResourceLocation.fromNamespaceAndPath("minecraft","entity.experience_orb.pickup"),3f),0.2f,0.1f);
        player.displayClientMessage(Component.literal(String.valueOf(storage.centerBlock.getCenter().subtract(vec))),false);
        return InteractionResult.SUCCESS;
    }
}
