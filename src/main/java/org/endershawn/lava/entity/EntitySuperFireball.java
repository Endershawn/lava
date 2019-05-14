package org.endershawn.lava.entity;

import org.endershawn.lava.Effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySuperFireball extends EntityLargeFireball {
	public float POWER = 2f;
	
	public EntitySuperFireball(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
	}

    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
        	Effects.immolateRange(world, result.hitVec, (float) (POWER * 1.5));

            // boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity);
            boolean flag = true;

            this.world.newExplosion((Entity)null, this.posX, this.posY, this.posZ, this.POWER, flag, flag);
            this.remove();
        }
    }
}