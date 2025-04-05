package org.sophia.nodecode.logicSystems.core.registries;

import range.main.core.Node;

import java.util.HashMap;
import java.util.function.Supplier;

public class NodeRegistry {
    //TODO: REPLACE THIS WITH A MINECRAFT REGISTRY SYSTEM
    //TODO: MAKE THIS WORK?
    public static HashMap<String, Node> nodeReg = new HashMap<>();

    public static Node register(String name, Supplier<Node> nodeSupplier){
        Node node = nodeSupplier.get();
        nodeReg.put(name,node);
        return node;
    }
}
