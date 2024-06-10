package gg.minecrush.reactions;

import gg.minecrush.reactions.async.AutomaticEvents;
import gg.minecrush.reactions.command.ReactionCommand;
import gg.minecrush.reactions.command.ReactionTabComplete;
import gg.minecrush.reactions.storage.yaml.Messages;
import gg.minecrush.reactions.storage.yaml.Words;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Random;
import java.util.logging.Logger;

public class Reactions extends JavaPlugin {

    private ReactionManager reactionManager;
    private static final Logger LOGGER = Logger.getLogger(Reactions.class.getName());
    private int automaticReactionsInterval;
    private Messages messages;
    private Words words;
    private AutomaticEvents automaticEvents;

    @Override
    public void onEnable() {
        getLogger().info("Reactions plugin is enabling...");
        try {

            try {
                File msgFile = new File(getDataFolder(), "words.yml");
                if (!msgFile.exists()) {
                    saveResource("words.yml", false);
                }
            } catch (Exception e) {
                getLogger().severe("[EpicChatGames] Failed to create words file");
                Bukkit.getPluginManager().disablePlugin(this);
            }

            try {
                File msgFile = new File(getDataFolder(), "lang.yml");
                if (!msgFile.exists()) {
                    saveResource("lang.yml", false);
                }
            } catch (Exception e) {
                getLogger().severe("[EpicChatGames] Failed to create language file");
                Bukkit.getPluginManager().disablePlugin(this);
            }

            this.messages = new Messages(this);
            this.words = new Words(this);


            this.saveDefaultConfig();
            reactionManager = new ReactionManager(this, words, messages);
            this.automaticReactionsInterval = this.getConfig().getInt("automaticReactionsInterval", 3);
            this.getCommand("chatgames").setExecutor(new ReactionCommand(reactionManager, this, messages, words));
            this.getCommand("chatgames").setTabCompleter(new ReactionTabComplete());
            this.getServer().getPluginManager().registerEvents(new ReactionListener(reactionManager), this);
//            if (this.getConfig().getBoolean("automaticReactionsEnabled", true)) {
//                scheduleAutomaticReactions();
//            }
            reactionManager.reset_Reaction();
            getLogger().info("Reactions plugin enabled successfully!");
            if (this.getConfig().getBoolean("automaticReactionsEnabled", true)) {
                this.automaticEvents = new AutomaticEvents(reactionManager, this);
            }

        } catch (Exception e) {
            getLogger().severe("Failed to enable Reactions plugin: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void onDisable() {
        getLogger().info("Reactions plugin is disabling...");
    }

    public Reactions() {
    }

    public void onReload() {
        this.reloadConfig();
        this.automaticReactionsInterval = this.getConfig().getInt("automaticReactionsInterval", 3); // Update the automaticReactionsInterval value
        this.reactionManager = new ReactionManager(this, words, messages);
        this.getCommand("chatgames").setExecutor(new ReactionCommand(reactionManager, this, messages, words));
        this.getServer().getPluginManager().registerEvents(new ReactionListener(reactionManager), this);
        if (this.getConfig().getBoolean("automaticReactionsEnabled", true)) {
            this.automaticEvents = new AutomaticEvents(reactionManager, this);
        }
    }
}
