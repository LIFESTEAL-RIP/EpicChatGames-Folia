package gg.minecrush.reactions.storage.yaml;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(plugin.getDataFolder(), "config.yml");
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }

    public FileConfiguration getConfig() {
        if (customConfig == null) {
            reloadConfig();
        }
        return customConfig;
    }

    public void saveConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getConfig().save(customConfigFile);
        } catch (IOException ex) {
            plugin.getLogger().severe("Could not save config to " + customConfigFile);
        }
    }

    public void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(plugin.getDataFolder(), "config.yml");
        }
        if (!customConfigFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }
    // To do:
    // Switch config managment system
    // Use different files for Words, Messages (Language) & Config.
}
