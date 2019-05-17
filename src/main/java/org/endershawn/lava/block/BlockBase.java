package org.endershawn.lava.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;

public class BlockBase extends Block {
	
	public BlockBase(String name, Material material) {
		super(Block.Properties.create(material));
	}
	
	public BlockBase(Properties props,  String name) {
		super(props);
		setRegistryName(name);
	}
	
	public Item createItemBlock() {
		Item.Properties p = new Item.Properties().group(ItemGroup.MISC);
		return new ItemBlock(this, p).setRegistryName(getRegistryName());
	}
}
