package org.sophia.nodecode.logicSystems;

import org.sophia.nodecode.logicSystems.core.NodeEnv;

import static org.sophia.nodecode.Utils.ID;

public class Testing {
    public static void testing() {
        NodeEnv env = new NodeEnv();
        env.createNode(ID(""));
    }
}