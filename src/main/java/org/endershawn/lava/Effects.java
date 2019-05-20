package org.endershawn.lava;

import java.util.List;
import java.util.Random;

import org.endershawn.lava.entity.EntitySuperFireball;
import org.endershawn.lava.item.LavaTier;
import org.endershawn.lava.item.ModItems;
import org.endershawn.lava.item.armor.ArmorMaterialLava;
import org.endershawn.lava.item.sword.HammerFire;
import org.endershawn.lava.item.sword.Sithe;
import org.endershawn.lava.item.sword.SwordBase;
import org.endershawn.lava.item.sword.SwordLava;
import org.endershawn.lava.item.sword.SwordThunder;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.InputMappings;
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
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTiered;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class Effects {
	private static int TICKS = 20;
	private static final float JUMP_LEVEL = 2.5f;
	protected static final int EFFECT_DURATION = 5 * TICKS;
	protected static final AttributeModifier INC_SWIM_SPEED = 
			new AttributeModifier("lava.inc_swimspeed", 6, 0);

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
		if (worldIn.isRemote) {return;}
		
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
		if (!worldIn.isRemote) {
			EntityLargeFireball fb = new EntitySuperFireball(worldIn);
			fb.setPosition(p.getX(), worldIn.getActualHeight(), p.getZ());
			fb.accelerationY -= .3;
			worldIn.spawnEntity(fb);
		}
	}

	public static void spawnLavaAtVec(World worldIn, Vec3d vec) {
		if (worldIn.isRemote) {return;}
		BlockPos pos = new BlockPos(vec);
		
		if (SwordLava.BLAST_SIZE > 0 && ModItems.swordLava.canDestroy(worldIn.getBlockState(pos))) {
			worldIn.createExplosion(
					null, 
					pos.getX(), 
					pos.getY(), 
					pos.getZ(), 
					SwordLava.BLAST_SIZE, 
					true);
			Effects.spawnLava(worldIn, pos, SwordLava.BLAST_SIZE);
		}
	}
	
	public static void spawnLava(World worldIn, BlockPos p) {
		spawnLava(worldIn, p, 0);
	}

	/** this is redick **/
	public static void spawnLava(World worldIn, BlockPos p, int rad) {
		if (worldIn.isRemote) {return;}
		
		int y = -1;
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

		if (worldIn.isRemote) {
			worldIn.spawnEntity(
					new EntityLightningBolt(
							worldIn, 
							hitPos.getX(), 
							hitPos.getY(), 
							hitPos.getZ(), 
							true));
		} else {
			worldIn.createExplosion(
					player, 
					hitPos.getX(), 
					hitPos.getY(), 
					hitPos.getZ(), 
					size, 
					true);
			
			for (Entity e : Effects.scanForEntites(worldIn, new Vec3d(hitPos), size)) {
				e.attackEntityFrom(DamageSource.LIGHTNING_BOLT, size * 2);
			}
		}
	}
	
	public static void lightningStrike(World worldIn, EntityPlayer player, Vec3d hitVec, Float size) {
		lightningStrike(worldIn, player, new BlockPos(hitVec), size);
	}

	public static void lightningStrikeCrosshair(EntityPlayer player) {
		World world = player.getEntityWorld();
		
		RayTraceResult rtr = Effects.rayTrace(
				world, 
				player, 
				true, 
				SwordThunder.REACH_MULT);
	
		if (rtr != null) {
			if (rtr.type != RayTraceResult.Type.MISS) {
					Effects.lightningStrike(
							world, 
							player, 
							rtr.getBlockPos(), 
							SwordThunder.BLAST_SIZE);
					}
		}
	}
	
	public static void fireballCrosshair(EntityPlayer player) {
		World worldIn = player.getEntityWorld();
		RayTraceResult rtr = Effects.rayTrace(worldIn, player, true, HammerFire.REACH_MULT);
		
		if (rtr != null && rtr.type != RayTraceResult.Type.MISS) {
			    Effects.dropFireball(worldIn, rtr.getBlockPos());
		}
	}
	
	public static void immolateCrosshair(EntityPlayer player) {
		World worldIn = player.getEntityWorld();
		
		RayTraceResult rtr = Effects.rayTrace(worldIn, player, true, Sithe.REACH_MULT);
		Effects.immolateRange(worldIn, rtr.hitVec, 1f);
	}
	
	public static void burnTargetArea(EntityPlayer player, Vec3d area) {
		World worldIn = player.getEntityWorld();
		EnumFacing facing = player.getHorizontalFacing();
		BlockPos target = new BlockPos(area);
		
		Effects.burnBlocks(worldIn, target, facing, HammerFire.BLAST_SIZE, HammerFire.TEMPERATURE);
	}
	
	private static boolean isLava(Item i) {
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

	private static boolean isTier(Item i, IItemTier tier) {
		if (i instanceof ItemTiered) {
			ItemTiered it = (ItemTiered)i;
			return (it.getTier().getClass().equals(tier.getClass()));
		}

		return false;
	}

	static boolean isHoldingTier(EntityPlayer p, IItemTier tier) {
		Item main = p.getHeldItemMainhand().getItem();
		Item off = p.getHeldItemOffhand().getItem();

		return (isTier(main, tier) || isTier(off, tier));
	}

	static boolean wearingLava(EntityPlayer p) {
		return isWearing(p, ModItems.armorMaterialLava);
	}

	public static boolean isWearing(EntityPlayer p, IArmorMaterial m) {
		for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
			if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
				Item i = p.getItemStackFromSlot(slot).getItem();
				if (!(i instanceof ItemArmor)) {return false;}
				if (!((ItemArmor)i).getArmorMaterial().getClass().equals(m.getClass())) {
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
	protected static void lavaJump(EntityPlayer p) {
		lavaJump(p, JUMP_LEVEL);
	}

	protected static void lavaJump(EntityPlayer p, float level) {		
		p.addVelocity(0, level, 0);
		Effects.spawnLava(p.getEntityWorld(), p.getPosition(), 3);
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
	
	protected static void cancelLavaFall(LivingHurtEvent event) {
		BlockState bs = (BlockState) event.getEntity()
				.getEntityWorld()
				.getBlockState(
						event.getEntity()
						.getPosition());

		if (bs == Blocks.LAVA.getDefaultState()) {
			if (event.getSource() == DamageSource.FALL) {
				event.setAmount(0);
				event.setCanceled(true);
			}
		}
	}

	protected static void cancelFire(LivingHurtEvent event) {
		DamageSource source = event.getSource();

		if (source == DamageSource.ON_FIRE || 
				source == DamageSource.IN_FIRE || 
				source == DamageSource.LAVA) {
			event.setAmount(0);
			event.setCanceled(true);
		}
	}

	protected static boolean isKeyDown(String keyName) {
		return InputMappings.isKeyDown(InputMappings.getInputByName(keyName).getKeyCode());
	}
	
	public static RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids, double dM)
    {
        float f = playerIn.rotationPitch;
        float f1 = playerIn.rotationYaw;
        double d0 = playerIn.posX;
        double d1 = playerIn.posY + (double)playerIn.getEyeHeight();
        double d2 = playerIn.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = playerIn.getAttribute(EntityPlayer.REACH_DISTANCE).getValue() * dM;
        Vec3d vec3d1 = vec3d.add((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
        return worldIn.rayTraceBlocks(vec3d, vec3d1);
    }
}
