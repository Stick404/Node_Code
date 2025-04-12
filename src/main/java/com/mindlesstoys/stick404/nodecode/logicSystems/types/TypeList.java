package com.mindlesstoys.stick404.nodecode.logicSystems.types;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import com.mindlesstoys.stick404.nodecode.registries.DataTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class TypeList extends DataType<ArrayList<DataType<?>>> {
    protected ResourceLocation dataName = DataTypeRegistry.TYPE_LIST.getId();
    //mmmm, casting
    public TypeList(ArrayList<DataType<?>> initVal) {
        super((Class<ArrayList<DataType<?>>>) new ArrayList<DataType<?>>().getClass(), initVal);
    }

    public TypeList(){
        this(new ArrayList<>());
    }

    @Override
    public int getColor() {
        return 0xc51fd4;
    }

    @Override
    public ArrayList<DataType<?>> getData() {
        return (ArrayList<DataType<?>>) super.getData().clone();
    }

    // DO NOT TRY TO SAVE TypeList, IT WILL BE LOADED WITH NOTHING
    public static DataType<ArrayList<DataType<?>>> load(CompoundTag tag) {
        var list = tag.getList(dataSave, Tag.TAG_COMPOUND);
        ArrayList<DataType<?>> dataList = new ArrayList<>();
        for (Tag a : list){
            CompoundTag data = (CompoundTag) a;
            dataList.add(DataType.load(data));
        }
        return new TypeList(dataList);
    }

    @Override
    public DataType<ArrayList<DataType<?>>> loadT(CompoundTag tag) {
        return TypeList.load(tag);
    }

    public CompoundTag save(){
        CompoundTag tag = new CompoundTag();
        tag.putString(dataType,this.dataName.toString());

        ListTag list = new ListTag();
        for (DataType<?> a : getData()) {
            list.add(a.save());
        }
        tag.put(dataSave,list);
        return tag;
    }
}
