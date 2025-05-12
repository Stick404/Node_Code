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
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Quaternionf;

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
                stack.pushPose();
                RenderSystem.disableDepthTest();

                for (Map.Entry<UUID, NodeEnv.Node> tempNode : nodes.entrySet()){
                    NodeEnv.Node node = tempNode.getValue();
                    BufferBuilder buff = tess.begin(VertexFormat.Mode.QUADS,DefaultVertexFormat.POSITION_COLOR);
                    stack.pushPose();
                    stack.translate((nodeSystem.centerBlock.getCenter().add(node.position)).subtract(camPos));

                    double xDir = node.position.x;
                    double zDir = node.position.z;
                    //welcome to the 'if' chain!
                    //this feels like a sin
                    int rotation = 0;
                    if (xDir < 0 && zDir <= 0) rotation = 0;
                    else if (xDir <= 0 && zDir < 0) rotation = 90;
                    else if (xDir >= 0 && zDir <= 0) rotation = 180;
                    else if (xDir <= 0 && zDir >= 0) rotation = 270;

                    float rad = (float) (Math.PI/180)*rotation;
                    stack.rotateAround(new Quaternionf(0,Math.sin(rad/2),0,Math.cos(rad/2)),0,0,0);


                    stack.scale(0.02f,-0.02f,-0.02f);
                    stack.translate(0,0,-2);

                    var fun = node.function;
                    Component translated = Component.translatable("node." + fun.getNamespace() + "." + fun.getPath());

                    MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
                    Font font = Minecraft.getInstance().font;
                    float width = Math.max(font.width(translated) / 2f, 30f);
                    stack.pushPose();
                    stack.scale(1f,1f,-1f);
                    drawText(translated, stack, source);
                    stack.popPose();
                    stack.pushPose();

                    //Types nodes
                    //stack.translate(0,0,0.01);
                    int outputs = 0;
                    for (var type : node.outputTypes){
                        stack.pushPose();
                        Component text = Component.translatable("node." + fun.getNamespace() + "." + fun.getPath() + ".output." + outputs);
                        stack.translate(width/1.5,(outputs+1)*15,0);
                        stack.scale(0.75f,0.75f,-0.01f);
                        drawText(text, stack, source);
                        stack.popPose();
                        ++outputs;
                    }
                    int inputs = 0;
                    for (var type : node.inputTypes){
                        stack.pushPose();
                        Component text = Component.translatable("node." + fun.getNamespace() + "." + fun.getPath() + ".input." + inputs);
                        stack.translate(-width/1.5,(inputs+1)*15,0);
                        stack.scale(0.75f,0.75f,-0.01f);
                        drawText(text, stack, source);
                        stack.popPose();
                        ++inputs;
                    }
                    source.endBatch();
                    stack.popPose();
                    stack.pushPose();
                    float sqHeight = Math.max(inputs,outputs)*15+15; //how "tall" the node should be
                    float sqWidth = Math.max(width,10)*3;
                    stack.translate(-sqWidth/2,-0.5,-0.01f);
                    stack.scale(sqWidth,sqHeight,1);
                    var color = KNOWN_NODES.get(fun).getColor();

                    RenderingUtils.makeDoubleSideSquare(buff,color,stack);
                    stack.popPose();
                    RenderSystem.enableDepthTest();
                    RenderSystem.enableCull();
                    RenderSystem.setShaderTexture(0, ID("textures/block/white_texture.png"));
                    RenderSystem.setShader(CoreShaders.RENDERTYPE_GUI);
                    MeshData check = buff.build();

                    if (check != null) {
                        BufferUploader.drawWithShader(check);
                    }
                    stack.popPose();
                }
                stack.popPose();
            }
        }
    }

    private static void drawText(Component text,PoseStack stack,MultiBufferSource.BufferSource source){
        Font font = Minecraft.getInstance().font;
        font.drawInBatch(
                text,
                -font.width(text) / 2f,
                0f,
                0xffffffff,
                true,
                stack.last().pose(),
                source,
                Font.DisplayMode.POLYGON_OFFSET,
                0,
                255
        );
    }
}
