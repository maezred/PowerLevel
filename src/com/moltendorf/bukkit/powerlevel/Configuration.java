package com.moltendorf.bukkit.powerlevel;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Configuration class.
 * Created by moltendorf on 14/09/03.
 *
 * @author moltendorf
 */
public class Configuration {

	static protected class Global {

		// Final data.
		final protected boolean enabled = true; // Whether or not the plugin is enabled at all; useful for using it as an interface (default is true).

		final protected int repairExperience = 8765;

		final protected HashSet<Material> blockEquipment = new HashSet<>(Arrays.asList(
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

		final protected HashMap<Material, Double> equipmentValues = new HashMap<Material, Double>() {{
			final double d = .15; // Discount.

			final double x = 1395; // Experience for repairs to full durability.

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

			final double m = 1 - d; // Discount base value.

			// Diamond tools.

			final double dtx = x / dtd;

			put(Material.DIAMOND_AXE, dtx);
			put(Material.DIAMOND_PICKAXE, dtx);
			put(Material.DIAMOND_SPADE, dtx);
			put(Material.DIAMOND_SWORD, dtx);
			put(Material.DIAMOND_HOE, dtx);

			// Iron tools.

			final double itx = x / dtd * (d * (itd - gtd) / (dtd - gtd) + m);

			put(Material.IRON_AXE, itx);
			put(Material.IRON_PICKAXE, itx);
			put(Material.IRON_SPADE, itx);
			put(Material.IRON_SWORD, itx);
			put(Material.IRON_HOE, itx);

			// Stone tools.

			final double stx = x / dtd * (d * (std - gtd) / (dtd - gtd) + m);

			put(Material.STONE_AXE, stx);
			put(Material.STONE_PICKAXE, stx);
			put(Material.STONE_SPADE, stx);
			put(Material.STONE_SWORD, stx);
			put(Material.STONE_HOE, stx);

			// Wood tools.

			final double wtx = x / dtd * (d * (wtd - gtd) / (dtd - gtd) + m);

			put(Material.WOOD_AXE, wtx);
			put(Material.WOOD_PICKAXE, wtx);
			put(Material.WOOD_SPADE, wtx);
			put(Material.WOOD_SWORD, wtx);
			put(Material.WOOD_HOE, wtx);

			// Gold tools.

			final double gtx = x / dtd * m;

			put(Material.GOLD_AXE, gtx);
			put(Material.GOLD_PICKAXE, gtx);
			put(Material.GOLD_SPADE, gtx);
			put(Material.GOLD_SWORD, gtx);
			put(Material.GOLD_HOE, gtx);

			// Diamond armor.

			put(Material.DIAMOND_HELMET, x / dhd);
			put(Material.DIAMOND_CHESTPLATE, x / dcd);
			put(Material.DIAMOND_LEGGINGS, x / dld);
			put(Material.DIAMOND_BOOTS, x / dbd);

			// Iron armor.

			put(Material.IRON_HELMET, x / dhd * (d * (ihd - lhd) / (dhd - lhd) + m));
			put(Material.IRON_CHESTPLATE, x / dcd * (d * (icd - lcd) / (dcd - lcd) + m));
			put(Material.IRON_LEGGINGS, x / dld * (d * (ild - lld) / (dld - lld) + m));
			put(Material.IRON_BOOTS, x / dbd * (d * (ibd - lbd) / (dbd - lbd) + m));

			// Chainmail armor.

			put(Material.CHAINMAIL_HELMET, x / dhd * (d * (chd - lhd) / (dhd - lhd) + m));
			put(Material.CHAINMAIL_CHESTPLATE, x / dcd * (d * (ccd - lcd) / (dcd - lcd) + m));
			put(Material.CHAINMAIL_LEGGINGS, x / dld * (d * (cld - lld) / (dld - lld) + m));
			put(Material.CHAINMAIL_BOOTS, x / dbd * (d * (cbd - lbd) / (dbd - lbd) + m));

			// Gold armor.

			put(Material.GOLD_HELMET, x / dhd * (d * (ghd - lhd) / (dhd - lhd) + m));
			put(Material.GOLD_CHESTPLATE, x / dcd * (d * (gcd - lcd) / (dcd - lcd) + m));
			put(Material.GOLD_LEGGINGS, x / dld * (d * (gld - lld) / (dld - lld) + m));
			put(Material.GOLD_BOOTS, x / dbd * (d * (gbd - lbd) / (dbd - lbd) + m));

			// Leather armor.

			put(Material.LEATHER_HELMET, x / dhd * +m);
			put(Material.LEATHER_CHESTPLATE, x / dcd * m);
			put(Material.LEATHER_LEGGINGS, x / dld * m);
			put(Material.LEATHER_BOOTS, x / dbd * m);

			// Special.

			put(Material.BOW, x / bd);
			put(Material.SHEARS, x / sd);
			put(Material.FISHING_ROD, x / frd);
			put(Material.FLINT_AND_STEEL, x / fsd);
		}};
	}

	// Final data.
	final protected Global global = new Global();

	public Configuration() {
		// Placeholder.
	}
}
