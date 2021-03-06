package org.endershawn.lava;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LavaMod.MODID)
public class LavaMod
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "lava";

    public LavaMod() {
        // Register the setup method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
//        // Register the enqueueIMC method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
//        // Register the processIMC method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
//        // Register the doClientStuff method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

//    private void setup(final FMLCommonSetupEvent event)
//    {
//        // some preinit code
//        LOGGER.info("HELLO FROM PREINIT");
//    }
//    
//    private void doClientStuff(final FMLCommonSetupEvent event) {
//    }
    
    // You can use SubscribeEvent and let the Event Bus discover methods to call
//	@SubscribeEvent
//	public void onServerStarting(FMLServerStartingEvent event) {
//		// do something when the server starts
//		LOGGER.info("HELLO from server starting");
//	}

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
//    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
//    public static class RegistryEvents {
//		@SubscribeEvent
//		public static void register(final RegistryEvent.Register<Item> event)
//		{
//			ModItems.registerItems(event);
//			LOGGER.info("Items registered.");
//		}
		
//        @SubscribeEvent
//        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
//            // register a new block here
//            LOGGER.info("HELLO from Register Block");
//        }
//    }
}
