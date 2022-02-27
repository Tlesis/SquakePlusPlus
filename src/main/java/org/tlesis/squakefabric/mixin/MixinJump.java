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

    private LivingEntity livingEntity;

    @Shadow int jumpingCooldown;
    
    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void tickMovement(CallbackInfo ci) {

        if (this.livingEntity != null) {
            System.out.println("LivingEntity Not Null");
            if (((MixinJump) (Object) livingEntity).jumpingCooldown > 0) {
                ((MixinJump) (Object) livingEntity).jumpingCooldown = 0;
                System.out.println(String.format("Set JumpCooldown to %d", ((MixinJump) (Object) livingEntity).jumpingCooldown));
            }
        }
    }
}

//TODO Inject a new getter and setter method that gets and sets jumpingCooldown
