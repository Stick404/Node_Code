package com.mindlesstoys.stick404.nodecode.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.mindlesstoys.stick404.nodecode.logicSystems.Funcs.*;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.Func;
import com.mindlesstoys.stick404.nodecode.logicSystems.types.TypeDouble;
import com.mindlesstoys.stick404.nodecode.logicSystems.types.TypeList;
import com.mindlesstoys.stick404.nodecode.logicSystems.types.TypeString;

import java.util.ArrayList;

import static com.mindlesstoys.stick404.nodecode.Nodecode.MODID;
import static com.mindlesstoys.stick404.nodecode.registries.CustomRegistries.NODE_REGISTRY;

public class NodeRegistry {
    public static final DeferredRegister<Func> NODES = DeferredRegister.create(NODE_REGISTRY,MODID);

    //Inputs
    public static final DeferredHolder<Func, FuncInput<Double>> NODE_DOUBLE_INPUT = NODES.register("double_input", () -> new FuncInput<>(new TypeDouble(0.0)));
    public static final DeferredHolder<Func, FuncInput<String>> NODE_STRING_INPUT = NODES.register("string_input", () -> new FuncInput<>(new TypeString("")));
    public static final DeferredHolder<Func, FuncInput<ArrayList<DataType<?>>>> NODE_LIST_INPUT = NODES.register("list_input", () -> new FuncInput<>(new TypeList()));

    //Consumers
    public static final DeferredHolder<Func, FuncPrint> NODE_PRINT = NODES.register("node_print", FuncPrint::new);

    //Numbers
    public static final DeferredHolder<Func, FuncAdd> NODE_ADD = NODES.register("node_add", FuncAdd::new);

    //Lists
    public static final DeferredHolder<Func, FuncAppendList> NODE_LIST_APPEND = NODES.register("list_append", FuncAppendList::new);
    public static final DeferredHolder<Func, FuncGetList> NODE_GET_LIST = NODES.register("list_get", FuncGetList::new);

    public static void init(IEventBus bus){
        NODES.register(bus);
    }
}
