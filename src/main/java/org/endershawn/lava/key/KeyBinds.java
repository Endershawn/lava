package org.endershawn.lava.key;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBinds {
	    public static KeyBinding jump;

	    public static void register()
	    {
	        jump = new KeyBinding("Jump", 149, "Lava");

	        ClientRegistry.registerKeyBinding(jump);
	    }
	}
