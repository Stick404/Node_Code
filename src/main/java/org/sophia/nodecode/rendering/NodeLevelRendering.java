package org.sophia.nodecode.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.CoreShaders;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.sophia.nodecode.networking.NodeStorageS2C;

import java.util.HashMap;
import java.util.UUID;

import static org.sophia.nodecode.Utils.ID;

public final class NodeLevelRendering {
    private static NodeLevelRendering INSTANCE;
    public HashMap<UUID,NodeStorageS2C> todos = new HashMap<>();
    private boolean shouldClear = true;

    private NodeLevelRendering(){
    }

    public void clearTodo(){
        if (!shouldClear) {
            shouldClear = true;
            return;
        }
        todos.clear();
    }

    public void setShouldClear(boolean shouldClear) {
        this.shouldClear = shouldClear;
    }

    public static NodeLevelRendering getInstance(){
        if(INSTANCE == null){
            INSTANCE = new NodeLevelRendering();
        }
        return INSTANCE;
    }

    public void render(RenderLevelStageEvent event){
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER && !todos.isEmpty()){
            Tesselator tess = Tesselator.getInstance();
            event.getStage();
            var color = 0x99_FFFFFF;
            var camPos = event.getCamera().getPosition();

            for (var nodeSystemTEMP : todos.entrySet()) {
                var nodeSystem = nodeSystemTEMP.getValue();
                var stack = event.getPoseStack();
                stack.pushPose();
                BufferBuilder buff = tess.begin(VertexFormat.Mode.QUADS,DefaultVertexFormat.BLOCK);
                //ForLoop
                stack.translate(nodeSystem.centerBlock().getCenter().subtract(camPos));
                var dirDist = nodeSystem.dirDistance();
                stack.scale(dirDist.getX()*-1,dirDist.getY(),dirDist.getZ()*-1);

                //Rotation shit here!

                stack.mulPose(Axis.YP.rotationDegrees(90));
                RenderingUtils.makeDoubleSideSquare(buff, color, stack);
                stack.mulPose(Axis.YP.rotationDegrees(90));
                RenderingUtils.makeDoubleSideSquare(buff, color, stack);

                RenderSystem.setShaderTexture(0, ID("textures/block/plane.png"));
                RenderSystem.setShader(CoreShaders.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE);
                RenderSystem.setShaderColor(1f, 1F, 1F, 1F);

                RenderSystem.enableBlend();
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableDepthTest();

                BufferUploader.drawWithShader(buff.build());

                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(true);
                RenderSystem.disableBlend();
                RenderSystem.enableDepthTest();
                stack.popPose();
            }//ForLoop

            RenderSystem.setShaderColor(1F, 1F, 1F, 1F); //KEEP THIS LINE OR ELSE BLUE
        }
    }
}