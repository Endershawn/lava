package org.endershawn.lava.item.armor;

import org.endershawn.lava.LavaMod;
import org.endershawn.lava.item.ModItems;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemLavaChestplate extends ArmorItem {
	public ItemLavaChestplate() {
		super(ModItems.armorMaterialLava, EquipmentSlotType.CHEST, 
				new Item.Properties().group(ItemGroup.COMBAT));
		setRegistryName(LavaMod.MODID, "lava_chestplate");
	}

}
