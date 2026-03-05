package com.gitsh01.libertyvillagers.mixin;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.WalkTowardsJobSiteTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.gitsh01.libertyvillagers.LibertyVillagersMod.CONFIG;

@Mixin(WalkTowardsJobSiteTask.class)
public class WalkTowardJobSiteTaskMixin {

    @Inject(method = "keepRunning",
            at = @At("HEAD"),
            cancellable = true)
    private void dontSetWalkTargetIfAlreadySet(ServerWorld serverWorld, VillagerEntity villagerEntity, long l,
                                               CallbackInfo ci) {
        // Prevent the villager from spamming the brain over and over with the same walk target.
        if (villagerEntity.getBrain().hasMemoryModule(MemoryModuleType.WALK_TARGET)) {
            ci.cancel();
        }
    }

}
