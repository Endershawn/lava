package org.endershawn.lava.item.sword;

import org.endershawn.lava.Effects;
import org.endershawn.lava.item.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SwordThunder extends SwordBase {
	private static final double REACH_MULT = 6;
	private static final float BLAST_SIZE = 6;
	
	private static IItemTier tier = ModItems.lightningTier;
	private static Item.Properties props = new Item.Properties()
			.group(ItemGroup.COMBAT);
	
	public SwordThunder() {
		super("sword_thunder", tier, props, 0, tier.getAttackDamage());
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		super.hitEntity(stack, target, attacker);
		target.setHealth(0);
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
		RayTraceResult rtr = this.rayTrace(worldIn, player, true, REACH_MULT);

		if (rtr != null) {
			if (rtr.type != RayTraceResult.Type.MISS) {
				Effects.lightningStrike(worldIn, player, rtr.getBlockPos(), BLAST_SIZE);
			}
		}
		return super.onItemRightClick(worldIn, player, handIn);
	}
}
