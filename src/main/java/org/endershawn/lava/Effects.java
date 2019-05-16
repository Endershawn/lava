package org.endershawn.lava;

import java.util.List;
import java.util.Random;

import org.endershawn.lava.entity.EntitySuperFireball;
import org.endershawn.lava.item.LavaTier;
import org.endershawn.lava.item.armor.ArmorMaterialLava;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTiered;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Effects {
	private static int TICKS = 20;
	protected static final int EFFECT_DURATION = 5 * TICKS;
	private static final int JUMP_LEVEL = 3;
	protected static final AttributeModifier INC_SWIM_SPEED = new AttributeModifier("lava.inc_swimspeed", 6, 0);

	public static interface IAffectEntity {
		public void affect(Entity e);
	}

	public static void affectEntity(Entity e, IAffectEntity a) {
		a.affect(e);
	}

	public static void immolateEntity(Entity target) {
		if (!target.isImmuneToFire()) {
			target.setFire(10);

			if (target instanceof EntityLiving) {
				((EntityLiving) target).setHealth(.1f);
			}
		}
	}

	public static void burnBlocks(World worldIn, BlockPos pos, EnumFacing direction, int range, int temp) {
		BlockPos toPos = new BlockPos(pos);
		IBlockState fireState = Blocks.FIRE.getDefaultState();
		Random rand = new Random();

		if (direction.getAxis() == Axis.X) {
			toPos = toPos.add(0, range, range);
			pos = pos.add(0, 0, -(range));
		} else {
			toPos = toPos.add(range, range, 0);
			pos = pos.add(-(range), 0, 0);
		}

		Iterable<BlockPos> blocksBox = BlockPos.getAllInBox(pos, toPos.offset(direction, range * 2));

		for (BlockPos p : blocksBox) {
			Block b = worldIn.getBlockState(p).getBlock();

			if (rand.nextInt(100) < temp && b != Blocks.AIR) {
				worldIn.setBlockState(p, fireState);
			}
		}
	}

	public static void immolateRange(World worldIn, Vec3d v, float range) {
		List<Entity> entities = Effects.scanForEntites(worldIn, v, range);

		for (Entity ent : entities) {
			Effects.immolateEntity(ent);
		}
	}

	public static void dropFireball(World worldIn, BlockPos p) {
		EntityLargeFireball fb = new EntitySuperFireball(worldIn);
		fb.setPosition(p.getX(), worldIn.getActualHeight(), p.getZ());
		fb.accelerationY -= .3;
		worldIn.spawnEntity(fb);
	}

	public static void spawnLava(World worldIn, BlockPos p) {
		spawnLava(worldIn, p, 0);
	}

	/** this is redick **/
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
					worldIn.setBlockState(p.add(i, y, -i), Blocks.LAVA.getDefaultState());
					worldIn.setBlockState(p.add(-i, y, i), Blocks.LAVA.getDefaultState());
					worldIn.setBlockState(p.add(-i, y, -i), Blocks.LAVA.getDefaultState());
				}
			}

		}
	}

	public static List<Entity> scanForEntites(World worldIn, Vec3d vec, Float range) {
		AxisAlignedBB scanBB = new AxisAlignedBB(vec.add(-range, -range, -range), vec.add(range, range, range));

		List<Entity> entities = worldIn.getEntitiesWithinAABB(Entity.class, scanBB);
		return entities;
	}

	public static void lightningStrike(World worldIn, EntityPlayer player, BlockPos hitPos, Float size) {
		worldIn.spawnEntity(new EntityLightningBolt(worldIn, hitPos.getX(), hitPos.getY(), hitPos.getZ(), true));

		worldIn.createExplosion(player, hitPos.getX(), hitPos.getY(), hitPos.getZ(), size, true);

		for (Entity e : Effects.scanForEntites(worldIn, new Vec3d(hitPos), size)) {
			e.attackEntityFrom(DamageSource.LIGHTNING_BOLT, size * 2);
		}
	}

	private static boolean isLava(net.minecraft.item.Item i) {
		if (i instanceof ItemTiered) {
			ItemTiered it = (ItemTiered) i;
			return (it.getTier() instanceof LavaTier);
		}

		if (i instanceof ItemArmor) {
			ItemArmor ia = (ItemArmor) i;
			return (ia.getArmorMaterial() instanceof ArmorMaterialLava);
		}

		return false;
	}

	/** abstract to isHolding(tier) **/
	private static boolean holdingLava(EntityPlayer p) {
		return isLava(p.getHeldItemMainhand().getItem()) || isLava(p.getHeldItemOffhand().getItem());
	}

	/** abstract to isWearing(armorMaterial) **/
	static boolean wearingLava(EntityPlayer p) {
		for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
			if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
				net.minecraft.item.Item i = p.getItemStackFromSlot(slot).getItem();
				if (!isLava(i)) {
					return false;
				}
			}
		}

		return true;
	}

	private static void addEffect(Potion potion, EntityPlayer p) {
		addEffect(potion, p, 100);
	}

	private static void addEffect(Potion potion, EntityPlayer p, int level) {
		p.addPotionEffect(new PotionEffect(potion, EFFECT_DURATION + TICKS, level));
	}

	static void addFireResistance(EntityPlayer p) {
		addEffect(MobEffects.FIRE_RESISTANCE, p);
		p.extinguish();

	}

	private static void addJumpBoost(EntityPlayer p, int level) {
		addEffect(MobEffects.JUMP_BOOST, p, level);
	}

	protected static void lavaJump(EntityPlayer p) {
		lavaJump(p, JUMP_LEVEL);
	}

	protected static void lavaJump(EntityPlayer p, float level) {
		p.addVelocity(0, level, 0);
		Effects.spawnLava(p.getEntityWorld(), p.getPosition(), 2);
	}

	protected static void activateModifier(EntityPlayer p, IAttribute a, AttributeModifier m) {
		IAttributeInstance i = p.getAttribute(a);

		if (!i.hasModifier(m)) {
			i.applyModifier(m);
		}
	}

	protected static void deactivateModifier(EntityPlayer p, IAttribute a, AttributeModifier m) {
		IAttributeInstance i = p.getAttribute(a);

		if (i.hasModifier(m)) {
			i.removeModifier(m);
		}
	}

	protected static void increaseSwimSpeed(EntityPlayer p) {
		Effects.activateModifier(p, EntityLivingBase.SWIM_SPEED, Effects.INC_SWIM_SPEED);
	}

	protected static void resetSwimSpeed(EntityPlayer p) {
		Effects.deactivateModifier(p, EntityLivingBase.SWIM_SPEED, Effects.INC_SWIM_SPEED);
	}
}
