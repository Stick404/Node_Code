package com.mindlesstoys.stick404.nodecode.items;

import com.mindlesstoys.stick404.nodecode.Utils;
import com.mindlesstoys.stick404.nodecode.save.ServerNodeCollection;
import com.mindlesstoys.stick404.nodecode.save.ServerNodeStorage;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import com.mindlesstoys.stick404.nodecode.save.ClientNodeCollection;
import com.mindlesstoys.stick404.nodecode.save.ClientNodeStorage;
import java.util.Map;
import java.util.UUID;

import static com.mindlesstoys.stick404.nodecode.save.ServerNodeCollection.factory;

public class NodeClicker extends Item {
    public static int MAX_DIST = 5;
    public NodeClicker(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level lev, Player player, InteractionHand hand) {
        if (lev instanceof ServerLevel level) {

            ServerNodeCollection collection = ServerNodeCollection.getInstance(level);

            var lookAngle = player.getLookAngle();
            var eyePosition = player.getEyePosition();

            for (Map.Entry<UUID, ServerNodeStorage> storageTEMP : collection.getNodeLocations().entrySet()) {
                ServerNodeStorage storage = storageTEMP.getValue();
                BlockPos dirDist = storage.getDirDistance();
                BlockPos corner = storage.getCenterBlock();
                Vec3 center = corner.getCenter();

                var outputX = Utils.raySlabIntersection(center,corner.offset(new BlockPos(dirDist.getX(),dirDist.getY(),0)).getCenter(),
                        lookAngle, eyePosition);

                var outputZ = Utils.raySlabIntersection(center,corner.offset(new BlockPos(0,dirDist.getY(),dirDist.getZ())).getCenter(),
                        lookAngle, eyePosition);

                Vec3 vec;
                //TODO: Check if this might be behind a block
                if ((outputX.y <= outputZ.y || outputX.x <= 0) && outputX.x <= 0 && outputX.y < MAX_DIST) {
                        System.out.println("Doing X!");
                        vec = lookAngle.multiply(outputX.y, outputX.y, outputX.y).add(eyePosition);
                        return editNodes(player, vec, storage, level);
                } else if (outputZ.x <= 0 && outputZ.y < MAX_DIST){
                    System.out.println("Doing Z!");
                    vec = lookAngle.multiply(outputZ.y, outputZ.y, outputZ.y).add(eyePosition);
                    return editNodes(player, vec, storage, level);
                }
            }
            return InteractionResult.FAIL;
        }
        return InteractionResult.FAIL;
    }
    private static InteractionResult editNodes(Player player, Vec3 vec, ServerNodeStorage storage, Level level){
        player.playSound(SoundEvent.createFixedRangeEvent(
                ResourceLocation.fromNamespaceAndPath("minecraft","entity.experience_orb.pickup"),3f),0.2f,0.1f);
        System.out.println(vec);
        System.out.println("HIT!");
        //player.displayClientMessage(Component.literal(String.valueOf(storage.centerBlock.getCenter().subtract(vec))),false);
        return InteractionResult.SUCCESS;
    }
}
