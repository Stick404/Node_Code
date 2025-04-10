package com.mindlesstoys.stick404.nodecode.logicSystems.types;

import net.minecraft.core.BlockPos;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;

public class TypeBlockPos extends DataType<BlockPos> {
    public TypeBlockPos(BlockPos initVal) {
        super(BlockPos.class, initVal);
    }
    public TypeBlockPos(){
        this(BlockPos.ZERO);
    }

    @Override
    public int getColor() {
        return 0xb42e47;
    }
}
