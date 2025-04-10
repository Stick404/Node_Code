package com.mindlesstoys.stick404.nodecode.logicSystems.Funcs;

import org.jetbrains.annotations.Nullable;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.Func;
import com.mindlesstoys.stick404.nodecode.logicSystems.types.TypeDouble;

import static com.mindlesstoys.stick404.nodecode.registries.DataTypeRegistry.TYPE_DOUBLE;

/**
 * This is an extremely basic node, and a prime "example node"
 */
public class FuncAdd implements Func {

    /**
     * The Input Types and Output Types Are declared here <Br>
     * It is stated that the Input Types are 2 {@link TypeDouble}s, and the Output Type is a {@link TypeDouble}
     */
    public final static DataType<?>[] inputTypes = new DataType[]{TYPE_DOUBLE.get(),TYPE_DOUBLE.get()};
    public final static DataType<?>[] outputTypes = new DataType[]{TYPE_DOUBLE.get()};

    /**
     * @return the Input Types, as declared above
     */
    public DataType<?>[] getInputTypes() {
        return inputTypes;
    }

    /**
     * @return the Output Type, as declared above
     */
    public DataType<?>[] getOutputTypes() {
        return outputTypes;
    }

    /** This is a prime example of how {@link Func#run} works in an implemented node
     * @param inputs A list of 2 {@link TypeDouble}s
     * @return a {@link TypeDouble} of the 2 inputs added together
     */

    public DataType<?>[] run(DataType<?>[] inputs, @Nullable DataType<?> extra) {
        Double in1 = getSlot(inputs,0); //We get slot 0 of the Inputs, and state it is going to be a Double
        Double in2 = getSlot(inputs,1); //We get slot 1 of the Inputs, and state it is going to be a Double
        //We then form a new TypeDouble by adding the Double values together, then feeding them into a TypeDouble constructor
        return new DataType<?>[]{new TypeDouble(in1 + in2)};
    }
}
