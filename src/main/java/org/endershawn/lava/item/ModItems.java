package org.endershawn.lava.item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.endershawn.lava.item.sword.HammerFire;
//import org.endershawn.lava.item.sword.Sithe;
//import org.endershawn.lava.item.sword.SwordLava;
//import org.endershawn.lava.item.sword.SwordThunder;
import org.endershawn.lava.LavaMod;
import org.endershawn.lava.item.sword.HammerFire;
import org.endershawn.lava.item.sword.Sithe;
import org.endershawn.lava.item.sword.SwordLava;
import org.endershawn.lava.item.sword.SwordThunder;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = LavaMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final IItemTier lavaTier = new LavaTier();
	public static final IItemTier lightningTier = new LightningTier();
	
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	LOGGER.info("Registering Items");
        event.getRegistry().registerAll(
        		new SwordThunder(),
        		new SwordLava(),
        		new HammerFire(),
        		new Sithe()
		);
    }
}
