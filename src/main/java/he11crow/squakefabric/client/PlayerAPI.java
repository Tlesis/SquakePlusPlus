package he11crow.squakefabric.client;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

public class PlayerAPI {

    public static double prevX(Entity player) {
        return player.prevX;
    }
    public static double prevZ(Entity player) {
        return player.prevZ;
    }

    public static void setMotionXZ(Entity player, double X, double Z) {
        player.setVelocity(X, getMotionY(player), Z);
    }

    public static double getMotionX(Entity player) {
        return player.getVelocity().x;
    }
    public static double getMotionY(Entity player) {
        return player.getVelocity().y;
    }
    public static double getMotionZ(Entity player) {
        return player.getVelocity().z;
    }

    public static void setMotionX(Entity player, double value) {
        player.setVelocity(value, getMotionY(player), getMotionZ(player));
    }
    public static void setMotionY(Entity player, double value) {
        player.setVelocity(getMotionX(player), value, getMotionZ(player));
    }
    public static void setMotionZ(Entity player, double value) {
        player.setVelocity(getMotionX(player), getMotionY(player), value);
    }

    public static void addMotionY(Entity player, double value) {
        player.setVelocity(getMotionX(player), getMotionY(player) + value, getMotionZ(player));
    }


    public static void multMotionX(Entity player, double value) {
        player.setVelocity(getMotionX(player) * value, getMotionY(player), getMotionZ(player));
    }
    public static void multMotionY(Entity player, double value) {
        player.setVelocity(getMotionX(player), getMotionY(player) * value, getMotionZ(player));
    }
    public static void multMotionZ(Entity player, double value) {
        player.setVelocity(getMotionX(player), getMotionY(player), getMotionZ(player) * value);
    }


    public static float getSlipperiness(Entity player, BlockPos pos) {
        return player.world.getBlockState(pos).getBlock().getSlipperiness();
    }

    public static boolean isOffsetPositionInLiquid(Entity player, double x, double y, double z) {
        Box boundingBox = player.getBoundingBox();
        boundingBox = moveBoundingBox(boundingBox, x, y, z);
        return isLiquidPresentInBB(player, boundingBox);
    }

    public static Box moveBoundingBox(Box bb, double x, double y, double z) {
        return new Box(bb.minX + x, bb.minY + y, bb.minZ + z, bb.maxX + x, bb.maxY + y, bb.maxZ + z);
    }

    public static boolean isLiquidPresentInBB(Entity player, Box bb) {
        boolean isInCollision = true;
        for (VoxelShape collision : player.world.getCollisions(player, bb)) {
            if (isInCollision) {
                if (!collision.isEmpty()) {
                    isInCollision = false;
                }
            }
        }
        return isInCollision && !player.world.containsFluid(bb);
    }
}
