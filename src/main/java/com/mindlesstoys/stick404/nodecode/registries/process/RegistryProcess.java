package com.mindlesstoys.stick404.nodecode.registries.process;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import net.minecraft.resources.ResourceLocation;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.Func;

import java.util.HashMap;

import static com.mindlesstoys.stick404.nodecode.registries.CustomRegistries.DATA_TYPE_REGISTRY;
import static com.mindlesstoys.stick404.nodecode.registries.CustomRegistries.NODE_REGISTRY;

/**
 * Converts what has been Registered in NodeRegistry into functional nodes
 */
public class RegistryProcess {
    public static final HashMap<ResourceLocation, Func> KNOWN_NODES = new HashMap<>();
    public static final HashMap<ResourceLocation, DataType> KNOWN_TYPES = new HashMap<>();

    /**
     * Called when initiating the Nodes during start up. Should be called on the Client and Server
     */
    public static void NodeInit(String string){
        System.out.println("Registering Nodes on: " + string);
        for (var entry : NODE_REGISTRY.entrySet()){
            KNOWN_NODES.put(entry.getKey().location(), entry.getValue());
        }
    }

    public static void TypeInit(String string){
        System.out.println("Registering Types on: " + string);
        for (var entry : DATA_TYPE_REGISTRY.entrySet()){
            KNOWN_TYPES.put(entry.getKey().location(), entry.getValue());
        }
    }
}
