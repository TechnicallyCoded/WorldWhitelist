package com.tcoded.whitelistworld;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerData {

    private final Set<String> allowedWorlds;

    public PlayerData() {
        this.allowedWorlds = new HashSet<>();
    }

    public Set<String> getAllowedWorlds() {
        return allowedWorlds;
    }

    public boolean hasAccess(String worldName) {
        return allowedWorlds.contains(worldName);
    }

    public void setAccess(String worldName, boolean access) {
        if (access) {
            allowedWorlds.add(worldName);
        } else {
            allowedWorlds.remove(worldName);
        }
    }

    public void serialize(ConfigurationSection section) {
        section.set("allowedWorlds", List.copyOf(allowedWorlds));
    }

    public static PlayerData deserialize(ConfigurationSection section) {
        PlayerData data = new PlayerData();
        if (section != null && section.isList("allowedWorlds")) {
            List<String> worlds = section.getStringList("allowedWorlds");
            data.allowedWorlds.addAll(worlds);
        }
        return data;
    }

}