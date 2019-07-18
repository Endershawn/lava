package org.endershawn.lava.entity;

import org.endershawn.lava.Effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntitySuperFireball extends FireballEntity {
	public float POWER = 2f;
	
	public EntitySuperFireball(World worldIn) {
		super(null, worldIn);
		// TODO Auto-generated constructor stub
	}

    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
        	Effects.immolateRange(world, result.getHitVec(), (float) (POWER * 1.5));

            // boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity);
            Explosion.Mode flag = Explosion.Mode.DESTROY;

            this.world.createExplosion((Entity)null, this.posX, this.posY, this.posZ, this.POWER, flag);
            this.remove();
        }
    }
}