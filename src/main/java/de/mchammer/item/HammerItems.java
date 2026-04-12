package de.mchammer.item;

import de.mchammer.McHammerMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class HammerItems {
	public static final Item GOLD_BLOCK_HAMMER = register(
		"gold_block_hammer",
		settings -> new HammerItem(settings.maxCount(1).maxDamage(1536).enchantable(22), 12.0f, 4.0f)
	);

	public static final Item OBSIDIAN_HAMMER = register(
		"obsidian_hammer",
		settings -> new ObsidianHammerItem(settings.maxCount(1).maxDamage(8192).enchantable(10), 10.0f, 10.0f)
	);

	public static final Item IRON_BLOCK_HAMMER = register(
		"iron_block_hammer",
		settings -> new HammerItem(settings.maxCount(1).maxDamage(4096).enchantable(14), 6.0f, 6.0f)
	);

	public static final Item COPPER_BLOCK_HAMMER = register(
		"copper_block_hammer",
		settings -> new HammerItem(settings.maxCount(1).maxDamage(3072).enchantable(12), 5.0f, 5.0f)
	);

	public static final Item DIAMOND_BLOCK_HAMMER = register(
		"diamond_block_hammer",
		settings -> new HammerItem(settings.maxCount(1).maxDamage(6144).enchantable(18), 8.0f, 8.0f)
	);

	private HammerItems() {
	}

	private static Item register(String name, Function<Item.Settings, Item> factory) {
		Identifier id = Identifier.of(McHammerMod.MOD_ID, name);
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
		Item item = factory.apply(new Item.Settings().registryKey(key));
		return Registry.register(Registries.ITEM, key, item);
	}

	public static void registerModItems() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
			.register(entries -> {
				entries.add(IRON_BLOCK_HAMMER);
				entries.add(COPPER_BLOCK_HAMMER);
				entries.add(DIAMOND_BLOCK_HAMMER);
				entries.add(GOLD_BLOCK_HAMMER);
				entries.add(OBSIDIAN_HAMMER);
			});
	}
}
