package org.sophia.nodecode.logicSystems.core;

public record Request(Node node, Integer slot) {
    //Node is the node it tries to request from
    //Slot is the slot it pulls from
}
