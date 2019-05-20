package org.endershawn.lava.item.sword;

import org.endershawn.lava.item.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class HammerFire extends SwordBase {
	public static final int BLAST_SIZE = 4;
	private static final int BURN_TIME = 4;
	public static final double REACH_MULT = 32;
	public static final int TEMPERATURE = 10;
	
	private static IItemTier tier = ModItems.itemTierLava;
	private static Item.Properties props = new Item.Properties()
			.group(ItemGroup.COMBAT);
		
	public HammerFire() {
		super("hammer_fire", tier, props, 100, tier.getAttackDamage());
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, 
							 EntityLivingBase attacker) {
	
		if (!target.isImmuneToFire()) {
			target.setFire(BURN_TIME);
		}
		
		return super.hitEntity(stack, target, attacker);
	}
}
