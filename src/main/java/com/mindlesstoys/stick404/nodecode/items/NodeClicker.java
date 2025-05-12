package com.mindlesstoys.stick404.nodecode.items;

import com.mindlesstoys.stick404.nodecode.Utils;
import com.mindlesstoys.stick404.nodecode.networking.AddNodeC2S;
import com.mindlesstoys.stick404.nodecode.save.ClientNodeCollection;
import com.mindlesstoys.stick404.nodecode.save.ClientNodeStorage;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;

public class NodeClicker extends Item {
    public static int MAX_DIST = 5;
    public NodeClicker(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level lev, Player player, InteractionHand hand) {
        if (lev instanceof ClientLevel level) {

            ClientNodeCollection collection = ClientNodeCollection.get();

            var lookAngle = player.getLookAngle();
            var eyePosition = player.getEyePosition();

            for (Map.Entry<UUID, ClientNodeStorage> storageTEMP : collection.getNodeLocations().entrySet()) {
                ClientNodeStorage storage = storageTEMP.getValue();
                BlockPos dirDist = storage.getDirDistance();
                BlockPos corner = storage.centerBlock;
                Vec3 center = corner.getCenter();

                var outputX = Utils.raySlabIntersection(center,corner.offset(new BlockPos(dirDist.getX(),dirDist.getY(),0)).getCenter(),
                        lookAngle, eyePosition);

                var outputZ = Utils.raySlabIntersection(center,corner.offset(new BlockPos(0,dirDist.getY(),dirDist.getZ())).getCenter(),
                        lookAngle, eyePosition);

                Vec3 vec;
                //TODO: Check if this might be behind a block
                if ((outputX.y <= outputZ.y || outputX.x <= 0) && outputX.x <= 0 && outputX.y < MAX_DIST && outputX.y > 0) {
                        System.out.println("Doing X!");
                        vec = lookAngle.multiply(outputX.y, outputX.y, outputX.y).add(eyePosition);
                        return editNodes(player, vec, storage);
                } else if (outputZ.x <= 0 && outputZ.y < MAX_DIST && outputZ.y > 0){
                    System.out.println("Doing Z!");
                    vec = lookAngle.multiply(outputZ.y, outputZ.y, outputZ.y).add(eyePosition);
                    return editNodes(player, vec, storage);
                }
            }
            return InteractionResult.FAIL;
        }
        return InteractionResult.FAIL;
    }
    private static InteractionResult editNodes(Player player, Vec3 temp, ClientNodeStorage storage){
        player.playSound(SoundEvent.createFixedRangeEvent(
                ResourceLocation.fromNamespaceAndPath("minecraft","entity.experience_orb.pickup"),3f),0.2f,0.1f);
        var vec = temp.subtract(storage.centerBlock.getCenter());
        PacketDistributor.sendToServer(new AddNodeC2S("nodecode","add",storage.uuid,vec));
        System.out.println(vec);
        System.out.println("HIT!");


        player.displayClientMessage(Component.literal(String.valueOf(vec)),false);
        return InteractionResult.SUCCESS;
    }
}
