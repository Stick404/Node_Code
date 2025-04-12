package com.mindlesstoys.stick404.nodecode.logicSystems.types;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import com.mindlesstoys.stick404.nodecode.registries.DataTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class TypeBoolean extends DataType<Boolean> {
    protected ResourceLocation dataName = DataTypeRegistry.TYPE_DOUBLE.getId();
    public TypeBoolean(Boolean initVal) {
        super(Boolean.class, initVal);
    }
    public TypeBoolean(){
        this(false);
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString(dataType,this.dataName.toString());
        tag.putBoolean(dataSave,this.getData());
        return tag;
    }

    public static DataType<Boolean> load(CompoundTag tag) {
        return new TypeBoolean(tag.getBoolean(dataSave));
    }

    @Override
    public DataType<Boolean> loadT(CompoundTag tag) {
        return TypeBoolean.load(tag);
    }

    public int getColor(){
        return 0x24bb42;
    }
}
