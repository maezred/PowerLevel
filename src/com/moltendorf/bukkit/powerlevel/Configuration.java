package com.moltendorf.bukkit.powerlevel;

import org.bukkit.Material;

import java.util.Arrays;
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

		final protected int repairExperience = 11105;
		final protected double repairExperienceCost = 1895;

		final protected HashSet<Material> blockEquipment = new HashSet<>(Arrays.asList(
			Material.DIAMOND_AXE,
			Material.DIAMOND_PICKAXE,
			Material.DIAMOND_SPADE,
			Material.DIAMOND_SWORD
		));

		final protected HashSet<Material> diamondEquipment = new HashSet<>(Arrays.asList(
			Material.DIAMOND_AXE,
			Material.DIAMOND_PICKAXE,
			Material.DIAMOND_SPADE,
			Material.DIAMOND_SWORD
		));

		final protected double diamondDiscount = .8;
	}

	// Final data.
	final protected Global global = new Global();

	public Configuration() {
		// Placeholder.
	}
}
