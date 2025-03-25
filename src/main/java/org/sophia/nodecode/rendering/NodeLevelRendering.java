package org.sophia.nodecode.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import static org.sophia.nodecode.Utils.ID;

public class NodeLevelRendering {
    public void render(RenderLevelStageEvent event){
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buff = tess.begin(VertexFormat.Mode.QUADS,DefaultVertexFormat.BLOCK);
        var stack = event.getPoseStack();
        var color = 0x66_FFFFFF;
        var pos = new BlockPos(-110,62,-44);
        var camPos = event.getCamera().getPosition();

        stack.pushPose();
        stack.translate(pos.getCenter().subtract(camPos));

        stack.scale(10,10,10);
        var pose = stack.last();
        var view = pose.pose();
        RenderingUtils.makeDoubleSideSquare(buff,view,color,stack);
        stack.popPose();

        RenderSystem.setShaderTexture(0, ID("textures/block/plane.png"));
        RenderSystem.setShader(CoreShaders.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE);
        RenderSystem.setShaderColor(1f, 1F, 1F, 0.3F);



        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableDepthTest();

        BufferUploader.drawWithShader(buff.build());

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F); //KEEP THIS LINE OR ELSE SHIT GETS FUCKED
    }
}


