package com.tcoded.whitelistworld.listener;

import com.tcoded.whitelistworld.PlayerData;
import com.tcoded.whitelistworld.PlayerDataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class WorldAccessListener implements Listener {

    private final PlayerDataManager manager;
    private final List<String> protectedWorlds;

    public WorldAccessListener(PlayerDataManager manager, List<String> protectedWorlds) {
        this.manager = manager;
        this.protectedWorlds = protectedWorlds;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getTo() == null) return;

        World world = event.getTo().getWorld();
        checkWorld(event, event.getPlayer(), world);
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {

    }

    private void checkWorld(Cancellable event, Player player, World world) {
        String worldName = world.getName();
        // If world is not in the protected list, allow
        if (!protectedWorlds.contains(worldName)) return;

        PlayerData data = manager.get(player.getUniqueId());
        if (!data.hasAccess(worldName)) {
            event.setCancelled(true);
            player.sendMessage(Component.text("You do not have access to this world!", NamedTextColor.RED));

            if (world.getEnvironment() == World.Environment.NETHER) {
                player.sendMessage(Component.text("Unlock the Nether with prestige level! /spawn", NamedTextColor.RED));
            }
        }
    }

}