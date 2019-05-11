package org.endershawn.lava.item.sword;


import org.endershawn.lava.LavaMod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SwordBase extends ItemSword {
	protected String name;
	protected IItemTier tier;
	
	public SwordBase(String name, IItemTier tier, Properties props, int maxDamage, float attackDamage) {
		super(tier, maxDamage, attackDamage, props);
		this.name = name;
		this.tier = tier;
		setRegistryName(LavaMod.MODID, name);
//		setUnlocalizedName(name);
//		setCreativeTab(CreativeTabs.COMBAT);
		
	}
	
	protected boolean canDestroy(IBlockState s) {
		return tier.getHarvestLevel() > s.getHarvestLevel();
	}
	
	protected RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids, double dM)
    {
        float f = playerIn.rotationPitch;
        float f1 = playerIn.rotationYaw;
        double d0 = playerIn.posX;
        double d1 = playerIn.posY + (double)playerIn.getEyeHeight();
        double d2 = playerIn.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = playerIn.getAttribute(EntityPlayer.REACH_DISTANCE).getValue() * dM;
        Vec3d vec3d1 = vec3d.add((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
        return worldIn.rayTraceBlocks(vec3d, vec3d1);
    }
	
//    public float getDestroySpeed(ItemStack stack, IBlockState state)
//    {        
//    	return tier.getEfficiency();
//    }
}
