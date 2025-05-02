package com.tcoded.whitelistworld.listener;

import com.tcoded.whitelistworld.PlayerData;
import com.tcoded.whitelistworld.PlayerDataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class WorldAccessListener implements Listener {

    private final PlayerDataManager manager;
    private final List<String> protectedWorlds;

    public WorldAccessListener(PlayerDataManager manager, List<String> protectedWorlds) {
        this.manager = manager;
        this.protectedWorlds = protectedWorlds;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getTo() == null) return;

        String worldName = event.getTo().getWorld().getName();
        // If world is not in the protected list, allow
        if (!protectedWorlds.contains(worldName)) return;

        Player player = event.getPlayer();
        PlayerData data = manager.get(player.getUniqueId());
        if (!data.hasAccess(worldName)) {
            event.setCancelled(true);
            player.sendMessage(Component.text("You do not have access to this world!", NamedTextColor.RED));

            if (event.getTo().getWorld().getEnvironment() == World.Environment.NETHER) {
                player.sendMessage(Component.text("Unlock the Nether with prestige level! /spawn", NamedTextColor.RED));
            }
        }
    }

}