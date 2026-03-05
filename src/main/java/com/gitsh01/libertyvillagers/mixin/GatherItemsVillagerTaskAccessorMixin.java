package com.gitsh01.libertyvillagers.mixin;

import net.minecraft.entity.ai.brain.task.GatherItemsVillagerTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(GatherItemsVillagerTask.class)
public interface GatherItemsVillagerTaskAccessorMixin {
    @Invoker("getGatherableItems")
    static Set<Item> callGetGatherableItems(VillagerEntity entity, VillagerEntity target) {
        throw new UnsupportedOperationException();
    }
}
