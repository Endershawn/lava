package org.endershawn.lava;

import java.util.List;
import java.util.Random;

//import org.endershawn.lava.entity.SuperFireball;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Effects {	
	public static interface IAffectEntity {
		public void affect(Entity e);
	}
	
	public static void affectEntity(Entity e, IAffectEntity a) {
		a.affect(e);
	}
	
	public static void immolateEntity(EntityLiving target) {
		if (!target.isImmuneToFire()) {
			target.setFire(10);
			target.setHealth(.1f);
		}
	}
	
	public static void burnBlocks(
			World worldIn, BlockPos pos, 
			EnumFacing direction, int range, int temp) {
		BlockPos toPos = new BlockPos(pos);
		IBlockState fireState = Blocks.FIRE.getDefaultState();
		Random rand = new Random();

		if (direction.getAxis() == Axis.X) {
			toPos = toPos.add(0, 0, range);
			pos = pos.add(0, 0, -(range));
		} else {
			toPos = toPos.add(range, 0, 0);
			pos = pos.add(-(range), 0, 0);
		}
		
		Iterable<BlockPos> leftArea = BlockPos.getAllInBox(
				pos, toPos.offset(direction, range * 2));
		
		for (BlockPos p: leftArea) {
			if (rand.nextInt(100) < temp) {
				worldIn.setBlockState(p, fireState);
			}
		}
	}
	
	public static void immolateRange(World worldIn, Vec3d v, float range) {
	    List<Entity> entities = Effects.scanForEntites(worldIn, v, range);
		
		for (Entity ent: entities) {
			if (ent instanceof EntityLiving) {
				Effects.immolateEntity((EntityLiving) ent);
			}
		}
	}
	
	public static void dropFireball(World worldIn, BlockPos p) {
		//	    EntityLargeFireball fb = new SuperFireball(worldIn);
	    EntityLargeFireball fb = new EntityLargeFireball(worldIn);
	    fb.setPosition(p.getX(), worldIn.getActualHeight(), p.getZ());
	    fb.accelerationY -= .2;
	    worldIn.spawnEntity(fb);
	}
	
	public static void spawnLava(World worldIn, BlockPos p) {
		spawnLava(worldIn, p, 0);
	}
	
	public static void spawnLava(World worldIn, BlockPos p, int rad) {
		int y = 0;
		worldIn.setBlockState(p, Blocks.LAVA.getDefaultState());
		
		if (rad > 0) {
			for (int i = 1; i <= rad; ++i) {
				worldIn.setBlockState(p.add(0, y, 0), Blocks.LAVA.getDefaultState());
				worldIn.setBlockState(p.add(i, y, 0), Blocks.LAVA.getDefaultState());
				worldIn.setBlockState(p.add(-i, y, 0), Blocks.LAVA.getDefaultState());
				worldIn.setBlockState(p.add(0, y, i), Blocks.LAVA.getDefaultState());
				worldIn.setBlockState(p.add(0, y, -i), Blocks.LAVA.getDefaultState());
				
				if (i < rad) {
					worldIn.setBlockState(p.add(i, y, i), Blocks.LAVA.getDefaultState());
					worldIn.setBlockState(p.add(-i, y, -i), Blocks.LAVA.getDefaultState());
				}
			}
		}
	}
	
	public static List<Entity> scanForEntites(World worldIn, Vec3d vec, Float range) {
	    AxisAlignedBB scanBB = new AxisAlignedBB(
	    		vec.add(-range, -range, -range), 
	    		vec.add(range,range, range));
	
	    List<Entity> entities = worldIn.getEntitiesWithinAABB(Entity.class, scanBB);
	    return entities;
	}
	
	public static void lightningStrike(World worldIn, EntityPlayer player, BlockPos hitPos, Float size) {
		worldIn.spawnEntity(
				new EntityLightningBolt(worldIn, hitPos.getX(), 
						                hitPos.getY(), hitPos.getZ(), true));
		
		worldIn.createExplosion(player, hitPos.getX(), hitPos.getY(), hitPos.getZ(), size, true);
		
		for (Entity e : Effects.scanForEntites(worldIn, new Vec3d(hitPos), size)) {
			e.attackEntityFrom(DamageSource.LIGHTNING_BOLT, size * 2);
		}
	}

}
