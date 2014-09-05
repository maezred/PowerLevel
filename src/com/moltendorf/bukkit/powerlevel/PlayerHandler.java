package com.moltendorf.bukkit.powerlevel;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * @author moltendorf
 */
public class PlayerHandler {
	public Player player;

	private Set<PotionEffectType> currentEffects = new LinkedHashSet<>(3);
	private Set<PotionEffect> currentPotions = new LinkedHashSet<>(3);

	private int currentEffectLevel = -1;

	public PlayerHandler(Player player) {
		this.player = player;
	}

	private int amplifier(final int effectLevel, final int startingEffectLevel) {
		return amplifier(effectLevel, startingEffectLevel, 0);
	}

	private int amplifier(final int effectLevel, final int startingEffectLevel, final int maximumAmplifier) {
		final int finalEffectLevel = startingEffectLevel + maximumAmplifier;

		for (int i = startingEffectLevel; ; ++i) {
			if (i == finalEffectLevel || i == effectLevel) {
				return i - startingEffectLevel;
			}
		}
	}

	public void refreshEffects() {
		// Intentionally doing integer division.
		int effectLevel = (player.getLevel() - 50) / 5; // Level 50 == effectLevel 0.

		double health = player.getHealth();

		for (PotionEffectType effect : currentEffects) {
			player.removePotionEffect(effect);
		}

		if (effectLevel != currentEffectLevel) {
			currentPotions.clear();

			// Level 55-60 bonuses.
			if (effectLevel >= 1) {
				int amplifier = amplifier(effectLevel, 1, 1);

				if (amplifier >= 0) {
					currentPotions.add(new PotionEffect(PotionEffectType.JUMP, 180, amplifier, true));
				}
			}

			// Level 65-70 bonuses.
			if (effectLevel >= 3) {
				int amplifier = amplifier(effectLevel, 3, 1);

				if (amplifier >= 0) {
					currentPotions.add(new PotionEffect(PotionEffectType.SPEED, 180, amplifier, true));
				}
			}

			// Level 75-95 bonuses.
			if (effectLevel >= 5) {
				int amplifier = amplifier(effectLevel, 5, 4);

				if (amplifier >= 0) {
					currentPotions.add(new PotionEffect(PotionEffectType.HEALTH_BOOST, 180, amplifier, true));
				}
			}

			Set<PotionEffectType> effects = new LinkedHashSet<>();

			for (PotionEffect potion : currentPotions) {
				effects.add(potion.getType());
			}

			currentEffects = effects;
			currentEffectLevel = effectLevel;
		}

		player.addPotionEffects(currentPotions);

		if (health <= player.getMaxHealth()) {
			player.setHealth(health);
		}
	}
}
