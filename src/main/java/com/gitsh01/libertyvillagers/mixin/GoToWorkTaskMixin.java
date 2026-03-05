package com.gitsh01.libertyvillagers.mixin;

import net.minecraft.entity.ai.brain.task.VillagerWorkTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import static com.gitsh01.libertyvillagers.LibertyVillagersMod.CONFIG;

@Mixin(VillagerWorkTask.class)
public class GoToWorkTaskMixin {

    // Inject into the lambda called by Task.trigger.
    @SuppressWarnings({"target", "descriptor"})
    @ModifyConstant(
            method = "shouldRun",
            constant = @Constant(doubleValue = 1.73))
    static private double modifyDistanceInShouldRun(double distance) {
        return Math.max(distance, CONFIG.villagerPathfindingConfig.minimumPOISearchDistance + 1);
    }
}
