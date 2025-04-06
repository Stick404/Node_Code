package org.sophia.nodecode.logicSystems.core;

public abstract class DataType<T> {
    private T data;
    private Class<T> type;

    public DataType(Class<T> type, T initVal){
        this.data = initVal;
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        if (data.getClass() != type) throw new NodeExecutionError("Tried to insert wrong type of data!");
        this.data = data;
    }

    public Class<T> getType() {
        return type;
    }

    public int getColor(){
        return 0xFFFFFF;
    }
}
