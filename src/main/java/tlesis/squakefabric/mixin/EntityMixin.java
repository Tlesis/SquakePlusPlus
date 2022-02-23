package tlesis.squakefabric.mixin;

import tlesis.squakefabric.client.QuakeClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
    public void updateVelocityInject(float speed, Vec3d movementInput, CallbackInfo ci) {
        Entity player = ((Entity)(Object)this);
        if(QuakeClientPlayer.moveRelativeBase(player, (float) movementInput.x, (float) movementInput.z, speed)) {
            ci.cancel();
        }
    }


}
