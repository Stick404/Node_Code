package org.sophia.nodecode.logicSystems.core;

public record Request(Node node, Integer pullSlot, Integer targetSlot) {
    // node is the node it tries to request from
    // pullSlot is the slot it pulls from
}
