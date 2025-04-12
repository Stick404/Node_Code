package com.mindlesstoys.stick404.nodecode.logicSystems.types;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import com.mindlesstoys.stick404.nodecode.registries.DataTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class TypeNull extends DataType<Void> {
    protected ResourceLocation dataName = DataTypeRegistry.TYPE_NULL.getId();
    public TypeNull() {
        super(Void.class, null);
    }

    public static DataType<Void> load(CompoundTag tag) {
        return new TypeNull();
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString(dataType,this.dataName.toString());
        return tag;
    }

    @Override
    public DataType<Void> loadT(CompoundTag tag) {
        return TypeNull.load(tag);
    }

    @Override
    public int getColor() {
        return 0x6a6a6a;
    }
}
