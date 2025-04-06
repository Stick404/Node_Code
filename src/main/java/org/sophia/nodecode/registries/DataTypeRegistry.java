package org.sophia.nodecode.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.types.*;

import static org.sophia.nodecode.Nodecode.MODID;
import static org.sophia.nodecode.registries.CustomRegistries.DATA_TYPE_REGISTRY;

public class DataTypeRegistry {
    public static final DeferredRegister<DataType<?>> TYPES = DeferredRegister.create(DATA_TYPE_REGISTRY,MODID);

    public static final DeferredHolder<DataType<?>, TypeBoolean> TYPE_BOOLEAN = TYPES.register("boolean", () -> new TypeBoolean());
    public static final DeferredHolder<DataType<?>, TypeDouble> TYPE_DOUBLE = TYPES.register("double", () -> new TypeDouble());
    public static final DeferredHolder<DataType<?>, TypeList> TYPE_LIST = TYPES.register("list", () -> new TypeList());
    public static final DeferredHolder<DataType<?>, TypeNull> TYPE_NULL = TYPES.register("null", () -> new TypeNull());
    public static final DeferredHolder<DataType<?>, TypeObject> TYPE_ANY = TYPES.register("any", () -> new TypeObject());
    public static final DeferredHolder<DataType<?>, TypeString> TYPE_STRING = TYPES.register("string", () -> new TypeString());

    public static void init(IEventBus bus){
        TYPES.register(bus);
    }
}
