package org.sophia.nodecode.logicSystems.core;

public abstract class DataType<T> {
    private final T data;
    private final Class<T> type;

    public DataType(Class<T> type, T initVal){
        this.data = initVal;
        this.type = type;
    }

    public T getData() {
        return data;
    }

    //TODO: Make each type have a Tag save/load

    public Class<T> getType() {
        return type;
    }

    public int getColor(){
        return 0xFFFFFF;
    }
}
