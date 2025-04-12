package com.mindlesstoys.stick404.nodecode.items;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import com.mindlesstoys.stick404.nodecode.logicSystems.types.TypeDouble;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import static com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType.dataType;

public class TestingItem extends Item {
    public TestingItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel z){
            try{
                TypeDouble e = new TypeDouble(5.0);
                var saved = e.save();
                var name = saved.get(dataType);
                var loaded = TypeDouble.load(saved);
                System.out.println(name);
                System.out.println(loaded.getData());

                System.out.println(DataType.load(saved).getData());
                //Testing.testing();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
