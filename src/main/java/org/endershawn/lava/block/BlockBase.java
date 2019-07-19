package org.endershawn.lava.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBase extends Block {
	
	public BlockBase(String name, Material material) {
		super(Block.Properties.create(material));
	}
	
	public BlockBase(Properties props,  String name) {
		super(props);
		setRegistryName(name);
	}
	
//	public Item createItemBlock() {
//		Item.Properties p = new Item.Properties().group(ItemGroup.MISC);
//		return new ItemBlock(this, p).setRegistryName(getRegistryName());
//	}
}
