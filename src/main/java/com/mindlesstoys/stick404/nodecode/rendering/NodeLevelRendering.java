package com.mindlesstoys.stick404.nodecode.rendering;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.NodeEnv;
import com.mindlesstoys.stick404.nodecode.save.ClientNodeCollection;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.sun.jna.platform.win32.OpenGL32;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.Map;
import java.util.UUID;

public class NodeLevelRendering {
    public static void render(RenderLevelStageEvent event) {
        var collection = ClientNodeCollection.get();
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES && !collection.getNodeLocations().isEmpty()) {
            var camPos = event.getCamera().getPosition();

             for (var nodeSystemTEMP : collection.getNodeLocations().entrySet()) {
                var nodeSystem = nodeSystemTEMP.getValue();
                if (!nodeSystem.isShouldRender()) {
                    continue;
                }

                var stack = event.getPoseStack();
                var nodes = nodeSystem.getNodes();
                int x = 0;
                for (Map.Entry<UUID, NodeEnv.Node> tempNode : nodes.entrySet()){
                    NodeEnv.Node node = tempNode.getValue();
                    stack.pushPose();
                    stack.translate((nodeSystem.centerBlock.getCenter().add(node.position)).subtract(camPos));
                    stack.translate(4,4.5+x,0);

                    stack.scale(0.05f,-0.05f,0.05f);
                    var fun = node.function;
                    Component translated = Component.translatable("node.".concat(fun.getNamespace().concat(".")).concat(fun.getPath()));

                    MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
                    Font font = Minecraft.getInstance().font;
                    RenderSystem.disableDepthTest();

                    font.drawInBatch(
                            translated,
                            -font.width(translated) / 2f,
                            0f,
                            0xffffff,
                            false,
                            stack.last().pose(),
                            source,
                            Font.DisplayMode.POLYGON_OFFSET,
                            0,
                            0xffffff
                    );
                    stack.pushPose();
                    stack.scale(-1,1,-1);
                    font.drawInBatch(
                            translated,
                            -font.width(translated) / 2f,
                            0f,
                            0xffffff,
                            false,
                            stack.last().pose(),
                            source,
                            Font.DisplayMode.POLYGON_OFFSET,
                            0,
                            0xffffff
                    );
                    stack.popPose();
                    x = x-1;
                    source.endBatch();
                    stack.popPose();
                }
            }
        }
    }
}
