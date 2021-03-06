package org.endershawn.lava.item.armor;

import org.endershawn.lava.LavaMod;
import org.endershawn.lava.item.ModItems;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemGroup;

public class ItemLavaLeggings extends ItemArmor {
	public ItemLavaLeggings() {
		super(ModItems.armorMaterialLava, EntityEquipmentSlot.LEGS, 
				new Item.Properties().group(ItemGroup.COMBAT));
		setRegistryName(LavaMod.MODID, "lava_leggings");
	}
}
