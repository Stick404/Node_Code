package com.mindlesstoys.stick404.nodecode;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import static com.mindlesstoys.stick404.nodecode.Nodecode.MODID;

public class Utils {
    /**
     * @param pos A BlockPos to modify the value of
     * @param axis The {@link net.minecraft.core.Direction.Axis} to select
     * @param val The Int value to set to the selected axis
     * @return a BlockPos with the axis changed to value
     */
    public static BlockPos setAxisVal(BlockPos pos, Direction.Axis axis, int val){
        return switch (axis){
            case X -> new BlockPos(val, pos.getY(), pos.getZ());
            case Y -> new BlockPos(pos.getX(), val, pos.getZ());
            case Z -> new BlockPos(pos.getX(), pos.getY(), val);
        };
    }

    /** Kind of clear, but:
     * @param string the input that would go into {@link ResourceLocation#fromNamespaceAndPath(String, String)}
     * @return the ResourceLocation of Node Code's MODID:string
     */
    public static ResourceLocation ID(String string){
        return ResourceLocation.fromNamespaceAndPath(MODID,string);
    }

    /** This is a method to solve the Ray-Slab Intersection method to find if a ray crosses a plane. As stated in <a href="https://www.cs.cornell.edu/courses/cs4620/2013fa/lectures/03raytracing1.pdf">Ray Tracing: intersection and shading</a>
     * @param cornerClose One corner of the cube (or plane) you are trying to find
     * @param cornerFar Another corner of the cube (or plane) you are trying to find
     * @param ray The length/direction of the ray you are using
     * @param pos The starting position of ray
     * @return a {@link Vec2}. Where the X is how far the ray is from the cube (<=0 means it is on/in the cube). And the X is how far the cube is from the ray's start position
     */
    public static Vec2 raySlabIntersection(Vec3 cornerClose, Vec3 cornerFar, Vec3 ray, Vec3 pos){
        cornerClose = cornerClose.add(0.01);
        cornerFar = cornerFar.subtract(-0.01);
        var XMin = (cornerClose.x -pos.x)/ray.x;
        var YMin = (cornerClose.y -pos.y)/ray.y;
        var ZMin = (cornerClose.z -pos.z)/ray.z;

        var XMax = (cornerFar.x -pos.x)/ray.x;
        var YMax = (cornerFar.y -pos.y)/ray.y;
        var ZMax = (cornerFar.z -pos.z)/ray.z;

        var XEnter = Math.min(XMin,XMax);
        var YEnter = Math.min(YMin,YMax);
        var ZEnter = Math.min(ZMin,ZMax);

        var XExit = Math.max(XMin,XMax);
        var YExit = Math.max(YMin,YMax);
        var ZExit = Math.max(ZMin,ZMax);

        var TEnter = Math.max(XEnter,Math.max(YEnter,ZEnter));
        var TExit = Math.min(XExit,Math.min(YExit,ZExit));

        return new Vec2((float) (TEnter - TExit),(float) TEnter);
    }
}
