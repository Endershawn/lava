package org.endershawn.lava.item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.endershawn.lava.item.sword.HammerFire;
//import org.endershawn.lava.item.sword.Sithe;
//import org.endershawn.lava.item.sword.SwordLava;
//import org.endershawn.lava.item.sword.SwordThunder;
import org.endershawn.lava.LavaMod;
import org.endershawn.lava.item.armor.ArmorMaterialLava;
import org.endershawn.lava.item.armor.ItemLavaBoots;
import org.endershawn.lava.item.armor.ItemLavaChestplate;
import org.endershawn.lava.item.armor.ItemLavaHelmet;
import org.endershawn.lava.item.armor.ItemLavaLeggings;
import org.endershawn.lava.item.sword.HammerFire;
import org.endershawn.lava.item.sword.Sithe;
import org.endershawn.lava.item.sword.SwordLava;
import org.endershawn.lava.item.sword.SwordThunder;

import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = LavaMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final IItemTier itemTierLava = new LavaTier();
	public static final IItemTier itemTierLightning = new LightningTier();
	public static final IArmorMaterial armorMaterialLava = new ArmorMaterialLava();
	
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	LOGGER.info("Registering Items");
        event.getRegistry().registerAll(
        		new SwordThunder(),
        		new SwordLava(),
        		new HammerFire(),
        		new Sithe(),
        		new ItemLavaChestplate(),
        		new ItemLavaHelmet(),
        		new ItemLavaBoots(),
        		new ItemLavaLeggings()
		);
    }
}
