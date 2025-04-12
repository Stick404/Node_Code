package com.mindlesstoys.stick404.nodecode.logicSystems.types;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import com.mindlesstoys.stick404.nodecode.registries.DataTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class TypeObject extends DataType<Object> {
    protected ResourceLocation dataName = DataTypeRegistry.TYPE_ANY.getId();
    public TypeObject(Object initVal) {
        super(Object.class, initVal);
    }

    public TypeObject(){
        this(null);
    }

    // DO NOT TRY TO SAVE TypeObject, IT WILL BE LOADED WITH NULL

    public static DataType<Object> load(CompoundTag tag) {
        return new TypeObject();
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString(dataType,this.dataName.toString());
        return tag;
    }

    @Override
    public DataType<Object> loadT(CompoundTag tag) {
        return TypeObject.load(tag);
    }

    @Override
    public int getColor() {
        return 0xffffff;
    }
}
