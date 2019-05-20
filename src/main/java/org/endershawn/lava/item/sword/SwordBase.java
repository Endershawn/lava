package org.endershawn.lava.item.sword;


import org.endershawn.lava.LavaMod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class SwordBase extends ItemSword {
	protected String name;
	protected IItemTier tier;
	
	public SwordBase(String name, IItemTier tier, Properties props, int maxDamage, float attackDamage) {
		super(tier, maxDamage, attackDamage, props);
		this.name = name;
		this.tier = tier;
		setRegistryName(LavaMod.MODID, name);
		
	}
	
	public boolean canDestroy(IBlockState s) {
		return tier.getHarvestLevel() > s.getHarvestLevel();
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		if (canDestroy(state)) {
			return 15.0F;
		} else {
			return super.getDestroySpeed(stack, state);
		}
	}
}
