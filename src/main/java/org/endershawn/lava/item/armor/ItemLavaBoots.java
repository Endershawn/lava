package org.endershawn.lava.item.armor;

import org.endershawn.lava.LavaMod;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemGroup;

public class ItemLavaBoots extends ItemArmor {
	public ItemLavaBoots() {
		super(new ArmorMaterialLava(), EntityEquipmentSlot.FEET, 
				new Item.Properties().group(ItemGroup.COMBAT));
		setRegistryName(LavaMod.MODID, "lava_boots");
	}
}
