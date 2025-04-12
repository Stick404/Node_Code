package com.mindlesstoys.stick404.nodecode.logicSystems.types;

import com.mindlesstoys.stick404.nodecode.registries.DataTypeRegistry;
import net.minecraft.core.BlockPos;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;

public class TypeBlockPos extends DataType<BlockPos> {
    protected ResourceLocation dataName = DataTypeRegistry.TYPE_BLOCK_POS.getId();
    public TypeBlockPos(BlockPos initVal) {
        super(BlockPos.class, initVal);
    }
    public TypeBlockPos(){
        this(BlockPos.ZERO);
    }

    public static DataType<BlockPos> load(CompoundTag tag) {
        var pos = ((IntArrayTag) tag.get(dataSave)).getAsIntArray();
        return new TypeBlockPos(new BlockPos(new BlockPos(pos[0], pos[1], pos[2])));
    }

    @Override
    public DataType<BlockPos> loadT(CompoundTag tag) {
        return TypeBlockPos.load(tag);
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString(dataType,this.dataName.toString());
        tag.put(dataSave, NbtUtils.writeBlockPos(getData()));
        return tag;
    }

    @Override
    public int getColor() {
        return 0xb42e47;
    }
}
