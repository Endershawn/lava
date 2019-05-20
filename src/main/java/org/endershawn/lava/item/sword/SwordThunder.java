package org.endershawn.lava.item.sword;

import org.endershawn.lava.item.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SwordThunder extends SwordBase {
	public static final double REACH_MULT = 6;
	public static final float BLAST_SIZE = 6;
	
	private static IItemTier tier = ModItems.itemTierLightning;
	private static Item.Properties props = new Item.Properties()
			.group(ItemGroup.COMBAT);
	
	public SwordThunder() {
		super("sword_thunder", tier, props, 0, tier.getAttackDamage());
	}

	/** TODO: Move this to an event **/
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		World worldIn = attacker.getEntityWorld();

		if (worldIn.isRemote) {
			super.hitEntity(stack, target, attacker);
			target.setHealth(0);
		} else {
			worldIn.spawnEntity(
					new EntityLightningBolt(worldIn, target.posX, 
							                target.posY, target.posZ, true));
		}
		return true;
	}
}
