package org.endershawn.lava.item.sword;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;

public class Sithe extends SwordBase {
	public static final double REACH_MULT = 20;
	private static Item.Properties props = new Item.Properties()
			.group(ItemGroup.COMBAT);

	public Sithe() {
		super("sithe_death", ItemTier.IRON, props, 100, ItemTier.IRON.getAttackDamage());
	}
}
