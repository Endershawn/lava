package org.endershawn.lava.item.sword;


import org.endershawn.lava.LavaMod;

import net.minecraft.block.BlockState;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class SwordBase extends SwordItem {
	protected String name;
	protected IItemTier tier;
	
	public SwordBase(String name, IItemTier tier, Properties props, int maxDamage, float attackDamage) {
		super(tier, maxDamage, attackDamage, props);
		this.name = name;
		this.tier = tier;
		setRegistryName(LavaMod.MODID, name);
		
	}
	
	public boolean canDestroy(BlockState s) {
		return tier.getHarvestLevel() > s.getHarvestLevel();
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (canDestroy(state)) {
			return 15.0F;
		} else {
			return super.getDestroySpeed(stack, state);
		}
	}
}
