package org.tlesis.squakeplusplus.event;

import org.tlesis.squakeplusplus.config.Configs.Speedometer;
import org.tlesis.squakeplusplus.util.ScreenPositions;

import fi.dy.masa.malilib.interfaces.IRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class RenderHandler implements IRenderer {

    public static RenderHandler renderHandler = new RenderHandler();

    private MinecraftClient mc;
    private TextRenderer textRenderer;

    private int color;
    private double lastFrameSpeed = 0.0;
    private float tickCounter = 0.0f;

    public void speedometerDraw(MatrixStack matrixStack, float tickDelta) {

        System.out.println("IDK] Called");

        this.mc = MinecraftClient.getInstance();
        this.textRenderer = mc.textRenderer;
        Entity entity = mc.getCameraEntity();

        double dx = entity.getX() - entity.lastRenderX;
        double dy = entity.getY() - entity.lastRenderY;
        double dz = entity.getZ() - entity.lastRenderZ;
        double currentSpeed = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (Speedometer.USE_COLORS.getBooleanValue()) {
            tickCounter += tickDelta;
            if (tickCounter >= (float)Speedometer.TICK_INTERVAL.getDoubleValue()) {
                if (currentSpeed < lastFrameSpeed) {
                    color = Speedometer.DECELERATING_COLOR.getColor().intValue;
                } else if (currentSpeed > lastFrameSpeed) {
                    color = Speedometer.ACCELERATING_COLOR.getColor().intValue;
                } else {
                    color = Speedometer.DEFAULT_COLOR.getColor().intValue;
                }
            }

            this.lastFrameSpeed = currentSpeed;
            this.tickCounter = 0.0f;
        }

        String currentSpeedText = String.format("%.2f blocks/sec", currentSpeed / 0.05F);
    
        // Calculate text position
        int horizWidth = this.textRenderer.getWidth(currentSpeedText);
        int height = this.textRenderer.fontHeight;
        int paddingX = 2;
        int paddingY = 2;
        int marginX = 4;
        int marginY = 4;
        int left = 0 + marginX;
        int top = 0 + marginY;
        int realHorizWidth = horizWidth + paddingX * 2 - 1;
        int realHeight = height + paddingY * 2 - 1;

        if (Speedometer.POSITIONS.getOptionListValue() == ScreenPositions.BOTTOM_LEFT) {
            top += mc.getWindow().getScaledHeight() - marginY * 2 - realHeight;

            left += paddingX;
            top += paddingY;
        }

        if (Speedometer.POSITIONS.getOptionListValue() == ScreenPositions.BOTTOM_RIGHT) {
            top += mc.getWindow().getScaledHeight() - marginY * 2 - realHeight;
            left += mc.getWindow().getScaledWidth() - marginX * 2 - realHorizWidth;

            left += paddingX;
            top += paddingY;
        }

        if (Speedometer.POSITIONS.getOptionListValue() == ScreenPositions.TOP_LEFT) {
            left += paddingX;
            top += paddingY;
        }

        if (Speedometer.POSITIONS.getOptionListValue() == ScreenPositions.TOP_RIGHT) {
            left += mc.getWindow().getScaledWidth() - marginX * 2 - realHorizWidth;

            left += paddingX;
            top += paddingY;
        }

        // Render the text
        this.textRenderer.drawWithShadow(matrixStack, currentSpeedText, left, top, color);

        return;
    }

    public void updateData(MinecraftClient mc) {
        if (mc.world != null) {}
    }

}