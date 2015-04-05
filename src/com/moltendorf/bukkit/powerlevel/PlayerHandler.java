package com.moltendorf.bukkit.powerlevel;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author moltendorf
 */
public class PlayerHandler {
	public Plugin plugin;
	public Player player;
	public BukkitScheduler scheduler;

	protected ExperienceManager xp;

	protected int durabilityChanges = 0;
	protected double experienceChange = 0;
	protected int task = -1;

	private Set<PotionEffectType> currentEffects = new LinkedHashSet<>(3);
	private Set<PotionEffect> currentPotions = new LinkedHashSet<>(3);

	private int currentEffectLevel = -1;

	private long cooldown = 0;

	public PlayerHandler(Plugin plugin, Player player) {
		this.plugin = plugin;
		this.player = player;

		scheduler = plugin.getServer().getScheduler();

		xp = new ExperienceManager(player);
	}

	private int amplifier(final int effectLevel, final int startingEffectLevel) {
		return amplifier(effectLevel, startingEffectLevel, 0);
	}

	private int amplifier(final int effectLevel, final int startingEffectLevel, final int maximumAmplifier) {
		final int finalEffectLevel = startingEffectLevel + maximumAmplifier;

		int amplifier = effectLevel - startingEffectLevel;

		if (amplifier > maximumAmplifier) {
			return maximumAmplifier;
		} else {
			return amplifier < 1 ? 0 : amplifier;
		}
	}

	public void changeExp(double input) {
		experienceChange += input;

		if (task == -1) {
			task = scheduler.scheduleSyncDelayedTask(plugin, () -> {
				int change = (int) experienceChange;

				if (Math.abs(change) >= 64) {
					experienceChange -= change;

					xp.changeExp(change);
				}

				task = -1;
			}, 20 * 15);
		}
	}

	public void refreshEffects() {
		// Intentionally doing integer division.
		int effectLevel = (player.getLevel() - 45) / 5; // Level 50 == effectLevel 1.

		double health = player.getHealth();

		currentEffects.forEach(player::removePotionEffect);

		if (effectLevel != currentEffectLevel) {
			Deque<String> messages = new LinkedList<>();

			currentPotions.clear();

			// Level 50-55 bonuses.
			if (effectLevel >= 1) {
				int amplifier = amplifier(effectLevel, 1, 1);

				if (amplifier >= 0) {
					currentPotions.add(new PotionEffect(PotionEffectType.JUMP, 1200, amplifier, true));
				}

				if (effectLevel > currentEffectLevel) {
					if (currentEffectLevel < 2) {
						messages.addLast("§2Jump Boost " + StringUtils.repeat("I", amplifier + 1) + " synthesized.");
					}
				} else if (currentEffectLevel > 1 && effectLevel < 2) {
					messages.addFirst("§4Jump Boost " + StringUtils.repeat("I", amplifier + 2) + " dispersed.");
				}

			} else if (currentEffectLevel >= 1) {
				messages.addFirst("§4Jump Boost I dispersed.");
			}

			// Level 60-65 bonuses.
			if (effectLevel >= 3) {
				int amplifier = amplifier(effectLevel, 3, 1);

				if (amplifier >= 0) {
					currentPotions.add(new PotionEffect(PotionEffectType.SPEED, 1200, amplifier, true));
				}

				if (effectLevel > currentEffectLevel) {
					if (currentEffectLevel < 4) {
						messages.addLast("§2Speed " + StringUtils.repeat("I", amplifier + 1) + " synthesized.");
					}
				} else if (currentEffectLevel > 3 && effectLevel < 4) {
					messages.addFirst("§4Speed " + StringUtils.repeat("I", amplifier + 2) + " dispersed.");
				}

			} else if (currentEffectLevel >= 3) {
				// currentEffectLevel assumed to be higher than 0.
				messages.addFirst("§4Speed I dispersed.");
			}

			// Level 70+ bonus.
			if (effectLevel >= 5) {
				if (currentEffectLevel < 5) {
					messages.addLast("§2Repair synthesized.");
				}
			} else if (currentEffectLevel >= 5) {
				messages.addFirst("§4Repair dispersed.");
			}

			// Level 75-95 bonuses.
			if (effectLevel >= 6) {
				int amplifier = amplifier(effectLevel, 6, 4);

				if (amplifier >= 0) {
					currentPotions.add(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1200, amplifier, true));
				}

				if (effectLevel > currentEffectLevel) {
					if (currentEffectLevel < 10) {
						// Add extra hearts so the player doesn't have to regenerate.
						health += (1 + amplifier - amplifier(currentEffectLevel, 6, 4)) * 4;

						cooldown = System.currentTimeMillis();

						int number = amplifier + 1;

						String string;

						if (number > 4) {
							string = "V";
						} else if (number > 3) {
							string = "IV";
						} else {
							string = StringUtils.repeat("I", number);
						}

						messages.addLast("§2Health Boost " + string + " synthesized.");
					}
				} else if (currentEffectLevel > 6 && effectLevel < 10) {
					if ((System.currentTimeMillis() - cooldown) < 60000L && health > 8) {
						// Remove extra hearts to prevent exploits.
						health -= (1 + amplifier(currentEffectLevel, 6, 4) - amplifier) * 4;

						// Don't freaking kill them!
						if (health < 8) {
							health = 8;
						}
					}

					int number = amplifier + 2;

					String string;

					if (number > 4) {
						string = "V";
					} else if (number > 3) {
						string = "IV";
					} else {
						string = StringUtils.repeat("I", number);
					}

					messages.addFirst("§4Health Boost " + string + " dispersed.");
				}

			} else if (currentEffectLevel >= 6) {
				if ((System.currentTimeMillis() - cooldown) < 60000L && health > 8) {
					// Remove extra hearts to prevent exploits.
					health -= (1 + amplifier(currentEffectLevel, 6, 4)) * 4;

					// Don't freaking kill them!
					if (health < 8) {
						health = 8;
					}
				}

				// currentEffectLevel assumed to be higher than 0.
				messages.addFirst("§4Health Boost I dispersed.");
			}

			Set<PotionEffectType> effects = new LinkedHashSet<>();

			for (PotionEffect potion : currentPotions) {
				effects.add(potion.getType());
			}

			currentEffects = effects;
			currentEffectLevel = effectLevel;

			messages.forEach(player::sendMessage);
		}

		player.addPotionEffects(currentPotions);

		double maxHealth = player.getMaxHealth();

		if (health <= maxHealth) {
			player.setHealth(health);
		} else {
			player.setHealth(maxHealth);
		}
	}
}
