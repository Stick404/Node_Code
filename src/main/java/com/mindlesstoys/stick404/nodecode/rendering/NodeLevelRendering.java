package com.mindlesstoys.stick404.nodecode.rendering;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.NodeEnv;
import com.mindlesstoys.stick404.nodecode.save.ClientNodeCollection;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.Map;
import java.util.UUID;

import static com.mindlesstoys.stick404.nodecode.Utils.ID;
import static com.mindlesstoys.stick404.nodecode.registries.process.RegistryProcess.KNOWN_NODES;

public class NodeLevelRendering {
    public static void render(RenderLevelStageEvent event) {
        var collection = ClientNodeCollection.get();
        if (!collection.getNodeLocations().isEmpty() && event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            var camPos = event.getCamera().getPosition();
            Tesselator tess = Tesselator.getInstance();

             for (var nodeSystemTEMP : collection.getNodeLocations().entrySet()) {
                var nodeSystem = nodeSystemTEMP.getValue();
                if (!nodeSystem.isShouldRender()) {
                    continue;
                }

                var stack = event.getPoseStack();
                var nodes = nodeSystem.getNodes();
                stack.translate(-1,0,0);
                RenderSystem.disableDepthTest();

                for (Map.Entry<UUID, NodeEnv.Node> tempNode : nodes.entrySet()){
                    BufferBuilder buff = tess.begin(VertexFormat.Mode.QUADS,DefaultVertexFormat.POSITION_COLOR);

                    NodeEnv.Node node = tempNode.getValue();
                    stack.pushPose();
                    stack.translate((nodeSystem.centerBlock.getCenter().add(node.position)).subtract(camPos));

                    stack.scale(0.05f,-0.05f,0.05f);
                    var fun = node.function;
                    Component translated = Component.translatable("node.".concat(fun.getNamespace().concat(".")).concat(fun.getPath()));

                    MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
                    Font font = Minecraft.getInstance().font;
                    float width = -font.width(translated) / 2f;
                    float height = font.wordWrapHeight(translated,50);
                    // TODO: Rotate the Nodes
                    font.drawInBatch(
                            translated,
                            width,
                            0f,
                            0xffffffff,
                            false,
                            stack.last().pose(),
                            source,
                            Font.DisplayMode.POLYGON_OFFSET,
                            0,
                            0xffffff
                    );
                    // the squares for the nodes
                    stack.pushPose();
                    Vec3 vec3 = new Vec3(
                            Math.copySign(1d,nodeSystem.getDirDistance().getX()),
                            -1,
                            //Math.copySign(1d,nodeSystem.getDirDistance().getZ())
                            0
                    );
                    // Sophia here! I have no clue what any of this does, and I am writing this as I am coding it!
                    stack.translate(0,-1,0);
                    height = height+20;
                    stack.scale(width*2, height,width*2);

                    // oh god.
                    //System.out.println(height/2);
                    stack.translate(vec3.multiply(0.5,1/height,0.5));
                    //stack.scale(10f,10f,10f);
                    //var color = KNOWN_NODES.get(fun).getColor();
                    var color = 0xFF666666;
                    RenderingUtils.makeDoubleSideSquare(buff,color,stack);

                    stack.popPose();
                    stack.pushPose();
                    stack.scale(-1,1,-1);
                    font.drawInBatch(
                            translated,
                            -font.width(translated) / 2f,
                            0f,
                            0xffffffff,
                            false,
                            stack.last().pose(),
                            source,
                            Font.DisplayMode.POLYGON_OFFSET,
                            0,
                            0xffffff
                    );
                    stack.popPose();
                    source.endBatch();
                    stack.popPose();

                    RenderSystem.enableDepthTest();
                    RenderSystem.enableCull();
                    RenderSystem.setShaderTexture(0, ID("textures/block/white_texture.png"));
                    RenderSystem.setShader(CoreShaders.RENDERTYPE_GUI);
                    MeshData check = buff.build();
                    if (check != null) {
                        BufferUploader.drawWithShader(check);
                    }
                }
            }
        }
    }
}
