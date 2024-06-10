package gg.minecrush.reactions.storage.yaml;

import gg.minecrush.reactions.util.color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Words {
    private final Plugin plugin;
    private File configFile;
    private FileConfiguration config;
    private String filePath = "words.yml";

    public Words(Plugin plugin) {
        this.plugin = plugin;
        createConfig();
    }

    public String getFilePath() {
        return filePath;
    }

    private void createConfig() {
        configFile = new File(plugin.getDataFolder(), filePath);
        if (!configFile.exists()) {
            plugin.saveResource(filePath, false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public String getWord(String key) {
        String message = config.getString(key);
        if (message == null) {
            return "";
        }
        return color.c(message);
    }

    public void addWord(String word) {
        List<String> words = getWordList("words");
        if (!words.contains(word)) {
            words.add(word);
            config.set("words", words);
        }
        saveConfig();
    }

    public void removeWord(String word) {
        List<String> words = getWordList("words");
        if (words.contains(word)) {
            words.remove(word);
            config.set("words", words);
        }
        saveConfig();
    }

    public List<String> getWordList(String key) {
        List<String> messages = config.getStringList(key);
        if (messages == null || messages.isEmpty()) {
            return new ArrayList<>();
        }
        return messages;
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), filePath);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
