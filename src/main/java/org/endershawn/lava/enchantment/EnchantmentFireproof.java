package org.endershawn.lava.enchantment;

import org.endershawn.lava.LavaMod;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentFireproof extends Enchantment {

	protected EnchantmentFireproof() {
		 super(
				 Rarity.RARE, 
				 EnumEnchantmentType.ARMOR, 
				 new EntityEquipmentSlot[] { 
						 EntityEquipmentSlot.FEET, 
						 EntityEquipmentSlot.LEGS, 
						 EntityEquipmentSlot.CHEST, 
						 EntityEquipmentSlot.HEAD 
						 });
		 
		 setRegistryName(LavaMod.MODID, "fireproof");
	}

}
