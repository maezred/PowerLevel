package com.moltendorf.bukkit.powerlevel;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
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
}
