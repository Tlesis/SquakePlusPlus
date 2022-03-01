package org.tlesis.squakeplusplus.client;

import org.tlesis.squakeplusplus.config.Configs.Speedometer;
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
    private int lastColor;
    private double lastSpeed = 0.0;
    private int counter = 0; // Jank. don't worry about it. . .
    private float tickCounter = 0.0f;
    private double currentSpeed;

    public void speedometerDraw(MatrixStack matrixStack, float tickDelta) {

        this.mc = MinecraftClient.getInstance();
        this.textRenderer = mc.textRenderer;


        Vec3d playerPosVec = mc.player.getPos();
        double travelledX = playerPosVec.x - mc.player.prevX;
        double travelledZ = playerPosVec.z - mc.player.prevZ;
        // double travelledY = playerPosVec.y - mc.player.prevY;
        this.currentSpeed = (double)Math.sqrt((float)(travelledX * travelledX + travelledZ * travelledZ));

        if (Speedometer.USE_COLORS.getBooleanValue()) {
            tickCounter += tickDelta;
            counter++;
            if (tickCounter >= (float)Speedometer.TICK_INTERVAL.getIntegerValue()) {
                if (currentSpeed < lastSpeed) {
                    color = Speedometer.DECELERATING_COLOR.getColor().intValue;
                } else if (currentSpeed > lastSpeed) {
                    color = Speedometer.ACCELERATING_COLOR.getColor().intValue;
                } else {
                    color = Speedometer.DEFAULT_COLOR.getColor().intValue;
                }
            }

            if (counter >= 15) {
                this.counter = 0;
                this.lastColor = color;
                this.lastSpeed = currentSpeed;
            }

        }

        String currentSpeedText = String.format("%.2f", currentSpeed / 0.05f);
        String lastSpeedText = String.format("%.2f", lastSpeed / 0.05f);
    
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

            if (Speedometer.SHOW_LAST_SPEED.getBooleanValue()) {
                top -= 10;
            }
        }

        if (Speedometer.POSITIONS.getOptionListValue() == ScreenPositions.BOTTOM_RIGHT) {
            top += mc.getWindow().getScaledHeight() - marginY * 2 - realHeight;
            left += mc.getWindow().getScaledWidth() - marginX * 2 - realHorizWidth;

            left += paddingX;
            top += paddingY;

            if (Speedometer.SHOW_LAST_SPEED.getBooleanValue()) {
                top -= 10;
            }
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
        this.textRenderer.drawWithShadow(matrixStack, currentSpeedText, left, top, color);
        if (Speedometer.SHOW_LAST_SPEED.getBooleanValue()) {
            this.textRenderer.drawWithShadow(matrixStack, lastSpeedText, left, top + 10, lastColor);
        }

        return;
    }
}
