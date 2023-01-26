package org.tlesis.squakeplusplus.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.tlesis.squakeplusplus.client.QuakeClientPlayer.*;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
    @Inject(method = "jump", at = @At("TAIL"))
    public void jumpInject(CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity)(Object)this);
        afterJump(player);
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travelInject(Vec3d movementInput, CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity)(Object)this);
        if(moveEntityWithHeading(player, (float) movementInput.x, (float) movementInput.z)) {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickInject(CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity)(Object)this);
        beforeOnLivingUpdate(player);
    }
    public boolean velChanged = false;

    @Inject(method = "handleFallDamage", at = @At("HEAD"))
    public void handleFallDamageInject(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = ((PlayerEntity)(Object)this);
        if(player.world.isClient) {
            return;
        }
        velChanged = player.velocityDirty;
    }

    @Inject(method = "handleFallDamage", at = @At("RETURN"), slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V"),
            to = @At("TAIL")
            )
    )
    public void handleFallDamageInjectSlice(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = ((PlayerEntity)(Object)this);
        if(player.world.isClient) {
            return;
        }
        player.velocityDirty = velChanged;
    }
}
