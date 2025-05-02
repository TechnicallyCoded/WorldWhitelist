package com.tcoded.whitelistworld.command;

import com.tcoded.whitelistworld.PlayerDataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WorldWhitelistCommand implements CommandExecutor {

    private final PlayerDataManager manager;

    public WorldWhitelistCommand(PlayerDataManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 4 || !args[0].equalsIgnoreCase("set")) {
            sender.sendMessage(ChatColor.RED + "Usage: /worldwhitelist set <player> <world> <true|false>");
            return true;
        }

        String playerName = args[1];
        String worldName = args[2];
        boolean access;
        if (args[3].equalsIgnoreCase("true")) {
            access = true;
        } else if (args[3].equalsIgnoreCase("false")) {
            access = false;
        } else {
            sender.sendMessage(ChatColor.RED + "Last argument must be true or false.");
            return true;
        }

        if (manager.setAccess(playerName, worldName, access)) {
            sender.sendMessage(ChatColor.GREEN + "Set access for player " + playerName + " to world " + worldName + ": " + access);
        } else {
            sender.sendMessage(ChatColor.RED + "Player not found: " + playerName);
        }
        return true;
    }
}