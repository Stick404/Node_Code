package com.mindlesstoys.stick404.nodecode.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.CoreShaders;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import com.mindlesstoys.stick404.nodecode.save.ClientNodeCollection;

import static com.mindlesstoys.stick404.nodecode.Utils.ID;

public final class PlaneLevelRendering {
    public static void render(RenderLevelStageEvent event){
        var collection = ClientNodeCollection.get();
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER && !collection.getNodeLocations().isEmpty()){
            Tesselator tess = Tesselator.getInstance();
            event.getStage();
            var color = 0x99_FFFFFF;
            var camPos = event.getCamera().getPosition();

            for (var nodeSystemTEMP : collection.getNodeLocations().entrySet()) {
                var nodeSystem = nodeSystemTEMP.getValue();
                if (!nodeSystem.isShouldRender()){
                    continue;
                }
                var stack = event.getPoseStack();
                stack.pushPose();
                BufferBuilder buff = tess.begin(VertexFormat.Mode.QUADS,DefaultVertexFormat.BLOCK);

                stack.translate(nodeSystem.centerBlock.getCenter().subtract(camPos));
                var dirDist = nodeSystem.getDirDistance();
                stack.scale(dirDist.getX()*-1,dirDist.getY(),dirDist.getZ()*-1);



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
            }

            RenderSystem.setShaderColor(1F, 1F, 1F, 1F); //KEEP THIS LINE OR ELSE BLUE
        }
    }
}