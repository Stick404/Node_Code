package org.sophia.nodecode.registries.process;

import net.minecraft.resources.ResourceLocation;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.nodes.NodeAdd;

import java.util.HashMap;

import static org.sophia.nodecode.Utils.ID;
import static org.sophia.nodecode.registries.CustomRegistries.NODE_REGISTRY;

/**
 * Converts what has been Registered in NodeRegistry into functional nodes
 */
public class NodeProcess {
    public static final HashMap<ResourceLocation, Node> KNOWN_NODES = new HashMap<>();

    /**
     * Called then initiating the Nodes during start up. Should be called on the Client and Server
     */
    public static void NodeInit(String string){
        System.out.println("Registering Nodes on: " + string);
        for (var entry : NODE_REGISTRY.entrySet()){
            KNOWN_NODES.put(entry.getKey().location(), entry.getValue());
        }

        //TESTING AREA BELOW
        var e = ID("node_add");
        System.out.println("NAME!!");
        System.out.println(KNOWN_NODES.get(e).getClass().getName());
    }
}
