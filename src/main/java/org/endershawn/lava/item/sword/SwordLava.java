package org.endershawn.lava.item.sword;

import org.endershawn.lava.Effects;
import org.endershawn.lava.item.ModItems;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SwordLava extends SwordBase {	
	private static final int BLAST_SIZE = 1;
	private static IItemTier tier = ModItems.itemTierLava;
	private static Item.Properties props = new Item.Properties()
			.group(ItemGroup.COMBAT);
	
	public SwordLava() {
		super("sword_lava", ModItems.itemTierLava, props, 0, tier.getAttackDamage());
	}
		
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)    {
		World worldIn = player.getEntityWorld();
		
		if (BLAST_SIZE > 0 && canDestroy(worldIn.getBlockState(pos))) {
			worldIn.createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), BLAST_SIZE, true);
			Effects.spawnLava(worldIn, pos, BLAST_SIZE);
			return false;
		}
		
		return super.onBlockStartBreak(itemstack, pos, player);
    }
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		Effects.immolateEntity((EntityLiving) target);
		return super.hitEntity(stack, target, attacker);
	}
}
