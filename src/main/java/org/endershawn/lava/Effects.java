package org.endershawn.lava;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.endershawn.lava.entity.EntitySuperFireball;
import org.endershawn.lava.item.ModItems;
import org.endershawn.lava.item.sword.HammerFire;
import org.endershawn.lava.item.sword.Sithe;
import org.endershawn.lava.item.sword.SwordLava;
import org.endershawn.lava.item.sword.SwordThunder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.TieredItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

// TODO: refactor lightning

public class Effects {
	private static int TICKS = 20;
	private static final float JUMP_LEVEL = 2.5f;
	private static final float FIREBALL_ACC = -0.001f;
	protected static final int EFFECT_DURATION = 5 * TICKS;
	protected static final AttributeModifier INC_SWIM_SPEED = 
			new AttributeModifier("lava.inc_swimspeed", 6, null);

	public static interface IAffectEntity {
		public void affect(Entity e);
	}

	public static void affectEntity(Entity e, IAffectEntity a) {
		a.affect(e);
	}

	public static void immolateEntity(Entity target) {
		if (!target.isImmuneToFire()) {
			target.setFire(10);

			if (target instanceof LivingEntity) {
				((LivingEntity) target).setHealth(.1f);
			}
		}
	}

	public static void burnBlocks(World worldIn, BlockPos pos, Direction direction, int range, int temp) {
		if (worldIn.isRemote) {return;}
		
		BlockPos toPos = new BlockPos(pos);
		BlockState fireState = Blocks.FIRE.getDefaultState();
		Random rand = new Random();

		if (direction.getAxis() == Axis.X) {
			toPos = toPos.add(0, range, range);
			pos = pos.add(0, 0, -(range));
		} else {
			toPos = toPos.add(range, range, 0);
			pos = pos.add(-(range), 0, 0);
		}

		Stream<BlockPos> blocksBox = BlockPos.getAllInBox(pos, toPos.offset(direction, range * 2));

		blocksBox.forEach(p -> {
			Block b = worldIn.getBlockState(p).getBlock();

			if (rand.nextInt(100) < temp && b != Blocks.AIR) {
				worldIn.setBlockState(p, fireState);
			}
		});
	}

	public static void immolateRange(World worldIn, Vec3d v, float range) {
		List<Entity> entities = Effects.scanForEntites(worldIn, v, range);

		for (Entity ent : entities) {
			Effects.immolateEntity(ent);
		}
	}

	public static void spawnClientSide(Entity e) {
		Optional<ClientWorld> ocw = LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT);
		
		if (ocw.isPresent()) {			
			ClientWorld cw = ocw.get();
			
			if (e instanceof LightningBoltEntity) {
				cw.addLightning((LightningBoltEntity)e);
			} else {
				cw.addEntity(e.getEntityId(), e);
			}
		}
	}
	
	public static void dropFireball(World worldIn, BlockPos p) {
		if (!worldIn.isRemote) {			
			spawnClientSide(new EntitySuperFireball(
					worldIn, 
					p.getX(), 
					worldIn.getActualHeight() / 2, 
					p.getZ(), 
					0, 
					FIREBALL_ACC, 
					0
			));
		} else {

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
					true,
					Explosion.Mode.DESTROY);
			Effects.spawnLava(worldIn, pos, SwordLava.BLAST_SIZE);
		}
	}
	
	public static void spawnLavaAtVec(World worldIn, BlockPos pos) {
		if (worldIn.isRemote) {return;}
		
		if (SwordLava.BLAST_SIZE > 0 && ModItems.swordLava.canDestroy(worldIn.getBlockState(pos))) {
			worldIn.createExplosion(
					null, 
					pos.getX(), 
					pos.getY(), 
					pos.getZ(), 
					SwordLava.BLAST_SIZE, 
					true,
					Explosion.Mode.DESTROY);
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
		AxisAlignedBB scanBB = new AxisAlignedBB(
				vec.add(-range, -range, -range), 
				vec.add(range, range, range));

		List<Entity> entities = worldIn.getEntitiesWithinAABB(Entity.class, scanBB);
		return entities;
	}

	public static void lightningStrike(World worldIn, PlayerEntity player, BlockPos hitPos, Float size) {
		worldIn.playSound(
				player, 
				hitPos, 
				SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, 
				SoundCategory.WEATHER, 
				10f, 
				1f);
		
		worldIn.playSound(
				player, 
				hitPos, 
				SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 
				SoundCategory.WEATHER, 
				10f, 
				1f);
		
		if (!worldIn.isRemote) {
			worldIn.createExplosion(
					player, 
					null,
					hitPos.getX(), 
					hitPos.getY(), 
					hitPos.getZ(), 
					size, 
					true, 
					Explosion.Mode.DESTROY);
		} else {
			spawnClientSide(new LightningBoltEntity(
					worldIn, 
					hitPos.getX(), 
					hitPos.getY(), 
					hitPos.getZ(), 
					true));
			
			for (Entity e : Effects.scanForEntites(worldIn, new Vec3d(hitPos), size)) {
				e.attackEntityFrom(DamageSource.LIGHTNING_BOLT, size * 2);
			}
		}
	}
	
	public static void lightningStrike(World worldIn, PlayerEntity player, Vec3d hitVec, Float size) {
		lightningStrike(worldIn, player, new BlockPos(hitVec), size);
	}

	public static void lightningStrikeCrosshair(PlayerEntity player) {
		World world = player.getEntityWorld();
		
		RayTraceResult rtr = Effects.rayTrace(
				world, 
				player, 
				true, 
				SwordThunder.REACH_MULT);
		
		if (rtr != null) {
			if (rtr.getType() != RayTraceResult.Type.MISS) {
					Effects.lightningStrike(
							world, 
							player, 
							rtr.getHitVec(), 
							SwordThunder.BLAST_SIZE);
					}
		}
	}
	
	public static void fireballCrosshair(PlayerEntity player) {
		World worldIn = player.getEntityWorld();
		RayTraceResult rtr = Effects.rayTrace(worldIn, player, true, HammerFire.REACH_MULT);
		
		if (rtr != null && rtr.getType() != RayTraceResult.Type.MISS) {
			    Effects.dropFireball(worldIn, new BlockPos(rtr.getHitVec()));
		}
	}
	
	public static void immolateCrosshair(PlayerEntity player) {
		World worldIn = player.getEntityWorld();
		
		RayTraceResult rtr = Effects.rayTrace(worldIn, player, true, Sithe.REACH_MULT);
		Effects.immolateRange(worldIn, rtr.getHitVec(), 1f);
	}
	
	public static void burnTargetArea(PlayerEntity player, Vec3d area) {
		World worldIn = player.getEntityWorld();
		Direction facing = player.getHorizontalFacing();
		BlockPos target = new BlockPos(area);
		
		Effects.burnBlocks(worldIn, target, facing, HammerFire.BLAST_SIZE, HammerFire.TEMPERATURE);
	}
	
	public static void burnTargetArea(PlayerEntity player, BlockPos pos) {
		World worldIn = player.getEntityWorld();
		Direction facing = player.getHorizontalFacing();
		BlockPos target = pos;
		
		Effects.burnBlocks(worldIn, target, facing, HammerFire.BLAST_SIZE, HammerFire.TEMPERATURE);
	}
	
//	private static boolean isLava(Item i) {
//		if (i instanceof ItemTiered) {
//			ItemTiered it = (ItemTiered) i;
//			return (it.getTier() instanceof LavaTier);
//		}
//
//		if (i instanceof ItemArmor) {
//			ItemArmor ia = (ItemArmor) i;
//			return (ia.getArmorMaterial() instanceof ArmorMaterialLava);
//		}
//
//		return false;
//	}

	private static boolean isTier(Item i, IItemTier tier) {
		if (i instanceof TieredItem) {
			TieredItem it = (TieredItem)i;
			return (it.getTier().getClass().equals(tier.getClass()));
		}

		return false;
	}

	static boolean isHoldingTier(PlayerEntity p, IItemTier tier) {
		Item main = p.getHeldItemMainhand().getItem();
		Item off = p.getHeldItemOffhand().getItem();

		return (isTier(main, tier) || isTier(off, tier));
	}

	static boolean wearingLava(PlayerEntity p) {
		return isWearing(p, ModItems.armorMaterialLava);
	}

	public static boolean isWearing(PlayerEntity p, IArmorMaterial m) {
		for (EquipmentSlotType slot : EquipmentSlotType.values()) {
			if (slot.getSlotType() == EquipmentSlotType.Group.ARMOR) {
				Item i = p.getItemStackFromSlot(slot).getItem();
				if (!(i instanceof ArmorItem)) {return false;}
				if (!((ArmorItem)i).getArmorMaterial().getClass().equals(m.getClass())) {
					return false;
				}
			}
		}
		return true;
	}

	private static void addEffect(EffectInstance e, PlayerEntity p) {
		addEffect(e, p, 100);
	}

	private static void addEffect(EffectInstance e, PlayerEntity p, int level) {
		p.addPotionEffect(new EffectInstance(e.getPotion(), EFFECT_DURATION + TICKS, level));
	}

	static void addFireResistance(PlayerEntity p) {
		addEffect(new EffectInstance(net.minecraft.potion.Effects.FIRE_RESISTANCE), p);
		p.extinguish();
	}
	protected static void lavaJump(PlayerEntity p) {
		lavaJump(p, JUMP_LEVEL);
	}

	protected static void lavaJump(PlayerEntity p, float level) {		
		p.addVelocity(0, level, 0);
		Effects.spawnLava(p.getEntityWorld(), p.getPosition(), 3);
	}

	protected static void activateModifier(PlayerEntity p, IAttribute a, AttributeModifier m) {
		IAttributeInstance i = p.getAttribute(a);

		if (!i.hasModifier(m)) {
			i.applyModifier(m);
		}
	}

	protected static void deactivateModifier(PlayerEntity p, IAttribute a, AttributeModifier m) {
		IAttributeInstance i = p.getAttribute(a);
		
		for (AttributeModifier mod : i.getModifiers()) {
			if (mod == m) {
				i.removeModifier(mod);
			}
		}
	}

	protected static void increaseSwimSpeed(PlayerEntity p) {
		Effects.activateModifier(p, LivingEntity.SWIM_SPEED, Effects.INC_SWIM_SPEED);
	}

	protected static void resetSwimSpeed(PlayerEntity p) {
		Effects.deactivateModifier(p, LivingEntity.SWIM_SPEED, Effects.INC_SWIM_SPEED);
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
		return InputMappings.isKeyDown(InputMappings.getInputByName(keyName).getKeyCode(), 0);
	}
	
	public static RayTraceResult rayTrace(World worldIn, PlayerEntity playerIn, boolean useLiquids, double dM)
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
        double d3 = playerIn.getAttribute(PlayerEntity.REACH_DISTANCE).getValue() * dM;
        Vec3d vec3d1 = vec3d.add((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
                
        RayTraceContext rtc = new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.ANY, playerIn);
        return worldIn.rayTraceBlocks(rtc);
    }
}
