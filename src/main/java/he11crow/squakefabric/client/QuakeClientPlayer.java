package he11crow.squakefabric.client;

import he11crow.squakefabric.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.ChunkStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static he11crow.squakefabric.client.PlayerAPI.*;


public class QuakeClientPlayer {
    private static final Random random = new Random();

    private static final List<float[]> baseVelocities = new ArrayList<>();



    public static boolean moveEntityWithHeading(PlayerEntity player, float sidemove,float forwardmove)
    {

        if(!player.world.isClient) {
            return false;
        }

        if (!ModConfig.ENABLED)
            return false;

        boolean didQuakeMovement;
        double d0 = player.getX();
        double d1 = player.getY();
        double d2 = player.getZ();

        if ((player.getAbilities().flying || player.isFallFlying()) && player.getVehicle() == null)
            return false;
        else
            didQuakeMovement = quake_moveEntityWithHeading(player, sidemove, forwardmove);

        if (didQuakeMovement)
            player.increaseTravelMotionStats(player.getX() - d0, player.getY() - d1, player.getZ() - d2);

        return didQuakeMovement;
    }

    public static void beforeOnLivingUpdate(PlayerEntity player)
    {

        if(!player.world.isClient) {
            return;
        }


        if (!baseVelocities.isEmpty())
        {
            baseVelocities.clear();
        }

    }

    public static boolean moveRelativeBase(Entity entity, float sidemove, float forwardmove, float friction)
    {
        if (!(entity instanceof PlayerEntity))
            return false;

        return moveRelative((PlayerEntity)entity, sidemove, forwardmove, friction);
    }

    public static boolean moveRelative(PlayerEntity player, float sidemove, float forwardmove, float friction)
    {
        if(!player.world.isClient) {
            return false;
        }

        if (!ModConfig.ENABLED)
            return false;

        if ((player.getAbilities().flying && player.getVehicle() == null) || player.isTouchingWater()
                || player.isInLava() || player.isClimbing())
        {
            return false;
        }

        // this is probably wrong, but its what was there in 1.10.2
        float wishspeed = friction;
        wishspeed *= 2.15f;
        float[] wishdir = getMovementDirection(player, sidemove, forwardmove);
        float[] wishvel = new float[]{wishdir[0] * wishspeed, wishdir[1] * wishspeed};
        baseVelocities.add(wishvel);

        return true;
    }

    public static void afterJump(PlayerEntity player)
    {
        if(!player.world.isClient) {
            return;
        }

        if (!ModConfig.ENABLED)
            return;

        // undo this dumb thing
        if (player.isSprinting())
        {


            float f = player.getYaw() * 0.017453292F;

            double X = getMotionX(player);
            double Z = getMotionZ(player);

            X += MathHelper.sin(f) * 0.2F;
            Z -= MathHelper.cos(f) * 0.2F;

            setMotionXZ(player, X, Z);
        }

        quake_Jump(player);

    }

    /* =================================================
     * START HELPERS
     * =================================================
     */

    private static double getSpeed(PlayerEntity player)
    {
        double X = getMotionX(player);
        double Z = getMotionZ(player);

        return MathHelper.sqrt((float) (X * X + Z * Z));
    }

    private static float getSurfaceFriction(PlayerEntity player)
    {
        float f2 = 1.0F;

        if (player.isOnGround())
        {
            BlockPos groundPos = new BlockPos(MathHelper.floor(player.getX()), MathHelper.floor(player.getBoundingBox().minY) - 1, MathHelper.floor(player.getZ()));
            Block ground = player.world.getBlockState(groundPos).getBlock();
            f2 = 1.0F - ground.getSlipperiness();
        }

        return f2;
    }

    private static float getSlipperiness(PlayerEntity player)
    {
        float f2 = 1.0F;

        if(player.isOnGround())
        {
            BlockPos groundPos = new BlockPos(MathHelper.floor(player.getX()), MathHelper.floor(player.getBoundingBox().minY) - 1, MathHelper.floor(player.getZ()));
            f2 = 1.0F - PlayerAPI.getSlipperiness(player, groundPos);
        }

        return f2;
    }

    private static float minecraft_getMoveSpeed(PlayerEntity player)
    {
        float f2 = getSlipperiness(player);

        float f3 = 0.16277136F / (f2 * f2 * f2);

        return player.getMovementSpeed() * f3;
    }

    private static float[] getMovementDirection(PlayerEntity player, float sidemove, float forwardmove)
    {
        float f3 = sidemove * sidemove + forwardmove * forwardmove;
        float[] dir = {0.0F, 0.0F};

        if (f3 >= 1.0E-4F)
        {
            f3 = MathHelper.sqrt(f3);

            if (f3 < 1.0F)
            {
                f3 = 1.0F;
            }

            f3 = 1.0F / f3;
            sidemove *= f3;
            forwardmove *= f3;
            float f4 = MathHelper.sin((float) (player.getYaw() *  Math.PI / 180.0F));
            float f5 = MathHelper.cos((float) (player.getYaw() *  Math.PI / 180.0F));
            dir[0] = (sidemove * f5 - forwardmove * f4);
            dir[1] = (forwardmove * f5 + sidemove * f4);
        }

        return dir;
    }

    private static float quake_getMoveSpeed(PlayerEntity player)
    {
        float baseSpeed = player.getMovementSpeed();
        return !player.isSneaking() ? baseSpeed * 2.15F : baseSpeed * 1.11F;
    }

    private static float quake_getMaxMoveSpeed(PlayerEntity player)
    {
        float baseSpeed = player.getMovementSpeed();
        return baseSpeed * 2.15F;
    }

    private static void spawnBunnyhopParticles(PlayerEntity player, int numParticles)
    {
        // taken from sprint
        int j = MathHelper.floor(player.getX());
        int i = MathHelper.floor(player.getY() - 0.20000000298023224D - player.getHeightOffset());
        int k = MathHelper.floor(player.getZ());
        BlockState blockState = player.world.getBlockState(new BlockPos(j, i, k));

        if (blockState.getRenderType() != BlockRenderType.INVISIBLE)
        {
            for (int iParticle = 0; iParticle < numParticles; iParticle++)
            {
                player.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState),
                        player.getX() + (random.nextFloat() - 0.5D) * player.getWidth(),
                        player.getBoundingBox().minY + 0.1D, player.getZ() + (random.nextFloat() - 0.5D) * player.getWidth(),
                        -getMotionX(player) * 4.0D, 1.5D, -getMotionZ(player));
            }
        }
    }

    private static boolean isJumping(PlayerEntity player)
    {
        return SquakeFabricClient.isJumping;
    }

    /* =================================================
     * END HELPERS
     * =================================================
     */

    /* =================================================
     * START MINECRAFT PHYSICS
     * =================================================
     */

    private static void minecraft_ApplyGravity(PlayerEntity player)
    {

        double Y = getMotionY(player);

        if(player.world.isClient && (!player.world.isPosLoaded((int) player.getX(), (int) player.getZ()) || player.world.getChunk(new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ())).getStatus() != ChunkStatus.FULL))
        {
            if(player.getY() > 0.0D)
            {
                Y = -0.1D;
            } else
            {
                Y = 0.0D;
            }
        } else
        {
            // gravity
            Y -= 0.08D;
        }

        // air resistance
        Y *= 0.9800000190734863D;

        setMotionY(player, Y);
    }

    private static void minecraft_ApplyFriction(PlayerEntity player, float momentumRetention)
    {
        double X = getMotionX(player);
        double Z = getMotionZ(player);

        X *= momentumRetention;
        Z *= momentumRetention;
        setMotionX(player, X);
        setMotionZ(player, Z);
    }

    /*

    private static void minecraft_ApplyLadderPhysics(PlayerEntity player)
    {
        if (player.isClimbing())
        {
            float f5 = 0.15F;

            if (getMotionX(player) < (-f5))
            {
                setMotionX(player, (-f5));
            }

            if (getMotionX(player) > f5)
            {
                setMotionX(player, f5);
            }

            if (getMotionZ(player) < (-f5))
            {
                setMotionZ(player, (-f5));
            }

            if (getMotionZ(player) > f5)
            {
                setMotionZ(player, f5);
            }

            player.fallDistance = 0.0F;

            if (getMotionY(player) < -0.15D)
            {
                setMotionY(player, -0.15D);
            }

            boolean flag = player.isSneaking();

            if (flag && getMotionY(player) < 0.0D)
            {
                setMotionY(player, -0.0D);
            }
        }
    }

     */

    private static void minecraft_ClimbLadder(PlayerEntity player)
    {
        if (player.horizontalCollision && player.isClimbing())
        {
            setMotionY(player, -0.2D);
        }
    }

    private static void minecraft_SwingLimbsBasedOnMovement(PlayerEntity player)
    {
        player.lastLimbDistance = player.limbDistance;
        double d0 = player.getX() - prevX(player);
        double d1 = player.getZ() - prevZ(player);
        float f6 = MathHelper.sqrt((float) (d0 * d0 + d1 * d1)) * 4.0F;

        if (f6 > 1.0F)
        {
            f6 = 1.0F;
        }

        player.limbDistance += (f6 - player.limbDistance) * 0.4F;
        player.limbAngle += player.limbDistance;
    }

    private static void minecraft_WaterMove(PlayerEntity player, float sidemove, float forwardmove)
    {
        double X = getMotionX(player);
        double Y = getMotionX(player);
        double Z = getMotionZ(player);

        double d0 = player.getY();
        player.updateVelocity(0.04F, new Vec3d(sidemove, 0, forwardmove));
        player.move(MovementType.SELF, player.getVelocity());

        X *= 0.800000011920929D;
        Y *= 0.800000011920929D;
        Z *= 0.800000011920929D;
        Y -= 0.02D;

        player.setVelocity(X, Y, Z);

        if (player.horizontalCollision && isOffsetPositionInLiquid(player, getMotionX(player), getMotionY(player) + 0.6000000238418579D - player.getY() + d0, getMotionZ(player)))
        {
            setMotionY(player, 0.30000001192092896D);
        }
    }

    public static void minecraft_moveEntityWithHeading(PlayerEntity player, float sidemove, float forwardmove)
    {
        // take care of water and lava movement using default code
        if ((player.isTouchingWater() && !player.getAbilities().flying)
                || (player.isInLava() && !player.getAbilities().flying))
        {
            player.travel(new Vec3d(sidemove, 0, forwardmove));
        }
        else
        {
            // get friction
            float momentumRetention = getSlipperiness(player);

            // alter motionX/motionZ based on desired movement
            player.updateVelocity(minecraft_getMoveSpeed(player), new Vec3d(sidemove, 0, forwardmove));

            // make adjustments for ladder interaction
            // minecraft_ApplyLadderPhysics(player);

            // do the movement
            player.move(MovementType.SELF, player.getVelocity());

            // climb ladder here for some reason
            minecraft_ClimbLadder(player);

            // gravity + friction
            minecraft_ApplyGravity(player);
            minecraft_ApplyFriction(player, momentumRetention);

            // swing them arms
            minecraft_SwingLimbsBasedOnMovement(player);
        }
    }

    /* =================================================
     * END MINECRAFT PHYSICS
     * =================================================
     */

    /* =================================================
     * START QUAKE PHYSICS
     * =================================================
     */

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    public static boolean quake_moveEntityWithHeading(PlayerEntity player, float sidemove, float forwardmove)
    {
        // take care of ladder movement using default code

        if (player.isClimbing())
        {
            return false;
        }
        // take care of lava movement using default code
        else if ((player.isInLava() && !player.getAbilities().flying))
        {
            return false;
        }
        else if (player.isTouchingWater() && !player.getAbilities().flying)
        {
            if (ModConfig.SHARKING_ENABLED)
                quake_WaterMove(player, sidemove, forwardmove);
            else
            {
                return false;
            }
        }
        else
        {
            // get all relevant movement values
            float wishspeed = (sidemove != 0.0F || forwardmove != 0.0F) ? quake_getMoveSpeed(player) : 0.0F;
            float[] wishdir = getMovementDirection(player, sidemove, forwardmove);
            boolean onGroundForReal = player.isOnGround() && !isJumping(player);
            float momentumRetention = getSlipperiness(player);

            // ground movement
            if (onGroundForReal)
            {
                // apply friction before acceleration so we can accelerate back up to maxspeed afterwards
                //quake_Friction(); // buggy because material-based friction uses a totally different format
                minecraft_ApplyFriction(player, momentumRetention);

                double sv_accelerate = ModConfig.ACCELERATE;

                if (wishspeed != 0.0F)
                {
                    // alter based on the surface friction
                    sv_accelerate *= minecraft_getMoveSpeed(player) * 2.15F / wishspeed;

                    quake_Accelerate(player, wishspeed, wishdir[0], wishdir[1], sv_accelerate);
                }

                if (!baseVelocities.isEmpty())
                {
                    double X = getMotionX(player);
                    double Z = getMotionZ(player);

                    float speedMod = wishspeed / quake_getMaxMoveSpeed(player);
                    // add in base velocities
                    for (float[] baseVel : baseVelocities)
                    {
                        X += baseVel[0] * speedMod;
                        Z += baseVel[1] * speedMod;
                    }

                    setMotionX(player, X);
                    setMotionZ(player, Z);
                }
            }
            // air movement
            else
            {
                double sv_airaccelerate = ModConfig.AIR_ACCELERATE;
                quake_AirAccelerate(player, wishspeed, wishdir[0], wishdir[1], sv_airaccelerate);

                if (ModConfig.SHARKING_ENABLED && ModConfig.SHARKING_SURFACE_TENSION > 0.0D && isJumping(player) && getMotionY(player) < 0.0F)
                {
                    Box boundingBox = player.getBoundingBox().offset(player.getVelocity());
                    boolean isFallingIntoWater = player.world.containsFluid(boundingBox);

                    if (isFallingIntoWater)
                        multMotionY(player, ModConfig.SHARKING_SURFACE_TENSION);
                }
            }



            // apply velocity
            player.move(MovementType.SELF, player.getVelocity());



            // HL2 code applies half gravity before acceleration and half after acceleration, but this seems to work fine
            minecraft_ApplyGravity(player);




        }

        // swing them arms
        minecraft_SwingLimbsBasedOnMovement(player);

        return true;
    }

    private static void quake_Jump(PlayerEntity player)
    {
        quake_ApplySoftCap(player, quake_getMaxMoveSpeed(player));

        boolean didTrimp = quake_DoTrimp(player);

        if (!didTrimp)
        {
            quake_ApplyHardCap(player, quake_getMaxMoveSpeed(player));
        }
    }

    private static boolean quake_DoTrimp(PlayerEntity player)
    {
        if (ModConfig.TRIMPING_ENABLED && player.isSneaking())
        {
            double curspeed = getSpeed(player);
            float movespeed = quake_getMaxMoveSpeed(player);
            if (curspeed > movespeed)
            {
                double speedbonus = curspeed / movespeed * 0.5F;
                if (speedbonus > 1.0F)
                    speedbonus = 1.0F;

                addMotionY(player, speedbonus * curspeed * ModConfig.TRIMP_MULTIPLIER);

                if (ModConfig.TRIMP_MULTIPLIER > 0)
                {
                    double X = getMotionX(player);
                    double Z = getMotionZ(player);

                    float mult = 1.0f / ModConfig.TRIMP_MULTIPLIER;

                    X *= mult;
                    Z *= mult;
                    setMotionXZ(player, X, Z);
                }

                spawnBunnyhopParticles(player, 30);

                return true;
            }
        }

        return false;
    }

    private static void quake_ApplyWaterFriction(PlayerEntity player, double friction)
    {
        double X = getMotionX(player);
        double Y = getMotionY(player);
        double Z = getMotionZ(player);

        X *= friction;
        Y *= friction;
        Z *= friction;

        player.setVelocity(X, Y, Z);

		/*
		float speed = (float)(player.getSpeed());
		float newspeed = 0.0F;
		if (speed != 0.0F)
		{
			newspeed = speed - 0.05F * speed * friction; //* player->m_surfaceFriction;

			float mult = newspeed/speed;
			player.motionX *= mult;
			player.motionY *= mult;
			player.motionZ *= mult;
		}

		return newspeed;
		*/

		/*
		// slow in water
		player.motionX *= 0.800000011920929D;
		player.motionY *= 0.800000011920929D;
		player.motionZ *= 0.800000011920929D;
		*/
    }

    @SuppressWarnings("unused")
    private static void quake_WaterAccelerate(PlayerEntity player, float wishspeed, float speed, double wishX, double wishZ, double accel)
    {
        float addspeed = wishspeed - speed;
        if (addspeed > 0)
        {
            float accelspeed = (float) (accel * wishspeed * 0.05F);
            if (accelspeed > addspeed)
            {
                accelspeed = addspeed;
            }

            double X = getMotionX(player);
            double Z = getMotionZ(player);
            X += accelspeed * wishX;
            Z += accelspeed * wishZ;
            setMotionXZ(player, X, Z);
        }
    }

    private static void quake_WaterMove(PlayerEntity player, float sidemove, float forwardmove)
    {
        double lastPosY = player.getY();

        // get all relevant movement values
        float wishspeed = (sidemove != 0.0F || forwardmove != 0.0F) ? quake_getMaxMoveSpeed(player) : 0.0F;
        float[] wishdir = getMovementDirection(player, sidemove, forwardmove);
        boolean isSharking = isJumping(player) && isOffsetPositionInLiquid(player, 0.0D, 1.0D, 0.0D);
        double curspeed = getSpeed(player);

        if (!isSharking || curspeed < 0.078F)
        {
            minecraft_WaterMove(player, sidemove, forwardmove);
        }
        else
        {
            if(curspeed > 0.09)
                quake_ApplyWaterFriction(player, ModConfig.SHARKING_WATER_FRICTION);

            if(curspeed > 0.098)
                quake_AirAccelerate(player, wishspeed, wishdir[0], wishdir[1], ModConfig.ACCELERATE);
            else
                quake_Accelerate(player, .0980F, wishdir[0], wishdir[1], ModConfig.ACCELERATE);

            player.move(MovementType.SELF, player.getVelocity());

            setMotionY(player, 0.0D);
        }

        // water jump
        if (player.horizontalCollision && isOffsetPositionInLiquid(player, getMotionX(player),
                getMotionY(player) + 0.6000000238418579D - player.getY() + lastPosY, player.getZ()))
        {
            setMotionY(player, 0.30000001192092896D);
        }

        if (!baseVelocities.isEmpty())
        {
            float speedMod = wishspeed / quake_getMaxMoveSpeed(player);
            // add in base velocities
            double X = getMotionX(player);
            double Z = getMotionZ(player);
            for (float[] baseVel : baseVelocities)
            {
                X += baseVel[0] * speedMod;
                Z += baseVel[1] * speedMod;
            }
            setMotionXZ(player, X, Z);
        }
    }

    private static void quake_Accelerate(PlayerEntity player, float wishspeed, double wishX, double wishZ, double accel)
    {
        double addspeed, accelspeed, currentspeed;

        double X = getMotionX(player);
        double Z = getMotionZ(player);

        // Determine veer amount
        // this is a dot product
        currentspeed = getMotionX(player) * wishX + getMotionZ(player) * wishZ;

        // See how much to add
        addspeed = wishspeed - currentspeed;

        // If not adding any, done.
        if (addspeed <= 0)
            return;

        // Determine acceleration speed after acceleration
        accelspeed = accel * wishspeed / getSlipperiness(player) * 0.05F;

        // Cap it
        if (accelspeed > addspeed)
            accelspeed = addspeed;

        // Adjust pmove vel.
        X += accelspeed * wishX;
        Z += accelspeed * wishZ;

        setMotionXZ(player, X, Z);
    }

    private static void quake_AirAccelerate(PlayerEntity player, float wishspeed, double wishX, double wishZ, double accel)
    {
        double addspeed, accelspeed, currentspeed;

        double X = getMotionX(player);
        double Z = getMotionZ(player);

        float wishspd = wishspeed;
        float maxAirAcceleration = (float) ModConfig.MAX_AIR_ACCEL_PER_TICK;

        if (wishspd > maxAirAcceleration)
            wishspd = maxAirAcceleration;

        // Determine veer amount
        // this is a dot product
        currentspeed = getMotionX(player) * wishX + getMotionZ(player) * wishZ;

        // See how much to add
        addspeed = wishspd - currentspeed;

        // If not adding any, done.
        if (addspeed <= 0)
            return;

        // Determine acceleration speed after acceleration
        accelspeed = accel * wishspeed * 0.05F;

        // Cap it
        if (accelspeed > addspeed)
            accelspeed = addspeed;

        // Adjust pmove vel.
        X += accelspeed * wishX;
        Z += accelspeed * wishZ;
        setMotionXZ(player, X, Z);
    }

    @SuppressWarnings("unused")
    private static void quake_Friction(PlayerEntity player)
    {
        double speed, newspeed, control;

        float friction;
        float drop;

        // Calculate speed
        speed = getSpeed(player);

        // If too slow, return
        if (speed <= 0.0F)
        {
            return;
        }

        drop = 0.0F;

        // convars
        float sv_friction = 1.0F;
        float sv_stopspeed = 0.005F;

        float surfaceFriction = getSurfaceFriction(player);
        friction = sv_friction * surfaceFriction;

        // Bleed off some speed, but if we have less than the bleed
        //  threshold, bleed the threshold amount.
        control = (speed < sv_stopspeed) ? sv_stopspeed : speed;

        // Add the amount to the drop amount.
        drop += control * friction * 0.05F;

        // scale the velocity
        newspeed = speed - drop;
        if (newspeed < 0.0F)
            newspeed = 0.0F;

        double X = getMotionX(player);
        double Z = getMotionZ(player);
        if (newspeed != speed)
        {
            // Determine proportion of old speed we are using.
            newspeed /= speed;
            // Adjust velocity according to proportion.
            X *= newspeed;
            Z *= newspeed;
        }
        setMotionXZ(player, X, Z);
    }

    private static void quake_ApplySoftCap(PlayerEntity player, float movespeed)
    {
        float softCapPercent = ModConfig.SOFT_CAP;
        float softCapDegen = ModConfig.SOFT_CAP_DEGEN;

        double X = getMotionX(player);
        double Z = getMotionZ(player);

        if (ModConfig.UNCAPPED_BUNNYHOP_ENABLED)
        {
            softCapPercent = 1.0F;
            softCapDegen = 1.0F;
        }

        float speed = (float) (getSpeed(player));
        float softCap = movespeed * softCapPercent;

        // apply soft cap first; if soft -> hard is not done, then you can continually trigger only the hard cap and stay at the hard cap
        if (speed > softCap)
        {
            if (softCapDegen != 1.0F)
            {
                float applied_cap = (speed - softCap) * softCapDegen + softCap;
                float multi = applied_cap / speed;
                X *= multi;
                Z *= multi;
                setMotionXZ(player, X, Z);
            }

            spawnBunnyhopParticles(player, 10);
        }
    }

    private static void quake_ApplyHardCap(PlayerEntity player, float movespeed)
    {
        double X = getMotionX(player);
        double Z = getMotionZ(player);

        if (ModConfig.UNCAPPED_BUNNYHOP_ENABLED)
            return;

        float hardCapPercent = ModConfig.HARD_CAP;

        float speed = (float) (getSpeed(player));
        float hardCap = movespeed * hardCapPercent;

        if (speed > hardCap && hardCap != 0.0F)
        {
            float multi = hardCap / speed;
            multMotionX(player, multi);
            multMotionZ(player, multi);

            X *= multi;
            Z *= multi;

            setMotionXZ(player, X, Z);

            spawnBunnyhopParticles(player, 30);
        }
    }


    /* =================================================
     * END QUAKE PHYSICS
     * =================================================
     */
}
