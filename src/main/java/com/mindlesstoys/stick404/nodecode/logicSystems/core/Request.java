package com.mindlesstoys.stick404.nodecode.logicSystems.core;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

/** Important to note: Requests are just a way to cleanly handle the data. This does not do any of the pushing/pulling, other systems handle that
 * @param source The {@link NodeEnv.Node} this request will pull from
 * @param pullSlot The slot this node will try to pull from
 * @param target The {@link NodeEnv.Node} this request will push too
 * @param targetSlot The slot this request will try to push to
 *
 */
public record Request(UUID source, Integer pullSlot, UUID target, Integer targetSlot) {
    /**
     * @return
     */
    public CompoundTag save(){
        CompoundTag tag = new CompoundTag();
        tag.putUUID("source",source);
        tag.putInt("pullslot",pullSlot);
        tag.putUUID("target",target);
        tag.putInt("targetslot",targetSlot);
        return tag;
    }
    public static Request load(CompoundTag tag){
        UUID source = tag.getUUID("source");
        Integer pullSlot = tag.getInt("pullslot");
        UUID target = tag.getUUID("target");
        Integer targetSlot = tag.getInt("targetslot");
        return new Request(source,pullSlot,target,targetSlot);
    }
}
