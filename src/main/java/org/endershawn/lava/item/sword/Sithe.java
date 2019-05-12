package org.endershawn.lava.item.sword;

import org.endershawn.lava.Effects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class Sithe extends SwordBase {
	private static final double REACH_MULT = 20;
	private static Item.Properties props = new Item.Properties()
			.group(ItemGroup.COMBAT);

	public Sithe() {
		super("sithe_death", ItemTier.IRON, props, 100, ItemTier.IRON.getAttackDamage());
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, 
			EntityPlayer player, EnumHand handIn) {
		RayTraceResult rtr = this.rayTrace(worldIn, player, true, REACH_MULT);
		Effects.immolateRange(worldIn, rtr.hitVec, 1f);
		return super.onItemRightClick(worldIn, player, handIn);
	}
}
