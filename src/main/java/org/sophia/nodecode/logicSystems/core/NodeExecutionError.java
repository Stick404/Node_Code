package org.sophia.nodecode.logicSystems.core;

public class NodeExecutionError extends RuntimeException {
    public NodeExecutionError(String message) {
        super(message);
    }
    public NodeExecutionError() {super();}
}
