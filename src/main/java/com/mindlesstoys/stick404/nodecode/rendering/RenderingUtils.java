package com.mindlesstoys.stick404.nodecode.rendering;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;

public class RenderingUtils {
    public static void makeSquare(BufferBuilder buff, Matrix4f view, int color){
        buff.addVertex(view, 0f,1f,0f).setUv(0f,1f).setLight(16).setColor(color).setUv2(0,1).setNormal(0f,1f,0f);
        buff.addVertex(view, 1f,1f,0f).setUv(1f,1f).setLight(16).setColor(color).setUv2(1,1).setNormal(1f,1f,0f);
        buff.addVertex(view, 1f,0f,0f).setUv(1f,0f).setLight(16).setColor(color).setUv2(1,0).setNormal(1f,0f,0f);
        buff.addVertex(view, 0f,0f,0f).setUv(0f,0f).setLight(16).setColor(color).setUv2(0,0).setNormal(0f,0f,0f);
    }
    public static void makeDoubleSideSquare(BufferBuilder buff, int color, PoseStack stack){
        var pose = stack.last();
        var view = pose.pose();
        buff.addVertex(view, 0f,1f,0f).setUv(0f,1f).setLight(16).setColor(color).setUv1(0,1).setUv2(0,1).setNormal(0f,1f,0f);
        buff.addVertex(view, 1f,1f,0f).setUv(1f,1f).setLight(16).setColor(color).setUv1(0,1).setUv2(1,1).setNormal(1f,1f,0f);
        buff.addVertex(view, 1f,0f,0f).setUv(1f,0f).setLight(16).setColor(color).setUv1(1,0).setUv2(1,0).setNormal(1f,0f,0f);
        buff.addVertex(view, 0f,0f,0f).setUv(0f,0f).setLight(16).setColor(color).setUv1(1,0).setUv2(0,0).setNormal(0f,0f,0f);

        pose = stack.last();
        view = pose.pose();
        buff.addVertex(view, 1f,0f,0f).setUv(1f,0f).setLight(16).setColor(color).setUv1(0,1).setUv2(0,1).setNormal(1f,1f,0f);
        buff.addVertex(view, 1f,1f,0f).setUv(1f,1f).setLight(16).setColor(color).setUv1(1,1).setUv2(1,1).setNormal(1f,1f,0f);
        buff.addVertex(view, 0f,1f,0f).setUv(0f,1f).setLight(16).setColor(color).setUv1(1,0).setUv2(1,0).setNormal(0f,1f,0f);
        buff.addVertex(view, 0f,0f,0f).setUv(0f,0f).setLight(16).setColor(color).setUv1(0,0).setUv2(0,0).setNormal(0f,0f,0f);

    }
}
