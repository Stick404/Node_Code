package org.sophia.nodecode.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.nodes.NodeAdd;
import org.sophia.nodecode.logicSystems.nodes.NodeAppendList;
import org.sophia.nodecode.logicSystems.nodes.NodeGetList;
import org.sophia.nodecode.logicSystems.nodes.NodePrint;
import org.sophia.nodecode.logicSystems.nodes.inputs.NodeListInput;
import org.sophia.nodecode.logicSystems.nodes.inputs.NodeDoubleInput;
import org.sophia.nodecode.logicSystems.nodes.inputs.NodeStringInput;

import static org.sophia.nodecode.Nodecode.MODID;
import static org.sophia.nodecode.registries.CustomRegistries.NODE_REGISTRY;

public class NodeRegistry {
    public static final DeferredRegister<Node> NODES = DeferredRegister.create(NODE_REGISTRY,MODID);

    //Inputs
    public static final DeferredHolder<Node, NodeDoubleInput> NODE_DOUBLE_INPUT = NODES.register("double_input", () -> new NodeDoubleInput());
    public static final DeferredHolder<Node, NodeStringInput> NODE_STRING_INPUT = NODES.register("string_input", () -> new NodeStringInput());
    public static final DeferredHolder<Node, NodeListInput> NODE_ARRAY_INPUT = NODES.register("list_input", () -> new NodeListInput());

    //Consumers
    public static final DeferredHolder<Node, NodePrint> NODE_PRINT = NODES.register("node_print", () -> new NodePrint());

    //Numbers
    public static final DeferredHolder<Node, NodeAdd> NODE_ADD = NODES.register("node_add", () -> new NodeAdd());

    //Lists
    public static final DeferredHolder<Node, NodeAppendList> NODE_LIST_APPEND = NODES.register("list_append", () -> new NodeAppendList());
    public static final DeferredHolder<Node, NodeGetList> NODE_GET_LIST = NODES.register("list_get",() -> new NodeGetList());

    public static void init(IEventBus bus){
        NODES.register(bus);
    }
}
