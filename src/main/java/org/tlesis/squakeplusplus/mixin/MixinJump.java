package org.tlesis.squakeplusplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tlesis.squakeplusplus.config.FeatureToggle;


import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public abstract class MixinJump {

    @Shadow int jumpingCooldown;
    
    @Inject(method = "tickMovement", at = @At(value = "HEAD"))
    public void tickMovement(CallbackInfo ci) {
        if (FeatureToggle.JUMP_SPAM.getBooleanValue()) {
            jumpingCooldown = 0;
        }
    }
}