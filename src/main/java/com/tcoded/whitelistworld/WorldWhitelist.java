package com.tcoded.whitelistworld;

import com.tcoded.whitelistworld.command.WorldWhitelistCommand;
import com.tcoded.whitelistworld.listener.PlayerDisconnectListener;
import com.tcoded.whitelistworld.listener.PreLoginListener;
import com.tcoded.whitelistworld.listener.WorldAccessListener;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldWhitelist extends JavaPlugin {

    private PlayerDataManager manager;

    @Override
    public void onEnable() {
        this.manager = new PlayerDataManager(this);
        this.manager.init();
        this.manager.loadAllOnline();

        // Ensure config.yml is saved
        saveDefaultConfig();
        // Load protected worlds from config
        var protectedWorlds = getConfig().getStringList("whitelisted-worlds");

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PreLoginListener(manager), this);
        pluginManager.registerEvents(new PlayerDisconnectListener(manager, this), this);
        pluginManager.registerEvents(new WorldAccessListener(manager, protectedWorlds), this);

        getCommand("worldwhitelist").setExecutor(new WorldWhitelistCommand(manager));

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        this.getServer().getScheduler().cancelTasks(this);

        if (manager != null) {
            // Save and unload data for all currently online players
            getServer().getOnlinePlayers().forEach(player -> manager.unload(player.getUniqueId()));
            manager = null;
        }
    }

}