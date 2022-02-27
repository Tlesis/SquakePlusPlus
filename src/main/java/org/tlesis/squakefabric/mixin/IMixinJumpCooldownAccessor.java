package org.tlesis.squakefabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(net.minecraft.entity.LivingEntity.class)
public interface IMixinJumpCooldownAccessor {

    @Accessor("jumpingCooldown")
    int getJumpingCooldown();

    @Accessor("jumpingCooldown")
    public void setJumpingCooldown(int jumpingCooldown);
}
