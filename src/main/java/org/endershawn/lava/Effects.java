package org.endershawn.lava;

import java.util.List;
import java.util.Random;

import org.endershawn.lava.item.LavaTier;
import org.endershawn.lava.item.armor.ArmorMaterialLava;

import net.minecraft.block.Block;

import org.endershawn.lava.entity.EntitySuperFireball;

import net.minecraft.client.util.InputMappings;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Effects {
	private static int TICKS = 20;
	private static final int EFFECT_DURATION = 2 * TICKS;
	private static final int JUMP_LEVEL = 15;

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
				((EntityLiving)target).setHealth(.1f);
			}
		}
	}
	
	public static void burnBlocks(
			World worldIn, BlockPos pos, 
			EnumFacing direction, int range, int temp) {
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
		
		Iterable<BlockPos> blocksBox = BlockPos.getAllInBox(
				pos, toPos.offset(direction, range * 2));
		
		for (BlockPos p: blocksBox) {
			Block b = worldIn.getBlockState(p).getBlock();
			
			if (rand.nextInt(100) < temp && b != Blocks.AIR) {
				worldIn.setBlockState(p, fireState);
			}
		}
	}
	
	public static void immolateRange(World worldIn, Vec3d v, float range) {
	    List<Entity> entities = Effects.scanForEntites(worldIn, v, range);
		
		for (Entity ent: entities) {
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
	
	private static boolean isLava(net.minecraft.item.Item i) {
		if (i instanceof ItemTiered) {
			ItemTiered it = (ItemTiered)i;
			return (it.getTier() instanceof LavaTier);
		}
		
		if (i instanceof ItemArmor) {
			ItemArmor ia = (ItemArmor)i;
			return (ia.getArmorMaterial() instanceof ArmorMaterialLava);
		}
		
		return false;
	}
	
	/** abstract to isHolding(tier) **/
	private static boolean holdingLava(EntityPlayer p) {
		return isLava(p.getHeldItemMainhand().getItem()) || isLava(p.getHeldItemOffhand().getItem());
	}
	
	/** abstract to isWearing(armorMaterial) **/
	private static boolean wearingLava(EntityPlayer p) {
		for (EntityEquipmentSlot slot: EntityEquipmentSlot.values() ) {
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
		p.addPotionEffect(new PotionEffect(
				potion, EFFECT_DURATION + TICKS, level));
	}
	
	private static void addFireResistance(EntityPlayer p) {
		addEffect(MobEffects.FIRE_RESISTANCE, p);
		p.extinguish();

	}
	
	private static void addJumpBoost(EntityPlayer p, int level) {
		addEffect(MobEffects.JUMP_BOOST, p, level);
	}
	
	@Mod.EventBusSubscriber
    public static class EffectHandler {
		
		@SubscribeEvent
		public static void playerTick(PlayerTickEvent event) {    	
			if (event.player.world.getGameTime() % EFFECT_DURATION > 0) {
				return;
			}
						
			if (wearingLava(event.player)) {
				addFireResistance(event.player);
				addJumpBoost(event.player, JUMP_LEVEL);
			}
			
//    		if (isLava(heldItem)) {
//    			addFireResistance(event.player);
//    		}
    		
		}
		
		@SubscribeEvent
		public static void equipChange(LivingEquipmentChangeEvent event) {

			if (event.getEntity() instanceof EntityPlayer) {	
				EntityPlayer p = (EntityPlayer)event.getEntity();
				
//				if (holdingLava(p)) {
//					addFireResistance(p);
//				}
				
				if (wearingLava(p)) {
					addFireResistance(p);
				}
			}
		}
		
		@SubscribeEvent
		public static void jumpEvent(LivingJumpEvent event) {
			Entity e = event.getEntity();
			if (e instanceof EntityPlayer) {
				if (InputMappings.isKeyDown(InputMappings.getInputByName("key.keyboard.space").getKeyCode())) {
					/** maybe at this point just shoot the player up **/
					spawnLava(event.getEntity().getEntityWorld(), event.getEntity().getPosition(), 2);
				}
			}
		}
    }

}
