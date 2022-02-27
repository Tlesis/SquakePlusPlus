package org.tlesis.squakefabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class MixinJump extends Entity {
    
    public MixinJump(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow int jumpingCooldown;
    
    @Inject(method = "tickMovement", at = @At(value = "HEAD"))
    public void tickMovement(CallbackInfo ci) {

        if (jumpingCooldown > 0) {
            jumpingCooldown = 0;
        }
    }
}

//TODO Inject a new getter and setter method that gets and sets jumpingCooldown
