package com.gitsh01.libertyvillagers.mixin;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.GatherItemsVillagerTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gitsh01.libertyvillagers.LibertyVillagersMod.CONFIG;

@Mixin(GatherItemsVillagerTask.class)
public abstract class GatherItemsVillagerTaskMixin {

    @Invoker("giveHalfOfStack")
    static void giveHalfOfStack(VillagerEntity villager, Set<Item> validItems, LivingEntity target) {
        throw new AssertionError();
    }

    @Inject(method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;" +
            "Lnet/minecraft/entity/passive/VillagerEntity;J)V",
            at = @At("HEAD"))
    private void keepRunning(ServerWorld serverWorld, VillagerEntity villagerEntity, long l, CallbackInfo ci) {
        if (villagerEntity.getBrain().getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).isEmpty()) {
            return;
        }
        VillagerEntity villagerEntity2 = (VillagerEntity)villagerEntity.getBrain().getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).get();
        if (villagerEntity.squaredDistanceTo(villagerEntity2) > 5.0) {
            return;
        }
        if (villagerEntity2.getVillagerData().profession() == VillagerProfession.FARMER) {
            GatherItemsVillagerTaskMixin.giveHalfOfStack(villagerEntity, ImmutableSet.of(Items.PUMPKIN),
                    villagerEntity2);
        }
        if ((CONFIG.villagersProfessionConfig.leatherworkersFeedCows &&
                villagerEntity2.getVillagerData().profession() == VillagerProfession.LEATHERWORKER) ||
                (CONFIG.villagersProfessionConfig.butchersFeedCows &&
                        villagerEntity2.getVillagerData().profession() == VillagerProfession.BUTCHER) ||
                (CONFIG.villagersProfessionConfig.butchersFeedSheep &&
                        villagerEntity2.getVillagerData().profession() == VillagerProfession.BUTCHER) ||
                (CONFIG.villagersProfessionConfig.shepherdsFeedSheep &&
                        villagerEntity2.getVillagerData().profession() == VillagerProfession.SHEPHERD)) {
            GatherItemsVillagerTaskMixin.giveHalfOfStack(villagerEntity, ImmutableSet.of(Items.WHEAT), villagerEntity2);
        }
        if (CONFIG.villagersProfessionConfig.butchersFeedPigs &&
                villagerEntity2.getVillagerData().profession() == VillagerProfession.BUTCHER) {
            GatherItemsVillagerTaskMixin.giveHalfOfStack(villagerEntity, ImmutableSet.of(Items.CARROT, Items.POTATO),
                    villagerEntity2);
        }
        if ((CONFIG.villagersProfessionConfig.butchersFeedChickens &&
                villagerEntity2.getVillagerData().profession() == VillagerProfession.BUTCHER) ||
                (CONFIG.villagersProfessionConfig.fletchersFeedChickens &&
                        villagerEntity2.getVillagerData().profession() == VillagerProfession.FLETCHER)) {
            GatherItemsVillagerTaskMixin.giveHalfOfStack(villagerEntity,
                    ImmutableSet.of(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS),
                    villagerEntity2);
        }
        if (CONFIG.villagersProfessionConfig.butchersFeedRabbits &&
                villagerEntity2.getVillagerData().profession() == VillagerProfession.BUTCHER) {
            GatherItemsVillagerTaskMixin.giveHalfOfStack(villagerEntity, ImmutableSet.of(Items.CARROT),
                    villagerEntity2);
        }
    }
    @Shadow
    private Set<Item> items;
    /**
     * @author Kin fuyuki
     * @reason lol dunno
     */
    @Overwrite
    private static Set<Item> getGatherableItems(VillagerEntity entity, VillagerEntity target){

        Set<Item> GATHERABLE_ITEMS =  Set.of();

        if (CONFIG.villagersProfessionConfig.farmersHarvestMelons) {
            GATHERABLE_ITEMS = new HashSet<>(GATHERABLE_ITEMS);
            GATHERABLE_ITEMS.add(Items.MELON_SLICE);
        }
        if (CONFIG.villagersGeneralConfig.villagersEatPumpkinPie) {
        GATHERABLE_ITEMS = new HashSet<>(GATHERABLE_ITEMS);
        GATHERABLE_ITEMS.add(Items.PUMPKIN_PIE);}
        if (CONFIG.villagersGeneralConfig.villagersEatCookedFish) {
            GATHERABLE_ITEMS = new HashSet<>(GATHERABLE_ITEMS);
            GATHERABLE_ITEMS.add(Items.COOKED_COD);
            GATHERABLE_ITEMS.add(Items.COOKED_SALMON);
        }
        ImmutableSet<Item> immutableSet = target.getVillagerData().profession().value().gatherableItems();
        ImmutableSet<Item> merged=Sets.union(immutableSet,GATHERABLE_ITEMS).immutableCopy();
        ImmutableSet<Item> immutableSet2 = entity.getVillagerData().profession().value().gatherableItems();
        ImmutableSet<Item> merged2=Sets.union(immutableSet2,GATHERABLE_ITEMS).immutableCopy();
        return merged.stream().filter(item -> !merged2.contains(item)).collect(Collectors.toSet());
    }
}
