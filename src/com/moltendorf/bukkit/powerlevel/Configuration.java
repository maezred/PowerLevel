package com.moltendorf.bukkit.powerlevel;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.*;

/**
 * Configuration class.
 * Created by moltendorf on 14/09/03.
 *
 * @author moltendorf
 */
public class Configuration {

	static protected class Global {

		// Final data.
		final protected boolean enabled = true; // Whether or not the plugin is enabled at all; interface mode.

		final protected int repairExperience = 8765; // Level 70+.

		final protected Set<Material> armorEquipment = new HashSet<>(Arrays.asList(
			Material.DIAMOND_HELMET,
			Material.DIAMOND_CHESTPLATE,
			Material.DIAMOND_LEGGINGS,
			Material.DIAMOND_BOOTS,

			Material.IRON_HELMET,
			Material.IRON_CHESTPLATE,
			Material.IRON_LEGGINGS,
			Material.IRON_BOOTS,

			Material.CHAINMAIL_HELMET,
			Material.CHAINMAIL_CHESTPLATE,
			Material.CHAINMAIL_LEGGINGS,
			Material.CHAINMAIL_BOOTS,

			Material.GOLD_HELMET,
			Material.GOLD_CHESTPLATE,
			Material.GOLD_LEGGINGS,
			Material.GOLD_BOOTS,

			Material.LEATHER_HELMET,
			Material.LEATHER_CHESTPLATE,
			Material.LEATHER_LEGGINGS,
			Material.LEATHER_BOOTS
		));

		final protected Set<Material> blockEquipment = new HashSet<>(Arrays.asList(
			Material.DIAMOND_AXE,
			Material.DIAMOND_PICKAXE,
			Material.DIAMOND_SPADE,
			Material.DIAMOND_SWORD,

			Material.IRON_AXE,
			Material.IRON_PICKAXE,
			Material.IRON_SPADE,
			Material.IRON_SWORD,

			Material.STONE_AXE,
			Material.STONE_PICKAXE,
			Material.STONE_SPADE,
			Material.STONE_SWORD,

			Material.WOOD_AXE,
			Material.WOOD_PICKAXE,
			Material.WOOD_SPADE,
			Material.WOOD_SWORD,

			Material.GOLD_AXE,
			Material.GOLD_PICKAXE,
			Material.GOLD_SPADE,
			Material.GOLD_SWORD,

			Material.SHEARS
		));

		final protected Set<Material> bowEquipment = new HashSet<>(Arrays.asList(
			Material.BOW
		));

		final protected Set<Material> farmEquipment = new HashSet<>(Arrays.asList(
			Material.DIAMOND_HOE,
			Material.IRON_HOE,
			Material.STONE_HOE,
			Material.WOOD_HOE,
			Material.GOLD_HOE
		));

		final protected Set<Material> farmBlocks = new HashSet<>(Arrays.asList(
			Material.DIRT,
			Material.GRASS
		));

		final protected Set<Material> fireEquipment = new HashSet<>(Arrays.asList(
			Material.FLINT_AND_STEEL
		));

		final protected Set<Material> fishingEquipment = new HashSet<>(Arrays.asList(
			Material.FISHING_ROD
		));

		final protected Set<Material> shearEquipment = new HashSet<>(Arrays.asList(
			Material.SHEARS
		));

		final protected Set<Material> weaponEquipment = new HashSet<>(Arrays.asList(
			Material.DIAMOND_AXE,
			Material.DIAMOND_PICKAXE,
			Material.DIAMOND_SPADE,
			Material.DIAMOND_SWORD,

			Material.IRON_AXE,
			Material.IRON_PICKAXE,
			Material.IRON_SPADE,
			Material.IRON_SWORD,

			Material.STONE_AXE,
			Material.STONE_PICKAXE,
			Material.STONE_SPADE,
			Material.STONE_SWORD,

			Material.WOOD_AXE,
			Material.WOOD_PICKAXE,
			Material.WOOD_SPADE,
			Material.WOOD_SWORD,

			Material.GOLD_AXE,
			Material.GOLD_PICKAXE,
			Material.GOLD_SPADE,
			Material.GOLD_SWORD
		));

		final protected Map<Integer, Integer> enchantmentCountValues = new HashMap<Integer, Integer>() {{
			put(1, 1);
			put(2, 3);
			put(3, 6);
			put(4, 10);
			put(5, 15);
		}};

		final protected Map<Enchantment, Integer> enchantmentBaseValues = new HashMap<Enchantment, Integer>() {{

			// Sword enchantments.

			put(Enchantment.DAMAGE_ALL, 1);
			put(Enchantment.DAMAGE_UNDEAD, 2);
			put(Enchantment.DAMAGE_ARTHROPODS, 2);
			put(Enchantment.KNOCKBACK, 2);
			put(Enchantment.FIRE_ASPECT, 4);
			put(Enchantment.LOOT_BONUS_MOBS, 4);

			// Tool enchantments.

			put(Enchantment.DIG_SPEED, 1);
			put(Enchantment.DURABILITY, 2);
			put(Enchantment.LOOT_BONUS_BLOCKS, 4);
			put(Enchantment.SILK_TOUCH, 8);

			// Armor enchantments.

			put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			put(Enchantment.PROTECTION_FIRE, 2);
			put(Enchantment.PROTECTION_PROJECTILE, 2);
			put(Enchantment.PROTECTION_FALL, 2);
			put(Enchantment.PROTECTION_EXPLOSIONS, 4);
			put(Enchantment.OXYGEN, 4);
			put(Enchantment.WATER_WORKER, 4);
			put(Enchantment.THORNS, 8);

			// Bow enchantments.

			put(Enchantment.ARROW_DAMAGE, 1);
			put(Enchantment.ARROW_KNOCKBACK, 4);
			put(Enchantment.ARROW_FIRE, 4);
			put(Enchantment.ARROW_INFINITE, 8);
		}};

		final protected Map<Material, Integer> equipmentBaseValues = new HashMap<Material, Integer>() {{
			// Diamond tools.

			put(Material.DIAMOND_AXE, 12);
			put(Material.DIAMOND_PICKAXE, 12);
			put(Material.DIAMOND_SPADE, 12);
			put(Material.DIAMOND_SWORD, 12);
			put(Material.DIAMOND_HOE, 12);

			// Iron tools.

			put(Material.IRON_AXE, 4);
			put(Material.IRON_PICKAXE, 4);
			put(Material.IRON_SPADE, 4);
			put(Material.IRON_SWORD, 4);
			put(Material.IRON_HOE, 4);

			// Stone tools.

			put(Material.STONE_AXE, 4);
			put(Material.STONE_PICKAXE, 4);
			put(Material.STONE_SPADE, 4);
			put(Material.STONE_SWORD, 4);
			put(Material.STONE_HOE, 4);

			// Wood tools.

			put(Material.WOOD_AXE, 4);
			put(Material.WOOD_PICKAXE, 4);
			put(Material.WOOD_SPADE, 4);
			put(Material.WOOD_SWORD, 4);
			put(Material.WOOD_HOE, 4);

			// Gold tools.

			put(Material.GOLD_AXE, 4);
			put(Material.GOLD_PICKAXE, 4);
			put(Material.GOLD_SPADE, 4);
			put(Material.GOLD_SWORD, 4);
			put(Material.GOLD_HOE, 4);

			// Diamond armor.

			put(Material.DIAMOND_HELMET, 12);
			put(Material.DIAMOND_CHESTPLATE, 12);
			put(Material.DIAMOND_LEGGINGS, 12);
			put(Material.DIAMOND_BOOTS, 12);

			// Iron armor.

			put(Material.IRON_HELMET, 4);
			put(Material.IRON_CHESTPLATE, 4);
			put(Material.IRON_LEGGINGS, 4);
			put(Material.IRON_BOOTS, 4);

			// Chainmail armor.

			put(Material.CHAINMAIL_HELMET, 4);
			put(Material.CHAINMAIL_CHESTPLATE, 4);
			put(Material.CHAINMAIL_LEGGINGS, 4);
			put(Material.CHAINMAIL_BOOTS, 4);

			// Gold armor.

			put(Material.GOLD_HELMET, 4);
			put(Material.GOLD_CHESTPLATE, 4);
			put(Material.GOLD_LEGGINGS, 4);
			put(Material.GOLD_BOOTS, 4);

			// Leather armor.

			put(Material.LEATHER_HELMET, 4);
			put(Material.LEATHER_CHESTPLATE, 4);
			put(Material.LEATHER_LEGGINGS, 4);
			put(Material.LEATHER_BOOTS, 4);

			// Special.

			put(Material.BOW, 4);
			put(Material.SHEARS, 4);
			put(Material.FISHING_ROD, 4);
			put(Material.FLINT_AND_STEEL, 4);
		}};

		final protected Map<Material, Double> equipmentValueMultipliers = new HashMap<Material, Double>() {{
			final double b = .8; // Base cost modifier.
			final double d = .1875; // Additional discount.

			final double dtd = 1562; // Diamond tool durability: 1562.
			final double itd = 251; // Iron tool durability: 251.
			final double std = 132; // Stone tool durability: 132.
			final double wtd = 60; // Wood tool durability: 60.
			final double gtd = 33; // Gold tool durability: 33.

			final double dhd = 364; // Diamond helmet durability: 364.
			final double dcd = 529; // Diamond chestplate durability: 529.
			final double dld = 496; // Diamond leggings durability: 496.
			final double dbd = 430; // Diamond boots durability: 430.
			final double ihd = 166; // Iron helmet durability: 166.
			final double icd = 241; // Iron chestplate durability: 241.
			final double ild = 226; // Iron leggings durability: 226.
			final double ibd = 196; // Iron boots durability: 196.
			final double chd = 166; // Chainmail helmet durability: 166.
			final double ccd = 241; // Chainmail chestplate durability: 241.
			final double cld = 226; // Chainmail leggings durability: 226.
			final double cbd = 196; // Chainmail boots durability: 196.
			final double ghd = 78; // Gold helmet durability: 78.
			final double gcd = 113; // Gold chestplate durability: 113.
			final double gld = 106; // Gold leggings durability: 106.
			final double gbd = 92; // Gold boots durability: 92.
			final double lhd = 56; // Leather helmet durability: 56.
			final double lcd = 81; // Leather chestplate durability: 81.
			final double lld = 76; // Leather leggings durability: 76.
			final double lbd = 66; // Leather boots durability: 66.

			final double bd = 385; // Bow durability: 385.
			final double sd = 238; // Shears durability: 238.
			final double frd = 65; // Fishing rod durability: 65.
			final double fsd = 65; // Flint and steel durability: 65.

			final double m = b - d; // Discount base value.

			// Diamond tools.

			final double dtx = dtd / b;

			put(Material.DIAMOND_AXE, dtx);
			put(Material.DIAMOND_PICKAXE, dtx);
			put(Material.DIAMOND_SPADE, dtx);
			put(Material.DIAMOND_SWORD, dtx);
			put(Material.DIAMOND_HOE, dtx);

			// Iron tools.

			final double itx = dtd / b;

			put(Material.IRON_AXE, itx);
			put(Material.IRON_PICKAXE, itx);
			put(Material.IRON_SPADE, itx);
			put(Material.IRON_SWORD, itx);
			put(Material.IRON_HOE, itx);

			// Stone tools.

			final double stx = dtd / (d * (std - gtd) / (itd - gtd) + m);

			put(Material.STONE_AXE, stx);
			put(Material.STONE_PICKAXE, stx);
			put(Material.STONE_SPADE, stx);
			put(Material.STONE_SWORD, stx);
			put(Material.STONE_HOE, stx);

			// Wood tools.

			final double wtx = dtd / (d * (wtd - gtd) / (itd - gtd) + m);

			put(Material.WOOD_AXE, wtx);
			put(Material.WOOD_PICKAXE, wtx);
			put(Material.WOOD_SPADE, wtx);
			put(Material.WOOD_SWORD, wtx);
			put(Material.WOOD_HOE, wtx);

			// Gold tools.

			final double gtx = dtd / m;

			put(Material.GOLD_AXE, gtx);
			put(Material.GOLD_PICKAXE, gtx);
			put(Material.GOLD_SPADE, gtx);
			put(Material.GOLD_SWORD, gtx);
			put(Material.GOLD_HOE, gtx);

			// Diamond armor.

			put(Material.DIAMOND_HELMET, dhd / b);
			put(Material.DIAMOND_CHESTPLATE, dcd / b);
			put(Material.DIAMOND_LEGGINGS, dld / b);
			put(Material.DIAMOND_BOOTS, dbd / b);

			// Iron armor.

			put(Material.IRON_HELMET, dhd / b);
			put(Material.IRON_CHESTPLATE, dcd / b);
			put(Material.IRON_LEGGINGS, dld / b);
			put(Material.IRON_BOOTS, dbd / b);

			// Chainmail armor.

			put(Material.CHAINMAIL_HELMET, dhd / (d * (chd - lhd) / (ihd - lhd) + m));
			put(Material.CHAINMAIL_CHESTPLATE, dcd / (d * (ccd - lcd) / (icd - lcd) + m));
			put(Material.CHAINMAIL_LEGGINGS, dld / (d * (cld - lld) / (ild - lld) + m));
			put(Material.CHAINMAIL_BOOTS, dbd / (d * (cbd - lbd) / (ibd - lbd) + m));

			// Gold armor.

			put(Material.GOLD_HELMET, dhd / (d * (ghd - lhd) / (ihd - lhd) + m));
			put(Material.GOLD_CHESTPLATE, dcd / (d * (gcd - lcd) / (icd - lcd) + m));
			put(Material.GOLD_LEGGINGS, dld / (d * (gld - lld) / (ild - lld) + m));
			put(Material.GOLD_BOOTS, dbd / (d * (gbd - lbd) / (ibd - lbd) + m));

			// Leather armor.

			put(Material.LEATHER_HELMET, dhd / m);
			put(Material.LEATHER_CHESTPLATE, dcd / m);
			put(Material.LEATHER_LEGGINGS, dld / m);
			put(Material.LEATHER_BOOTS, dbd / m);

			// Special.

			put(Material.BOW, bd / b);
			put(Material.SHEARS, sd / b);
			put(Material.FISHING_ROD, frd / b);
			put(Material.FLINT_AND_STEEL, fsd / b);
		}};
	}

	// Final data.
	final protected Global global = new Global();

	public Configuration() {
		// Placeholder.
	}
}
