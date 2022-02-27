package org.tlesis.squakefabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tlesis.squakefabric.config.FeatureToggle;


import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public abstract class MixinJump {

    @Shadow int jumpingCooldown;
    
    @Inject(method = "tickMovement", at = @At(value = "HEAD"))
    public void tickMovement(CallbackInfo ci) {

        if (FeatureToggle.JUMP_SPAM.getBooleanValue())
            if (jumpingCooldown > 0) {
                jumpingCooldown = 0;
            }
    }
}