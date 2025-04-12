package com.mindlesstoys.stick404.nodecode.logicSystems.core;

import com.mindlesstoys.stick404.nodecode.logicSystems.types.TypeList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import static com.mindlesstoys.stick404.nodecode.registries.process.RegistryProcess.KNOWN_TYPES;

public abstract class DataType<T> {
    private final T data;
    private final Class<T> type;
    public static String dataSave = "data";
    public static String dataType = "type";

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


    /**
     * @return the held class of the DataType
     */
    public Class<T> getType() {
        return type;
    }

    /** Used for serializing the data into Minecraft Tags so they can be saved for later. These will mostly be called in {@link NodeEnv#save()}}
     * @return A tag with the data saved into it
     */
    public CompoundTag save(){
        return new CompoundTag();
    }

    /** This method is primarily meant for {@link TypeList#load(CompoundTag)}, but it can be used to load any DataType from a tag; if it is registered
     * @param tag The tag to attempt to load from
     * @return The datatype the CompoundTag encodes
     */
    public static DataType<?> load(CompoundTag tag) {
        String type = tag.getString(dataType);
        ResourceLocation location = ResourceLocation.parse(type);
        return KNOWN_TYPES.get(location).loadT(tag);
    }

    abstract public DataType<T> loadT(CompoundTag tag);

    /**
     * @return the color of the node. Used for rendering
     */
    public int getColor(){
        return 0xFFFFFF;
    }
}
