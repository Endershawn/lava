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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class HammerFire extends SwordBase {
	private static final int BLAST_SIZE = 4;
	private static final int BURN_TIME = 4;
	private static final double REACH_MULT = 32;
	private static final int TEMPERATURE = 10;
	
	private static IItemTier tier = ModItems.lavaTier;
	private static Item.Properties props = new Item.Properties()
			.group(ItemGroup.COMBAT);
		
	public HammerFire() {
		super("hammer_fire", tier, props, 100, tier.getAttackDamage());
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)    {
		World worldIn = player.getEntityWorld();
		EnumFacing facing = player.getHorizontalFacing();
		
		Effects.burnBlocks(worldIn, pos, facing, BLAST_SIZE, TEMPERATURE);
		
//		List<Entity> entities = Effects.scanForEntites(worldIn, new Vec3d(pos), (float)BLAST_SIZE);
//		for (Entity ent: entities) {
//			Effects.affectEntity(ent, new IAffectEntity() {
//				@Override
//				public void affect(Entity entity) {
//					if (!entity.isImmuneToFire()) {
//						entity.setFire(BURN_TIME);
//					}
//					
//				}
//			});
//		}

		return super.onBlockStartBreak(itemstack, pos, player);
    }
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, 
							 EntityLivingBase attacker) {
//		target.getEntityWorld().createExplosion(
//				target, target.posX, target.posY, target.posZ, 
//				BLAST_SIZE, true);
		
		if (!target.isImmuneToFire()) {
			target.setFire(BURN_TIME);
		}
		
		return super.hitEntity(stack, target, attacker);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, 
			EntityPlayer player, EnumHand handIn) {
		RayTraceResult rtr = this.rayTrace(worldIn, player, true, REACH_MULT);
	    Effects.dropFireball(worldIn, rtr.getBlockPos());
		return super.onItemRightClick(worldIn, player, handIn);
	}
}
