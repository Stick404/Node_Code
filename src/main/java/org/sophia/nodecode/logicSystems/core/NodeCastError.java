package org.sophia.nodecode.logicSystems.core;

public class NodeCastError extends NodeExecutionError {
    public NodeCastError(Class wanted, Class got){
      super("Node casting error, wanted:" + wanted + " got: " + got);
    }
}
