package org.endershawn.lava.enchantment;

import org.endershawn.lava.LavaMod;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = LavaMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEnchantments {
    @SubscribeEvent
    public static void onEvent(final RegistryEvent.Register<Enchantment> event)
    {
        final IForgeRegistry<Enchantment> registry = event.getRegistry();
                    
        registry.register(new EnchantmentFireproof());            
}
}
