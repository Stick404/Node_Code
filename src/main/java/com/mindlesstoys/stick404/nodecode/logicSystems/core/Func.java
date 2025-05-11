package com.mindlesstoys.stick404.nodecode.logicSystems.core;

import org.jetbrains.annotations.Nullable;
import com.mindlesstoys.stick404.nodecode.logicSystems.Funcs.FuncAdd;
import com.mindlesstoys.stick404.nodecode.logicSystems.types.TypeNull;
import com.mindlesstoys.stick404.nodecode.logicSystems.types.TypeObject;

/**
 * The abstract Func, all Func subclasses extend from this root. See {@link FuncAdd} for an example of a basic Func
 */
public interface Func {
    DataType<?>[] inputTypes = new DataType[]{}; //The list of DataTypes this node will take in
    DataType<?>[] outputTypes = new DataType[]{}; //The list of DataTypes this node will output

    /**
     * @return The input types that this node will take
     */
    default DataType<?>[] getInputTypes() {
        return inputTypes;
    }

    /** Its better to try to use a color from {@link DefaultColors}, but a custom color can be used too
     * @return an int of the color this node should render as
     */
    default int getColor(){
        return DefaultColors.DEFAULT.color;
    }

    /**
     * @return The output types that this node will give
     */
    default DataType<?>[] getOutputTypes() {
        return outputTypes;
    }

    /**
     * @param inputs A list of {@link DataType}s formated with either null, or {@link Func#inputTypes}
     * @return A list of {@link DataType}s formated with either null, or {@link Func#outputTypes}
     */
    DataType<?>[] run(DataType<?>[] inputs, @Nullable DataType<?> extra);

    /**
     *  A protected method to return the value of a slot during Func Runtime. Meant to be a smarter replacement than manually casting Nodes to their values
     * @param inputs The input list to "parse," should be formated according too {@link Func#run(DataType[], DataType)}'s inputs
     * @param slot The slot to try to read from, ranges from 0 to {@link Func#inputTypes}'s max length
     * @return The wanted value of {@link Func#inputTypes}
     * @param <T> The value that is wanted. Should be the same as {@link Func#inputTypes}
     */
    @Nullable
    default <T> T getSlot(DataType<?>[] inputs, Integer slot){
        if (slot > inputs.length) throw new NodeExecutionError("tried to get output outside of bounds, target: " + slot + " max:" + (inputs.length -1));
        var value = inputs[slot];

        if (value.getClass().isInstance(this.getInputTypes()[slot])){
            //If the value is the one that is wanted, return it as said value
            return (T) this.getInputTypes()[slot].getClass().cast(value).getData();

        } else if (this.getInputTypes()[slot].getClass() == TypeObject.class) {
            //If the value is marked as "Object"/"Any," then return the raw DataType<?>
            return (T) inputs[slot];

        } else if (value.getClass() == TypeNull.class) {
            //If the value is given is a TypeNull, then just return null. This is for if we want a Nullable input
            return null;
        }
        //If all the if-checks fail, throw a NodeCastError to be picked up by the main Eval
        throw new NodeCastError(this.getInputTypes()[slot].getClass(),value.getClass());
    }
}
