package com.tcoded.whitelistworld;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    private final JavaPlugin plugin;
    private File dataFolder;
    private final Map<UUID, PlayerData> dataMap;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataMap = new ConcurrentHashMap<>();
    }

    public void init() {
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public void loadAllOnline() {
        plugin.getServer().getOnlinePlayers()
                .stream()
                .map(OfflinePlayer::getUniqueId)
                .forEach(this::get);
    }

    public PlayerData get(UUID uuid) {
        return dataMap.computeIfAbsent(uuid, this::loadData);
    }

    private PlayerData loadData(UUID uuid) {
        File file = new File(dataFolder, uuid.toString() + ".yml");
        if (!file.exists()) {
            return new PlayerData();
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return PlayerData.deserialize(config.getConfigurationSection("data"));
    }

    public void saveData(UUID uuid) {
        PlayerData data = dataMap.get(uuid);
        if (data == null) {
            return;
        }
        if (data.getAllowedWorlds().isEmpty()) {
            File file = new File(dataFolder, uuid.toString() + ".yml");
            if (file.exists()) {
                file.delete();
            }
            dataMap.remove(uuid);
            return;
        }
        File file = new File(dataFolder, uuid.toString() + ".yml");
        FileConfiguration config = new YamlConfiguration();
        config.createSection("data");
        data.serialize(config.getConfigurationSection("data"));
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data for " + uuid);
            e.printStackTrace();
        }
    }

    public void unload(UUID uuid) {
        saveData(uuid);
        dataMap.remove(uuid);
    }

    public boolean setAccess(String playerName, String worldName, boolean access) {
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(playerName);
        if (offlinePlayer == null || (!offlinePlayer.hasPlayedBefore() && !offlinePlayer.isOnline())) {
            return false;
        }
        UUID uuid = offlinePlayer.getUniqueId();
        PlayerData data = get(uuid);
        data.setAccess(worldName, access);
        return true;
    }
}