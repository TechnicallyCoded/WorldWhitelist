package com.tcoded.whitelistworld.listener;

import com.tcoded.whitelistworld.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PreLoginListener implements Listener {

    private final PlayerDataManager manager;

    public PreLoginListener(PlayerDataManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        manager.get(event.getUniqueId());
    }

}