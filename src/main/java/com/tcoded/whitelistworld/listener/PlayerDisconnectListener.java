package com.tcoded.whitelistworld.listener;

import com.tcoded.whitelistworld.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlayerDisconnectListener implements Listener {

    private final PlayerDataManager manager;
    private final JavaPlugin plugin;

    public PlayerDisconnectListener(PlayerDataManager manager, JavaPlugin plugin) {
        this.manager = manager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> manager.saveData(uuid));
    }

}