package org.endershawn.lava.item.armor;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ArmorMaterialLava implements IArmorMaterial {
	private String name;
	private int maxDamageFactor;
	private int[] damageReductionAmountArray;
	private int enchantability;
	private SoundEvent soundEvent;
	private float toughness;
	private LazyLoadBase<Ingredient> repairMaterial;
	private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };

	public ArmorMaterialLava() {
		this.name = "lava:lava";
		this.maxDamageFactor = 33;
		this.damageReductionAmountArray = new int[] { 3, 6, 8, 3 };
		this.enchantability = 10;
		this.soundEvent = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
		this.toughness = 2;
		this.repairMaterial = new LazyLoadBase<>(() -> {
			return Ingredient.fromItems(Items.LAVA_BUCKET);
		});
	}

	public int getDurability(EntityEquipmentSlot slotIn) {
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
	}

	public int getDamageReductionAmount(EntityEquipmentSlot slotIn) {
		return this.damageReductionAmountArray[slotIn.getIndex()];
	}

	public int getEnchantability() {
		return this.enchantability;
	}

	public SoundEvent getSoundEvent() {
		return this.soundEvent;
	}

	public Ingredient getRepairMaterial() {
		return (Ingredient) this.repairMaterial.getValue();
	}

	@OnlyIn(Dist.CLIENT)
	public String getName() {
		return this.name;
	}

	public float getToughness() {
		return this.toughness;
	}

}