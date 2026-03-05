package com.gitsh01.libertyvillagers.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.LookTarget;
import net.minecraft.entity.ai.brain.task.TargetUtil;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.gitsh01.libertyvillagers.LibertyVillagersMod.CONFIG;

@Mixin(TargetUtil.class)
public class TargetUtilMixin {
    @Shadow
    public static void walkTowards(LivingEntity entity, LookTarget target, float speed, int completionRange){}
    @Inject(method = "walkTowards",at=@At("HEAD"))
    public static void walkTowards(LivingEntity entity, BlockPos target, float speed, int completionRange, CallbackInfo ci) {
        if (entity instanceof VillagerEntity) {
            walkTowards(entity, new BlockPosLookTarget(target), speed, Math.max(completionRange, CONFIG.villagerPathfindingConfig.minimumPOISearchDistance + 1));
            ci.cancel();}
    }
}
