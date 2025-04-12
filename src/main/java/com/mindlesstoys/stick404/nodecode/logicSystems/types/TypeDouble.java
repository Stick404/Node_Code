package com.mindlesstoys.stick404.nodecode.logicSystems.types;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import com.mindlesstoys.stick404.nodecode.registries.DataTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class TypeDouble extends DataType<Double> {
    protected ResourceLocation dataName = DataTypeRegistry.TYPE_DOUBLE.getId();
    public TypeDouble(Double initVal) {
        super(Double.class, initVal);
    }

    public TypeDouble() {
        this(0.0);
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString(dataType,this.dataName.toString());
        tag.putDouble(dataSave,getData());
        return tag;
    }

    public static DataType<Double> load(CompoundTag tag) {
        return new TypeDouble(tag.getDouble(dataSave));
    }

    @Override
    public DataType<Double> loadT(CompoundTag tag) {
        return TypeDouble.load(tag);
    }

    @Override
    public int getColor() {
        return 0x2fb4db;
    }
}
