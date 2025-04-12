package com.mindlesstoys.stick404.nodecode.logicSystems.types;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import com.mindlesstoys.stick404.nodecode.registries.DataTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import static com.mindlesstoys.stick404.nodecode.Utils.ID;

public class TypeString extends DataType<String> {
    protected ResourceLocation dataName = DataTypeRegistry.TYPE_STRING.getId();

    public TypeString(String initVal) {
        super(String.class, initVal);
    }

    public TypeString(){
        this("");
    }

    public CompoundTag save(){
        CompoundTag tag = new CompoundTag();
        tag.putString(dataType,this.dataName.toString());
        tag.putString(dataSave,getData());
        return tag;
    }

    public static DataType<String> load(CompoundTag tag) {
        return new TypeString(tag.getString(dataSave));
    }

    @Override
    public DataType<String> loadT(CompoundTag tag) {
        return TypeString.load(tag);
    }

    @Override
    public int getColor() {
        return 0x8236de;
    }
}
