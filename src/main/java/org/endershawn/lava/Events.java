package org.endershawn.lava;

import org.endershawn.lava.item.sword.HammerFire;
import org.endershawn.lava.item.sword.Sithe;
import org.endershawn.lava.item.sword.SwordLava;
import org.endershawn.lava.item.sword.SwordThunder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@Mod.EventBusSubscriber
public class Events {

	@SubscribeEvent
	public static void effectTick(PlayerTickEvent event) {
		if (event.player.world.getGameTime() % Effects.EFFECT_DURATION > 0) {
			if (Effects.wearingLava(event.player)) {
				if (event.player.isInLava()) {
					Effects.increaseSwimSpeed(event.player);
				} else {
					Effects.resetSwimSpeed(event.player);
				}
			}
		} else {
			if (Effects.wearingLava(event.player)) {
				Effects.addFireResistance(event.player);
			}
		}
	}

	@SubscribeEvent
	public static void tryLavaJump(LivingJumpEvent event) {
		Entity e = event.getEntity();
		if (e instanceof PlayerEntity) {
			if (Effects.wearingLava((PlayerEntity) e) && Effects.isKeyDown("key.keyboard.space")) {
				Effects.lavaJump((PlayerEntity) e);
			}
		}
	}

	@SubscribeEvent
	public static void livingHurt(LivingHurtEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			if (Effects.wearingLava((PlayerEntity) event.getEntity())) {
				Effects.cancelLavaFall(event);
//				cancelFire(event);
			}
		}
	}
	
	@SubscribeEvent
	public static void leftClick(LeftClickBlock event) {
		if (!(event.getEntity() instanceof PlayerEntity)) {
			return;
		}
		
		PlayerEntity player = (PlayerEntity)event.getEntity();
		Item mainItem = player.getHeldItemMainhand().getItem();
		World worldIn = event.getWorld();
		
		if (mainItem instanceof HammerFire) {
			Effects.burnTargetArea(player, event.getPos());
		} else if (mainItem instanceof SwordLava) {
			Effects.spawnLavaAtVec(worldIn, event.getPos());
		}
	}
	
	@SubscribeEvent
	public static void rightClick(RightClickItem event) {
		if (!(event.getEntity() instanceof PlayerEntity)) {
			return;
		}
		
		PlayerEntity player = (PlayerEntity)event.getEntity();
		Item mainItem = player.getHeldItemMainhand().getItem();
		
		if (mainItem instanceof SwordThunder) {
			Effects.lightningStrikeCrosshair(player);
		} else if (mainItem instanceof HammerFire) {
			Effects.fireballCrosshair(player);
		} else if (mainItem instanceof Sithe) {
			Effects.immolateCrosshair(player);
		}
	}


}