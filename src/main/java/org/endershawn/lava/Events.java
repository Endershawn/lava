package org.endershawn.lava;

import net.minecraft.block.state.BlockState;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
		if (e instanceof EntityPlayer) {
			if (Effects.wearingLava((EntityPlayer) e) && isKeyDown("key.keyboard.space")) {
				Effects.lavaJump((EntityPlayer) e);
			}
		}
	}

	@SubscribeEvent
	public static void livingHurt(LivingHurtEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			if (Effects.wearingLava((EntityPlayer) event.getEntity())) {
				cancelLavaFall(event);
//				cancelFire(event);
				event.setCanceled(true);
			}
		}
	}

	private static void cancelLavaFall(LivingHurtEvent event) {
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

	private static void cancelFire(LivingHurtEvent event) {
		DamageSource source = event.getSource();

		if (source == DamageSource.ON_FIRE || 
				source == DamageSource.IN_FIRE || 
				source == DamageSource.LAVA) {
			event.setAmount(0);
			event.setCanceled(true);
		}
	}

	private static boolean isKeyDown(String keyName) {
		return InputMappings.isKeyDown(InputMappings.getInputByName(keyName).getKeyCode());
	}
}