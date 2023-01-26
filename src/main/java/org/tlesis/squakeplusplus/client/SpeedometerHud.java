package org.tlesis.squakeplusplus.client;

import org.tlesis.squakeplusplus.config.Configs.Speedometer;
import org.tlesis.squakeplusplus.scheduler.ClientTickHandler;
import org.tlesis.squakeplusplus.util.ScreenPositions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class SpeedometerHud {

    public static SpeedometerHud speedometerHud = new SpeedometerHud();

    private MinecraftClient mc;
    private TextRenderer textRenderer;

    private int color = Speedometer.DEFAULT_COLOR.getColor().intValue;
    private double lastSpeed = 0.0;
    private float tickCounter = 0.0f;
    private double currentSpeed = 0.0;
    private String currentSpeedText = "0.0";
    private String lastSpeedText = "0.0";

    public void speedometerDraw(MatrixStack matrixStack, float tickDelta) {

        this.mc = MinecraftClient.getInstance();
        this.textRenderer = mc.textRenderer;


        Vec3d playerPosVec = mc.player.getPos();
        double travelledX = playerPosVec.x - mc.player.prevX;
        double travelledZ = playerPosVec.z - mc.player.prevZ;
        // double travelledY = playerPosVec.y - mc.player.prevY;
        this.currentSpeed = Math.sqrt((travelledX * travelledX + travelledZ * travelledZ));

        if (Speedometer.USE_COLORS.getBooleanValue() && mc.player.isOnGround()) {
            tickCounter += tickDelta;
            if (tickCounter >= (float)Speedometer.TICK_INTERVAL.getIntegerValue()) {
                if (currentSpeed < lastSpeed) {
                    color = Speedometer.DECELERATING_COLOR.getColor().intValue;
                } else if (currentSpeed > lastSpeed) {
                    color = Speedometer.ACCELERATING_COLOR.getColor().intValue;
                } else if (currentSpeed == lastSpeed && (!mc.player.isOnGround())) {
                    color = Speedometer.DEFAULT_COLOR.getColor().intValue;
                }
            }
            this.lastSpeed = currentSpeed;
        }
         
        /* if (mc.player.isOnGround()) {
            float fixedCurrentSpeed = (float)currentSpeed / 0.05f;
            float fixedLastSpeed = (float)lastSpeed / 0.05f;
        } */

        this.currentSpeedText = String.format("%.2f", currentSpeed / 0.05f);

        if (mc.player.isOnGround() && ClientTickHandler.isJumping) {
            this.lastSpeedText = String.format("%.2f", lastSpeed / 0.05f);
        }
    
        // Calculate text position
        int horizWidth = this.textRenderer.getWidth(currentSpeedText);
        final int height = this.textRenderer.fontHeight;
        final int paddingX = 2;
        final int paddingY = 2;
        final int marginX = 4;
        final int marginY = 4;
        int left = 0 + marginX;
        int top = 0 + marginY;
        final int realHorizWidth = horizWidth + paddingX * 2 - 1;
        final int realHeight = height + paddingY * 2 - 1;

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

        if (Speedometer.POSITIONS.getOptionListValue() == ScreenPositions.CENTER) {
            left += mc.getWindow().getScaledWidth() / 2;
            top += mc.getWindow().getScaledHeight() / 2;

            if ((this.currentSpeed / 0.05f) >= 10.0 && (this.currentSpeed / 0.05f) < 100.0) {
                left -= realHorizWidth - 13;
                top += 2;
            } else {
                left -= realHorizWidth - 10;
                top += 2;
            }
        }

        // Render the text
        if (Speedometer.WHEN_JUMPING.getBooleanValue() && ClientTickHandler.isJumping) {

            this.textRenderer.drawWithShadow(matrixStack, currentSpeedText, left, top, Speedometer.DEFAULT_COLOR.getColor().intValue);
            if (Speedometer.SHOW_LAST_SPEED.getBooleanValue()) {
                this.textRenderer.drawWithShadow(matrixStack, lastSpeedText, left, top + 10, color);
            }

        } else if (!Speedometer.WHEN_JUMPING.getBooleanValue()) {

            this.textRenderer.drawWithShadow(matrixStack, currentSpeedText, left, top, Speedometer.DEFAULT_COLOR.getColor().intValue);
            if (Speedometer.SHOW_LAST_SPEED.getBooleanValue()) {
                this.textRenderer.drawWithShadow(matrixStack, lastSpeedText, left, top + 10, color);
            }
        }

        return;
    }
}
