package org.endershawn.lava.enchantment;

import org.endershawn.lava.LavaMod;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class EnchantmentFireproof extends Enchantment {

	protected EnchantmentFireproof() {
		 super(
				 Rarity.RARE, 
				 EnchantmentType.ARMOR, 
				 new EquipmentSlotType[] { 
						 EquipmentSlotType.FEET, 
						 EquipmentSlotType.LEGS, 
						 EquipmentSlotType.CHEST, 
						 EquipmentSlotType.HEAD 
						 });
		 
		 setRegistryName(LavaMod.MODID, "fireproof");
	}

}
