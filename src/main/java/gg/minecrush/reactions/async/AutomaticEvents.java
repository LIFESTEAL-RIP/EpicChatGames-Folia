package gg.minecrush.reactions.async;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import gg.minecrush.reactions.ReactionManager;
import gg.minecrush.reactions.Reactions;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class AutomaticEvents {

    private final ReactionManager reactionManager;
    private final JavaPlugin plugin;

    private int automaticReactionsInterval;
    private WrappedTask currentTask;

    private static final String[] REACTION_TYPES = {"math", "scramble", "fastest"};

    public AutomaticEvents(ReactionManager reactionManager, JavaPlugin plugin) {
        this.reactionManager = reactionManager;
        this.plugin = plugin;
        loadConfig();
        scheduleAutomaticReactions();
    }

    private void loadConfig() {
        automaticReactionsInterval = plugin.getConfig().getInt("automaticReactionsInterval", 3);
    }

    public synchronized void scheduleAutomaticReactions() {
        cancelCurrentTask();
        plugin.getLogger().info("Scheduling new automatic reaction task.");
        currentTask = Reactions.getInstance().foliaLib.getImpl().runTimerAsync(() -> {
                    if (!reactionManager.isReactionActive()) {
                        Random random = new Random();
                        String type = REACTION_TYPES[random.nextInt(REACTION_TYPES.length)];
                        reactionManager.startReaction(type);
                    }
        }, 60 * 20, 20L * 60 * automaticReactionsInterval);
    }

    public synchronized void cancelCurrentTask() {
        if (currentTask != null && !currentTask.isCancelled()) {
            plugin.getLogger().info("Cancelling current automatic reaction task.");
            currentTask.cancel();
        }
    }

    public void reload() {
        loadConfig();
        scheduleAutomaticReactions();
    }
}
