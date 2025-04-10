package com.mindlesstoys.stick404.nodecode.logicSystems.core;

public class NodeCastError extends NodeExecutionError {
    public NodeCastError(Class wanted, Class got){
      super("Func casting error, wanted:" + wanted + " got: " + got);
    }
}
