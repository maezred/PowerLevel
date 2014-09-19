package com.moltendorf.bukkit.powerlevel;

import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by moltendorf on 14/09/03.
 *
 * @author moltendorf
 */
public class Plugin extends JavaPlugin {

    // Variable data.
    protected Configuration configuration = null;

    @Override
    public synchronized void onDisable() {

        // Clear data.
        configuration = null;
    }

    @Override
    public synchronized void onEnable() {

        // Construct new configuration.
        configuration = new Configuration();

        // Are we enabled?
        if (!configuration.global.enabled) {
            return;
        }

        // Get server.
        final Server server = getServer();

        // Get plugin manager.
        final PluginManager manager = server.getPluginManager();

        // Register our event listeners.
        manager.registerEvents(new Listeners(this), this);
    }
}
