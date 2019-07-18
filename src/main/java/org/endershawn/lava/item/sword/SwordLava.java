package org.endershawn.lava.item.sword;

import org.endershawn.lava.Effects;
import org.endershawn.lava.item.ModItems;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class SwordLava extends SwordBase {	
	public static final int BLAST_SIZE = 1;
	private static IItemTier tier = ModItems.itemTierLava;
	private static Item.Properties props = new Item.Properties()
			.group(ItemGroup.COMBAT);
	
	public SwordLava() {
		super("sword_lava", ModItems.itemTierLava, props, 0, tier.getAttackDamage());
	}
		
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		Effects.immolateEntity((LivingEntity) target);
		return super.hitEntity(stack, target, attacker);
	}
}
