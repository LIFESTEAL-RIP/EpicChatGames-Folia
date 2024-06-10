package gg.minecrush.reactions.command;

import gg.minecrush.reactions.ReactionManager;
import gg.minecrush.reactions.Reactions;
import gg.minecrush.reactions.storage.yaml.Messages;
import gg.minecrush.reactions.storage.yaml.Words;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import gg.minecrush.reactions.util.color;

import java.util.Arrays;
import java.util.List;

public class ReactionCommand implements CommandExecutor {

    private final ReactionManager reactionManager;
    private final JavaPlugin plugin;
    private final Messages messages;
    private final Words wordsManager;

    public ReactionCommand(ReactionManager reactionManager, JavaPlugin plugin, Messages messages, Words wordsManager) {
        this.reactionManager = reactionManager;
        this.messages = messages;
        this.plugin = plugin;
        this.wordsManager = wordsManager;
    }

    //

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(color.c(getMessage("usage-message")));
            return false;
        }
        if ("stop".equalsIgnoreCase(args[0])) {
            if (reactionManager.isReactionActive() == true) {
                reactionManager.endReaction();
                sender.sendMessage(color.c(getMessage("reaction-ended")));
            } else {
                sender.sendMessage(color.c(getMessage("no-reaction-active")));
            }
        } else if ("start".equalsIgnoreCase(args[0])) {
            if (args.length > 1) {
                if (reactionManager.isReactionActive() != true) {
                    List<String> words = wordsManager.getWordList("words");
                    if (words.isEmpty()) {
                        sender.sendMessage(color.c(getMessage("reaction-empty")));
                    } else {
                        reactionManager.startReaction(args[1]);
                    }
                } else {
                    sender.sendMessage(color.c(getMessage("reaction-already-active")));
                }
            } else {
                sender.sendMessage(color.c(getMessage("specify-reaction")));
            }
        } else if ("add".equalsIgnoreCase(args[0])) {
            if (args.length > 1) {
                String word = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                List<String> words = wordsManager.getWordList("words");
                if (!words.contains(word)) {
                    wordsManager.addWord(word);
                    sender.sendMessage(color.c(getMessage("word-added")));
                } else {
                    sender.sendMessage(color.c(getMessage("word-already-exists")));
                }
            } else {
                sender.sendMessage(color.c(getMessage("specify-word")));
            }
        } else if ("remove".equalsIgnoreCase(args[0])) {
            if (args.length > 1) {
                String word = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                List<String> words = wordsManager.getWordList("words");
                if (words.contains(word)) {
                    wordsManager.removeWord(word);
                    sender.sendMessage(color.c(getMessage("word-removed").replace("%word%", word)));
                } else {
                    sender.sendMessage(color.c(getMessage("word-not-found").replace("%word%", word)));
                }
            } else {
                sender.sendMessage(getMessage("specify-word"));
            }
        } else if ("list".equalsIgnoreCase(args[0])) {
            List<String> words = wordsManager.getWordList("words");
            sender.sendMessage(color.c(getMessage("reaction-words") + words));
        } else if ("reload".equalsIgnoreCase(args[0])){

            long startTime = System.currentTimeMillis();

            plugin.reloadConfig();
            if (plugin instanceof Reactions) {
                ((Reactions) plugin).onReload();
            }
            wordsManager.reloadConfig();
            messages.reloadConfig();

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            sender.sendMessage(color.c(getMessage("config-reloaded")).replace("%time%", elapsedTime + ""));
        } else {
            sender.sendMessage(color.c(getMessage("unknown-command")));
        }

        return true;
    }

    private String getMessage(String key) {
        return messages.getReplacedMessage(key);
    }

    // To do:
    // Redo how commands are executed
}
