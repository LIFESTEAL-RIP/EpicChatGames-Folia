package gg.minecrush.reactions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ReactionListener implements Listener {

    private final ReactionManager reactionManager;

    public ReactionListener(ReactionManager reactionManager) {
        this.reactionManager = reactionManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (reactionManager.isAnswer(message)) {
            reactionManager.handleCorrectAnswer(player, message);
        }
    }
}
