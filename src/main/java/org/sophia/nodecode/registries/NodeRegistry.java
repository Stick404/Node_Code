package org.sophia.nodecode.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.nodes.NodeAdd;
import org.sophia.nodecode.logicSystems.nodes.NodeInput;
import org.sophia.nodecode.logicSystems.nodes.NodePrint;

import static org.sophia.nodecode.Nodecode.MODID;
import static org.sophia.nodecode.registries.CustomRegistries.NODE_REGISTRY;

public class NodeRegistry {
    public static final DeferredRegister<Node> NODES = DeferredRegister.create(NODE_REGISTRY,MODID);

    //public static final DeferredHolder<Node, NodeInput<?>> INPUT_NODE = NODES.register("node_input", () -> );
    public static final DeferredHolder<Node, NodeAdd> NODE_ADD = NODES.register("node_add", () -> new NodeAdd());
    public static final DeferredHolder<Node, NodePrint> NODE_PRINT = NODES.register("node_print", () -> new NodePrint());
    public static final DeferredHolder<Node, NodeInput> NODE_INPUT = NODES.register("node_input", NodeInput::new);


    public static void init(IEventBus bus){
        NODES.register(bus);
    }
}
