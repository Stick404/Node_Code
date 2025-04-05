package org.sophia.nodecode.logicSystems.core.registries;

import range.main.core.DataType;
import range.main.types.*;

import java.util.HashMap;

public class TypeRegistry {
    public static HashMap<String, Class<? extends DataType<?>>> typeSystem = new HashMap<>();
    //TODO: CHANGE TO A RESOURCE LOCATION AND MINECRAFT REGISTRY

    public static void init(){}

    public static final Class<? extends DataType<?>> DOUBLE = register("NUMBER", TypeDouble.class);
    public static final Class<? extends DataType<?>> ANY = register("ANY", TypeObject.class);
    public static final Class<? extends DataType<?>> NULL = register("NULL", TypeNull.class);
    public static final Class<? extends DataType<?>> BOOL = register("BOOL", TypeBoolean.class);
    public static final Class<? extends DataType<?>> STRING = register("STRING", TypeString.class);
    public static final Class<? extends DataType<?>> LIST = register("LIST", TypeList.class);

    //RECOMMENDED: Make `name` all caps
    private static Class<? extends DataType<?>> register(String name, Class<? extends DataType<?>> data){
        typeSystem.put(name,data);
        return data;
    }
}
