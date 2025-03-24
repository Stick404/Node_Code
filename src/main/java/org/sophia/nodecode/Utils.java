package org.sophia.nodecode;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import static org.sophia.nodecode.Nodecode.MODID;

public class Utils {
    public static BlockPos setAxisVal(BlockPos pos, Direction.Axis axis, int val){
        return switch (axis){
            case X -> new BlockPos(val, pos.getY(), pos.getZ());
            case Y -> new BlockPos(pos.getX(), val, pos.getZ());
            case Z -> new BlockPos(pos.getX(), pos.getY(), val);
        };
    }
    public static ResourceLocation ID(String string){
        return ResourceLocation.fromNamespaceAndPath(MODID,string);
    }
}
