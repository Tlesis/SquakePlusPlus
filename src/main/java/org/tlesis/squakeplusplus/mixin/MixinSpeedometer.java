package org.tlesis.squakeplusplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tlesis.squakeplusplus.config.FeatureToggle;
import org.tlesis.squakeplusplus.event.RenderHandler;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameHud.class)
public class MixinSpeedometer {
    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
    private void renderSpeedometer(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        if (FeatureToggle.SPEEDOMETER.getBooleanValue()) {
            RenderHandler.renderHandler.speedometerDraw(matrices, tickDelta);
        }
    }
}