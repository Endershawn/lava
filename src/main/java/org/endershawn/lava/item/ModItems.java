package org.endershawn.lava.item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.endershawn.lava.item.sword.HammerFire;
//import org.endershawn.lava.item.sword.Sithe;
//import org.endershawn.lava.item.sword.SwordLava;
//import org.endershawn.lava.item.sword.SwordThunder;
import org.endershawn.lava.LavaMod;
import org.endershawn.lava.item.sword.HammerFire;
import org.endershawn.lava.item.sword.Sithe;
import org.endershawn.lava.item.sword.SwordLava;
import org.endershawn.lava.item.sword.SwordThunder;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = LavaMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final IItemTier lavaTier = new LavaTier();
	public static final IItemTier lightningTier = new LightningTier();

//	public static final Set<Block> LAVA_EFFECTIVE_ON = Sets.newHashSet(Blocks.STONE_BRICK_STAIRS, Blocks.STONE_SLAB, Blocks.STONE_SLAB2, Blocks.SAND, Blocks.STONE, Blocks.TALLGRASS, Blocks.LEAVES2, Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SANDSTONE, Blocks.SANDSTONE_STAIRS, Blocks.RED_SANDSTONE, Blocks.RED_SANDSTONE_STAIRS, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.CONCRETE_POWDER, Blocks.GRASS, Blocks.LEAVES, Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);
//
//	public static final ToolMaterial FIRE = EnumHelper.addToolMaterial("LAVA", 3, 700, 100f, 6f, 30);
//	public static final Set<Block> FIRE_EFFECTIVE_ON = Sets.newHashSet(Blocks.TALLGRASS, Blocks.LEAVES2, Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.GRASS, Blocks.LEAVES, Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);
//	
//	
//	public static final ToolMaterial THUNDER = EnumHelper.addToolMaterial("THUNDER", 3, 3000, 100f, Float.MAX_VALUE, 30);
//	public static final Set<Block> THUNDER_EFFECTIVE_ON = Sets.newHashSet(Blocks.TALLGRASS, Blocks.LEAVES2, Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.CONCRETE_POWDER, Blocks.GRASS, Blocks.LEAVES, Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);
//
//	public static SwordLava lavaSword = new SwordLava("sword_lava");
//	public static HammerFire fireHammer = new HammerFire("hammer_fire");
//	public static SwordThunder thunderSword = 
//			(SwordThunder) new SwordThunder("sword_thunder")
//				.setRegistryName(LavaMod.MODID, "sword_thunder");
//	public static Sithe sithe = new Sithe("sithe");

	
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	LOGGER.info("Registering Items");
        event.getRegistry().registerAll(
        		new SwordThunder(),
        		new SwordLava(),
        		new HammerFire(),
        		new Sithe()
		);
    }
	
    
//	public static void register(IForgeRegistry<Item> registry) {
//		registry.registerAll(thunderSword);
//	}
//	
	
//	@Mod.EventBusSubscriber
//    public static class DamageHandler {
//    	private static boolean isFireDamage(DamageSource s) {
//    		return (s.equals(DamageSource.LAVA) 
//            	    || s.equals(DamageSource.IN_FIRE) 
//            	    || s.equals(DamageSource.ON_FIRE));
//    	}
//    	
//    	private static boolean playerHoldingLava(EntityPlayer p) {
//    		return p.getHeldItemMainhand().getItem() instanceof SwordLava;
//    	}
//    	
//    	@SubscribeEvent
//    	public static void playerBurning(LivingAttackEvent event) {
//    		Entity e = event.getEntity();
//    		DamageSource s = event.getSource();
//    		
//    		if (e instanceof EntityPlayer) {
//    			EntityPlayer player = (EntityPlayer)e;
//    			    			
//            	if (isFireDamage(s) && playerHoldingLava(player)) {
//            		player.extinguish();
//            		event.setCanceled(true);
//            	}
//    		}
//    	}
//    }
}
