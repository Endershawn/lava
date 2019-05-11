package org.endershawn.lava.block;

import org.endershawn.lava.LavaMod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;

public class BlockBase extends Block {

	protected String name;
	protected Properties props = null;
	
	public BlockBase(Properties props,  String name) {
		super(props);
		this.props = props;
		this.name = name;
	
		//setUnlocalizedName(name);
		setRegistryName(name);
	}
	
	public void registerItemModel(Item itemBlock) {
		//LavaMod.proxy.registerItemRenderer(itemBlock, 0, name);
	}
	
	public Item createItemBlock() {
		Item.Properties p = new Item.Properties().group(ItemGroup.MISC);
		return new ItemBlock(this, p).setRegistryName(getRegistryName());
	}
	
//	@Override
//	public BlockBase setCreativeTab(CreativeTabs tab) {
//		super.setCreativeTab(tab);
//		return this;
//	}

}
