package com.moltendorf.bukkit.powerlevel;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

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

		// Since we require Unbreaking III, we don't need to worry about updating the client's perceived durability.
		if (item.getEnchantmentLevel(Enchantment.DURABILITY) < 3) {
			return;
		}

		short durability = item.getDurability();
		short maxDurability = type.getMaxDurability();

		// +1 to avoid flickering health bar on tool.
		if ((maxDurability - durability + 1) < maxDurability) {
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

			final double experienceMean = plugin.configuration.global.equipmentValues.get(type);
			final int experience = (int) experienceMean;

			final int currentExperience = playerHandler.xp.getCurrentExp();

			if (currentExperience - Math.ceil(experienceMean) >= plugin.configuration.global.repairExperience) {
				final int experienceChange;

				if (Math.random() > experienceMean - experience) {
					experienceChange = 0 - experience;
				} else {
					experienceChange = 0 - experience - 1;
				}

				playerHandler.xp.changeExp(experienceChange);
				item.setDurability((short) (durability - 1));
			}
		}
	}
}
