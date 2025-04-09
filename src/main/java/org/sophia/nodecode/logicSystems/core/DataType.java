package org.sophia.nodecode.logicSystems.core;

public abstract class DataType<T> {
    private final T data;
    private final Class<T> type;

    /**
     * This constructor should only be used in DataTypes that extend DataType
     * @param type The class that this DataType will be holding
     * @param initVal The value that this DataType will be holding
     */

    public DataType(Class<T> type, T initVal){
        this.data = initVal;
        this.type = type;
    }

    /**
     * @return the held data of the DataType
     */
    public T getData() {
        return data;
    }

    //TODO: Make each type have a Tag save/load

    /**
     * @return the held class of the DataType
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * @return the color of the node. Used for rendering
     */
    public int getColor(){
        return 0xFFFFFF;
    }
}
