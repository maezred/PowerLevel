package com.moltendorf.bukkit.powerlevel;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Created by moltendorf on 14/09/03.
 */
public class Listeners implements Listener {

	final protected Plugin plugin;

	protected BukkitTask clock = null;

	protected Map<UUID, PlayerHandler> players = new LinkedHashMap<>();

	protected Listeners(final Plugin instance) {
		plugin = instance;

		for (Player player : plugin.getServer().getOnlinePlayers()) {
			refreshEffects(player);
		}

		final Runnable runnable;

		runnable = new Runnable() {

			@Override
			public void run() {
				for (PlayerHandler playerHandler : players.values()) {
					playerHandler.refreshEffects();
				}
			}
		};

		clock = plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, 0, 100);
	}

	public void refreshEffects(final Player player) {
		final UUID id = player.getUniqueId();

		final PlayerHandler playerHandler;
		PlayerHandler fetchedPlayerHandler = players.get(id);

		// This should never happen.
		if (fetchedPlayerHandler == null) {
			playerHandler = new PlayerHandler(player);
			players.put(id, playerHandler);
		} else {
			playerHandler = fetchedPlayerHandler;
		}

		playerHandler.refreshEffects();
	}

	public void repairArmor(final Player player) {
		final List<ItemStack> equipment = new LinkedList<>(Arrays.asList(player.getEquipment().getArmorContents()));
		final Map<Material, ItemState> lookup = new LinkedHashMap<>();

		for (Iterator<ItemStack> iterator = equipment.listIterator(); iterator.hasNext(); ) {
			final ItemStack item = iterator.next();
			final Material type = item.getType();

			if (!plugin.configuration.global.armorEquipment.contains(type)) {
				continue;
			}

			if (item.getEnchantmentLevel(Enchantment.DURABILITY) < 3) {
				continue;
			}

			lookup.put(type, new ItemState(item));
		}

		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				final UUID id = player.getUniqueId();

				final PlayerHandler playerHandler;
				PlayerHandler fetchedPlayerHandler = players.get(id);

				// This should never happen.
				if (fetchedPlayerHandler == null) {
					playerHandler = new PlayerHandler(player);
					players.put(id, playerHandler);
				} else {
					playerHandler = fetchedPlayerHandler;
				}

				final List<ItemStack> equipment = new LinkedList<>(Arrays.asList(player.getEquipment().getArmorContents()));

				double experienceMean = 0;
				int totalDurability = 0;

				for (Iterator<ItemStack> iterator = equipment.listIterator(); iterator.hasNext(); ) {
					final ItemStack item = iterator.next();
					final Material type = item.getType();

					final ItemState state = lookup.get(type);

					if (state == null) {
						continue;
					}

					if (!state.update(item)) {
						lookup.remove(type);

						continue;
					}

					final short durability = item.getDurability();

					if (durability > 1) {
						if (durability <= state.durability) {
							// If equal then remove, but if it's less than, then: W.T.F.? Hacks? But still remove.
							lookup.remove(type);

							continue;
						}

						final int difference;

						if (state.durability > 0) {
							difference = durability - state.durability;
						} else {
							// Minus 1 to preserve the health bar.
							difference = durability - 1;
						}

						if (difference > 12) {
							player.sendMessage("Could not restore " + difference + " durability.");

							// W.T.F.? Hacks?
							lookup.remove(type);

							continue;
						}

						final Double experienceDenominator = plugin.configuration.global.equipmentValueMultipliers.get(type);

						if (experienceDenominator == null) {
							// We can't repair this tool.
							lookup.remove(type);

							continue;
						}

						final Integer baseCost = plugin.configuration.global.equipmentBaseValues.get(type);

						if (baseCost == null) {
							// We can't repair this tool.
							lookup.remove(type);

							continue;
						}

						final Map<Enchantment, Integer> enchantments = item.getEnchantments();

						final Integer countCost = plugin.configuration.global.enchantmentCountValues.get(enchantments.size());

						if (countCost == null) {
							// We can't repair this tool.
							lookup.remove(type);

							continue;
						}

						int levelCost = baseCost + countCost;

						for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
							final Integer cost = plugin.configuration.global.enchantmentBaseValues.get(entry.getKey());

							// Free discount if Bukkit or PowerLevel isn't up to date.
							if (cost != null) {
								levelCost += cost * entry.getValue();
							}
						}

						final int repair;

						// 5% roll for +1.
						if (state.durability > 1 && Math.random() < .05) {
							repair = difference + 1;

							state.durability -= 1;
						} else {
							repair = difference;
						}

						totalDurability += repair;

						experienceMean += repair * playerHandler.xp.getXpForLevel(levelCost) / experienceDenominator;
					} else {
						lookup.remove(type);
					}
				}

				// Nothing to repair.
				if (experienceMean == 0) {
					return;
				}

				final int experienceCeil = (int) Math.ceil(experienceMean);
				final int experienceFloor = (int) experienceMean;

				final int currentExperience = playerHandler.xp.getCurrentExp();

				// Don't do anything unless the player has enough experience to repair all of their equipment.
				if (currentExperience - experienceCeil >= plugin.configuration.global.repairExperience) {
					final int experienceChange;

					if (Math.random() > experienceMean - experienceFloor) {
						experienceChange = 0 - experienceFloor;
					} else {
						experienceChange = 0 - experienceCeil;
					}

					player.sendMessage("Restored " + totalDurability + " durability for " + (-experienceChange) + " experience.");

					playerHandler.xp.changeExp(experienceChange);

					String message = "";

					// Repair all damaged equipment.
					for (ItemState state : lookup.values()) {
						message += " (" + (state.type.getMaxDurability() - state.durability) + "/" + state.type.getMaxDurability() + ")";

						state.item.setDurability(state.durability);
					}

					player.sendMessage(message);
				}
			}
		};

		plugin.getServer().getScheduler().runTask(plugin, runnable);
	}

	public void repairTool(final Player player, final ItemStack item, final Material type) {
		// Since we require Unbreaking III, we don't need to worry about updating the client's perceived durability.
		if (item.getEnchantmentLevel(Enchantment.DURABILITY) < 3) {
			return;
		}

		final ItemState state = new ItemState(item);

		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				final ItemStack item = player.getItemInHand();

				if (!state.update(item)) {
					return;
				}

				final short durability = item.getDurability();

				// +1 to avoid flickering health bar on tool.
				if (durability > 1) {
					if (durability <= state.durability) {
						// If equal then remove, but if it's less than, then: W.T.F.? Hacks? But still remove.
						return;
					}

					final int difference;

					if (state.durability > 0) {
						difference = durability - state.durability;
					} else {
						// Minus 1 to preserve the health bar.
						difference = durability - 1;
					}

					if (difference > 3) {
						player.sendMessage("Could not restore " + difference + " durability.");

						// W.T.F.? Hacks?
						return;
					}

					final UUID id = player.getUniqueId();

					final PlayerHandler playerHandler;
					PlayerHandler fetchedPlayerHandler = players.get(id);

					// This should never happen.
					if (fetchedPlayerHandler == null) {
						playerHandler = new PlayerHandler(player);
						players.put(id, playerHandler);
					} else {
						playerHandler = fetchedPlayerHandler;
					}

					final Double experienceDenominator = plugin.configuration.global.equipmentValueMultipliers.get(type);

					if (experienceDenominator == null) {
						// We can't repair this tool.
						return;
					}

					final Integer baseCost = plugin.configuration.global.equipmentBaseValues.get(type);

					if (baseCost == null) {
						// We can't repair this tool.
						return;
					}

					final Map<Enchantment, Integer> enchantments = item.getEnchantments();

					final Integer countCost = plugin.configuration.global.enchantmentCountValues.get(enchantments.size());

					if (countCost == null) {
						// We can't repair this tool.
						return;
					}

					int levelCost = baseCost + countCost;

					for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
						final Integer cost = plugin.configuration.global.enchantmentBaseValues.get(entry.getKey());

						// Free discount if Bukkit or PowerLevel isn't up to date.
						if (cost != null) {
							levelCost += cost * entry.getValue();
						}
					}

					final int repair;

					// 5% roll for +1.
					if (state.durability > 1 && Math.random() < .05) {
						repair = difference + 1;

						state.durability -= 1;
					} else {
						repair = difference;
					}

					final double experienceMean = repair * playerHandler.xp.getXpForLevel(levelCost) / experienceDenominator;

					final int experienceCeil = (int) Math.ceil(experienceMean);
					final int experienceFloor = (int) experienceMean;

					final int currentExperience = playerHandler.xp.getCurrentExp();

					if (currentExperience - experienceCeil >= plugin.configuration.global.repairExperience) {
						final int experienceChange;

						if (Math.random() > experienceMean - experienceFloor) {
							experienceChange = 0 - experienceFloor;
						} else {
							experienceChange = 0 - experienceCeil;
						}

						player.sendMessage("Restored " + repair + " durability for " + (-experienceChange) + " experience.");

						playerHandler.xp.changeExp(experienceChange);
						item.setDurability(state.durability);

						player.sendMessage(" (" + (state.type.getMaxDurability() - state.durability) + "/" + state.type.getMaxDurability() + ")");
					}
				}
			}
		};

		plugin.getServer().getScheduler().runTask(plugin, runnable);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void BlockBreakEventMonitor(final BlockBreakEvent event) {

		// Are we enabled at all?
		if (!plugin.configuration.global.enabled) {
			return;
		}

		final Player player = event.getPlayer();

		if (player == null) {
			return;
		}

		ItemStack item = player.getItemInHand();
		Material type = item.getType();

		if (!plugin.configuration.global.blockEquipment.contains(type)) {
			return;
		}

		repairTool(player, item, type);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void BlockIgniteEventMonitor(final BlockIgniteEvent event) {

		// Are we enabled at all?
		if (!plugin.configuration.global.enabled) {
			return;
		}

		final Player player = event.getPlayer();

		if (player == null) {
			return;
		}

		ItemStack item = player.getItemInHand();
		Material type = item.getType();

		if (!plugin.configuration.global.fireEquipment.contains(type)) {
			return;
		}

		repairTool(player, item, type);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void EntityDamageByEntityEventMonitor(final EntityDamageByEntityEvent event) {

		// Are we enabled at all?
		if (!plugin.configuration.global.enabled) {
			return;
		}

		final Entity damager = event.getDamager();

		if (damager.getType() == EntityType.PLAYER) {
			final Player player = (Player) damager;

			ItemStack item = player.getItemInHand();
			Material type = item.getType();

			if (plugin.configuration.global.weaponEquipment.contains(type)) {
				repairTool(player, item, type);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void EntityDamageEventMonitor(final EntityDamageEvent event) {

		// Are we enabled at all?
		if (!plugin.configuration.global.enabled) {
			return;
		}

		if (event.getEntityType() == EntityType.PLAYER) {
			repairArmor((Player) event.getEntity());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void EntityShootBowEventMonitor(final EntityShootBowEvent event) {

		// Are we eneabled at all?
		if (!plugin.configuration.global.enabled) {
			return;
		}

		final Entity entity = event.getEntity();

		if (entity.getType() == EntityType.PLAYER) {
			final Player player = (Player) entity;

			ItemStack item = player.getItemInHand();
			Material type = item.getType();

			if (!plugin.configuration.global.bowEquipment.contains(type)) {
				return;
			}

			repairTool(player, item, type);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void PlayerExpChangeEventMonitor(final PlayerExpChangeEvent event) {

		// Are we enabled at all?
		if (!plugin.configuration.global.enabled) {
			return;
		}

		final Player player = event.getPlayer();

		refreshEffects(player);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void PlayerFishEventMonitor(final PlayerFishEvent event) {

		// Are we enabled at all?
		if (!plugin.configuration.global.enabled) {
			return;
		}

		if (event.getState() != PlayerFishEvent.State.FISHING) {

		}

		final Player player = event.getPlayer();

		ItemStack item = player.getItemInHand();
		Material type = item.getType();

		if (!plugin.configuration.global.fishingEquipment.contains(type)) {
			return;
		}

		repairTool(player, item, type);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void PlayerInteractEventMonitor(final PlayerInteractEvent event) {

		// Are we enabled at all?
		if (!plugin.configuration.global.enabled) {
			return;
		}

		final ItemStack item = event.getItem();

		if (item == null) {
			return;
		}

		final Material type = item.getType();

		if (!plugin.configuration.global.farmEquipment.contains(type)) {
			return;
		}

		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		if (!plugin.configuration.global.farmBlocks.contains(event.getClickedBlock().getType())) {
			return;
		}

		repairTool(event.getPlayer(), item, type);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void PlayerJoinEventMonitor(final PlayerJoinEvent event) {

		// Are we enabled at all?
		if (!plugin.configuration.global.enabled) {
			return;
		}

		final Player player = event.getPlayer();

		refreshEffects(player);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void PlayerQuitEventMonitor(final PlayerQuitEvent event) {

		// Are we enabled at all?
		if (!plugin.configuration.global.enabled) {
			return;
		}

		final Player player = event.getPlayer();

		players.remove(player.getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void PlayerShearEntityEventMonitor(final PlayerShearEntityEvent event) {

		// Are we enabled at all?
		if (!plugin.configuration.global.enabled) {
			return;
		}

		final Player player = event.getPlayer();

		ItemStack item = player.getItemInHand();
		Material type = item.getType();

		if (!plugin.configuration.global.shearEquipment.contains(type)) {
			return;
		}

		repairTool(player, item, type);
	}
}
